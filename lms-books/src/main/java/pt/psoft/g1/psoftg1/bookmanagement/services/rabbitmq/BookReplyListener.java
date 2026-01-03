package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
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

    // Map para guardar pedidos pendentes de criação de Book
    private final Map<String, CreateBookRequest> pendingCreateBook = new ConcurrentHashMap<>();

    private final BookService bookService;

    /**
     * Registra um novo pedido de criação de Book, com um correlationId único.
     */
    public void registerCreateBook(String correlationId, CreateBookRequest request) {
        pendingCreateBook.put(correlationId, request);
    }

    /**
     * Listener para replies de Author (confirmação que o Author foi criado)
     */
    @RabbitListener(queues = "author.reply.queue")
    public void handleAuthorReply(AuthorFoundReply reply) {
        CreateBookRequest request = pendingCreateBook.get(reply.correlationId());

        if (request == null) {
            System.err.println("Reply de Author sem pedido pendente: " + reply.correlationId());
            return;
        }

        request.setAuthorsIds(reply.book().getBook().getAuthorsIds()); // Marca Author como criado

        tryCreateBookIfReady(request, reply.correlationId());
    }

    /**
     * Listener para replies de Genre (confirmação que o Genre foi criado)
     */
    @RabbitListener(queues = "genre.reply.queue")
    public void handleGenreReply(GenreFoundReply reply) {
        CreateBookRequest request = pendingCreateBook.get(reply.correlationId());

        if (request == null) {
            System.err.println("Reply de Genre sem pedido pendente: " + reply.correlationId());
            return;
        }

        request.setGenreId(reply.genreId());

        tryCreateBookIfReady(request, reply.correlationId());
    }

    /**
     * Verifica se todos os dependencies (Author + Genre) estão prontos e cria o Book
     */
    private void tryCreateBookIfReady(CreateBookRequest request, String correlationId) {
        if (request.getAuthorsIds() != null && request.getGenreId() != null) {
            bookService.create(request);
            pendingCreateBook.remove(correlationId);
            System.out.println("Book criado com sucesso: " + request.getTitle());
        }
    }
}
