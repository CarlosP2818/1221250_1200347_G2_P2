package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.OutboxEventRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.command.BookCommandService;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.AuthorFoundReply;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.GenreFoundReply;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class BookReplyListener {

    // Cache em memória para guardar o progresso da Saga
    private final Map<String, CreateBookRequest> pendingBooks = new ConcurrentHashMap<>();
    private final BookCommandService bookCommandService;
    private final RabbitTemplate rabbitTemplate; // Injeta isto no construtor
    private final OutboxEventRepository outboxRepository;

    public void registerCreateBook(String correlationId, CreateBookRequest request) {
        pendingBooks.put(correlationId, request);
    }

    @RabbitListener(queues = "author.book.reply.queue")
    public void handleAuthorReply(AuthorFoundReply reply) {
        if (reply == null || reply.correlationId() == null) return;

        String correlationId = reply.correlationId();
        CreateBookRequest request = pendingBooks.get(correlationId);

        if (request != null) {
            request.setAuthors(reply.authors());
            tryCreateBookIfReady(request, correlationId);
        }
    }

    @RabbitListener(queues = "genre.book.reply.queue")
    public void handleGenreReply(GenreFoundReply reply) {
        if (reply == null || reply.correlationId() == null) return;

        System.out.println("BOOKS: Recebi resposta do Genre! Nome: " + reply.genreName());
        String correlationId = reply.correlationId();

        CreateBookRequest request = pendingBooks.get(correlationId);

        if (request != null) {
            request.setGenreName(reply.genreName());
            tryCreateBookIfReady(request, correlationId);
        } else {
            System.err.println("BOOKS: Request não encontrado no Map para ID: " + correlationId);
        }
    }

    private void tryCreateBookIfReady(CreateBookRequest request, String correlationId) {
        boolean hasAuthors = request.getAuthors() != null && !request.getAuthors().isEmpty();
        boolean hasGenre = request.getGenreName() != null;

        if (hasAuthors && hasGenre) {
            pendingBooks.remove(correlationId);

            java.util.concurrent.CompletableFuture.runAsync(() -> {
                try {
                    bookCommandService.create(request);
                    System.out.println("BOOKS: SUCESSO! Livro criado com ISBN da Google API.");

                    outboxRepository.findByCorrelationId(correlationId).ifPresent(event -> {
                        event.setProcessed(true);
                        event.setType("BOOK_CREATED_SUCCESSFULLY");
                        outboxRepository.save(event);
                        System.out.println("BOOKS: Evento no MongoDB atualizado para PROCESSED=TRUE");
                    });

                } catch (Exception e) {
                    System.err.println("BOOKS: Erro ao criar livro: " + e.getMessage());
                }
            });

        } else {
            System.out.println("BOOKS: Ainda aguardando replies. Authors: " + hasAuthors + ", Genre: " + hasGenre);
        }
    }
}