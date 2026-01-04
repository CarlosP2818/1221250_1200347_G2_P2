package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.AuthorFoundReply;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.GenreFoundReply;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class BookReplyListener {

    // Cache em memória para guardar o progresso da Saga
    private final Map<String, CreateBookRequest> pendingBooks = new ConcurrentHashMap<>();
    private final BookService bookService;
    private final RabbitTemplate rabbitTemplate; // Injeta isto no construtor

    public void registerCreateBook(String correlationId, CreateBookRequest request) {
        pendingBooks.put(correlationId, request);
    }

    @RabbitListener(queues = "author.reply.queue")
    public void handleAuthorReply(AuthorFoundReply reply) {
        System.out.println("BOOKS: Recebi resposta do Author! ID: " + reply.authors());
        String correlationId = reply.correlationId();
        CreateBookRequest request = pendingBooks.get(correlationId);

        if (request != null) {
            // Atualiza o pedido com o ID técnico que veio do microserviço de Autores
            request.setAuthors(reply.authors());
            tryCreateBookIfReady(request, correlationId);
        } else {
            System.err.println("BOOKS: Erro - Pedido não encontrado para ID: " + correlationId);
        }
    }

    @RabbitListener(queues = "genre.reply.queue")
    public void handleGenreReply(GenreFoundReply reply) {
        System.out.println("BOOKS: Recebi resposta do Genre! ID: " + reply.genreId());
        String correlationId = reply.correlationId();
        CreateBookRequest request = pendingBooks.get(correlationId);

        if (request != null) {
            // Atualiza o pedido com o ID técnico que veio do microserviço de Géneros
            request.setGenreId(reply.genreId());
            tryCreateBookIfReady(request, correlationId);
        } else {
            System.err.println("BOOKS: Erro - Pedido não encontrado para ID: " + correlationId);
        }
    }

    private void tryCreateBookIfReady(CreateBookRequest request, String correlationId) {
        boolean hasAuthors = request.getAuthors() != null && !request.getAuthors().isEmpty();
        boolean hasGenre = request.getGenreId() != null;

        if (hasAuthors && hasGenre) {
            bookService.create(request);

            // --- ADICIONA ISTO PARA O NÍVEL 3 ---
            // Enviamos uma resposta para a exchange de replies que o Author está a ouvir
            AuthorFoundReply finalReply = new AuthorFoundReply(correlationId, request.getAuthors());

            rabbitTemplate.convertAndSend(
                    "book.replies.exchange",
                    "author.reply",
                    finalReply
            );
            // ------------------------------------

            pendingBooks.remove(correlationId);
            System.out.println("BOOKS: SUCESSO! Transação finalizada e resposta enviada ao Author.");
        }
    }
}