package pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.readermanagement.model.Dto.CreateReaderRequestDto;
import pt.psoft.g1.psoftg1.readermanagement.model.Dto.UserDto;
import pt.psoft.g1.psoftg1.readermanagement.services.CreateReaderRequest;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq.events.UserFoundReply;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class UserReplyListener {

    private final Map<String, CompletableFuture<UserDto>> pendingRequests = new ConcurrentHashMap<>();
    private final ReaderService readerService;

    public CompletableFuture<UserDto> register(String correlationId) {
        CompletableFuture<UserDto> future = new CompletableFuture<>();
        pendingRequests.put(correlationId, future);
        return future;
    }

    @RabbitListener(queues = "user.reply.queue")
    public void handle(UserFoundReply reply) {
        CompletableFuture<UserDto> future =
                pendingRequests.remove(reply.correlationId());

        if (future != null) {
            future.complete(reply.user());
        }
    }

    @RabbitListener(queues = "temp.user.created.queue")
    public void handleUserCreated(UserFoundReply reply) {
        if (reply == null || reply.user() == null || reply.user().getUsername() == null || reply.createReaderRequest()== null) {
            System.err.println("Mensagem inválida recebida, descartando: " + reply);
            return; // Descarta a mensagem para não reprocessar
        }
        UserDto user = reply.user();
        CreateReaderRequest request = getCreateReaderRequest(reply, user);

        // Chama o serviço de criação de reader
        readerService.create(request, null);

        // Se quiser, pode logar ou enviar algum reply
        System.out.println("Reader criado a partir do user: " + user.getUsername() + " | correlationId: " + reply.correlationId());
    }

    private static @NotNull CreateReaderRequest getCreateReaderRequest(UserFoundReply reply, UserDto user) {
        CreateReaderRequestDto createReaderRequest = reply.createReaderRequest();

        // Monta a request para criar o reader
        CreateReaderRequest request = new CreateReaderRequest();
        request.setUsername(user.getUsername());
        request.setFullName(user.getName().getName());
        request.setPassword(user.getPassword()); // opcional, se você quiser manter a mesma
        request.setGdpr(true); // default
        request.setMarketing(false);
        request.setThirdParty(false);
        request.setReader(user.getId());
        request.setBirthDate(createReaderRequest.getBirthDate());
        request.setPhoneNumber(createReaderRequest.getPhoneNumber());
        request.setInterestList(createReaderRequest.getInterestList());
        return request;
    }
}
