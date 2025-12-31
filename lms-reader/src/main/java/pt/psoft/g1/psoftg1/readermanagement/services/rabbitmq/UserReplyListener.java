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

    private final Map<String, CreateReaderRequestDto> pendingCreateReader =
            new ConcurrentHashMap<>();

    private final ReaderService readerService;

    public void registerCreateReader(
            String correlationId,
            CreateReaderRequest request) {

        CreateReaderRequestDto dto = new CreateReaderRequestDto(
                request.getUsername(),
                request.getPassword(),
                request.getFullName(),
                request.getBirthDate(),
                request.getPhoneNumber(),
                request.getPhoto(),
                request.getGdpr(),
                request.getMarketing(),
                request.getThirdParty(),
                request.getInterestList(),
                request.getReader()
        );

        pendingCreateReader.put(correlationId, dto);
    }

    @RabbitListener(queues = "user.reply.queue")
    public void handle(UserFoundReply reply) {

        CreateReaderRequestDto dto = pendingCreateReader.remove(reply.correlationId());

        if (dto == null) {
            System.err.println(
                    "Sem contexto para correlationId: " + reply.correlationId()
            );
            return;
        }

        UserDto user = reply.user();

        CreateReaderRequest request = buildCreateReaderRequest(dto, user);

        readerService.create(request, null);

        System.out.println(
                "Reader criado para user " + user.getUsername()
                        + " | correlationId: " + reply.correlationId()
        );
    }

    private CreateReaderRequest buildCreateReaderRequest(
            CreateReaderRequestDto dto,
            UserDto user) {

        CreateReaderRequest request = new CreateReaderRequest();
        request.setUsername(user.getUsername());
        request.setFullName(user.getName().getName());
        request.setPassword(user.getPassword());
        request.setGdpr(true);
        request.setMarketing(false);
        request.setThirdParty(false);
        request.setReader(user.getId());
        request.setBirthDate(dto.getBirthDate());
        request.setPhoneNumber(dto.getPhoneNumber());
        request.setInterestList(dto.getInterestList());
        return request;
    }
}
