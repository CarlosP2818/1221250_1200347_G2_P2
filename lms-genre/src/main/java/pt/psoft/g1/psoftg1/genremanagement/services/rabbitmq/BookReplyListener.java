package pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.services.CreateGenreRequest;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq.events.GenreFoundReply;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class BookReplyListener {

    // Map para guardar pedidos pendentes de criação de Book
    private final Map<String, CreateGenreRequest> pendingCreateGenre = new ConcurrentHashMap<>();

    private final GenreService genreService;

    /**
     * Registra um novo pedido de criação de Book, com um correlationId único.
     */
    public void registerCreateGenre(String correlationId, CreateGenreRequest request) {
        pendingCreateGenre.put(correlationId, request);
    }

    /**
     * Listener para replies de Author (confirmação que o Author foi criado)
     */
    @RabbitListener(queues = "genre.reply.queue")
    public void handleGenreReply(GenreFoundReply reply) {
        CreateGenreRequest request = pendingCreateGenre.get(reply.correlationId());

        if (request == null) {
            System.err.println("Reply de Genre sem pedido pendente: " + reply.correlationId());
            return;
        }

        request.setId(reply.genreId());
        // Aqui você poderia notificar o BookReplyListener
        pendingCreateGenre.remove(reply.correlationId());
        System.out.println("Genre criado com sucesso: " + request.getName());
    }
}
