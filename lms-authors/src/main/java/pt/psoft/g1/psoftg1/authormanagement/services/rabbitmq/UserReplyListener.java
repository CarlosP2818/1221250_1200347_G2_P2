package pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.events.AuthorFoundReply;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class UserReplyListener {

    private final Map<String, CreateAuthorRequest> pendingCreateAuthor =
            new ConcurrentHashMap<>();

    private final AuthorService authorService;

    public void registerCreateAuthor(String correlationId, CreateAuthorRequest request) {
        pendingCreateAuthor.put(correlationId, request);
    }

    @RabbitListener(queues = "author.reply.queue")
    public void handleAuthorReply(AuthorFoundReply reply) {
        CreateAuthorRequest request = pendingCreateAuthor.get(reply.correlationId());
        if (request == null) {
            System.err.println("Reply de Author sem pedido pendente: " + reply.correlationId());
            return;
        }

        pendingCreateAuthor.remove(reply.correlationId());

        System.out.println("Author criado com sucesso: " + request.getName());
    }
}
