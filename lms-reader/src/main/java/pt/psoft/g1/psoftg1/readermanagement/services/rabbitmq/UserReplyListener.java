package pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa.ReaderDetailsJpaTemp;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.jpa.ReaderDetailsJpaTempRepositoryImpl;
import pt.psoft.g1.psoftg1.readermanagement.model.Dto.CreateReaderRequestDto;
import pt.psoft.g1.psoftg1.readermanagement.model.Dto.UserDto;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderDetailsTempRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.CreateReaderRequest;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq.events.UserFoundReply;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@RequiredArgsConstructor
public class UserReplyListener {

    private final Map<String, CreateReaderRequestDto> pendingCreateReader =
            new ConcurrentHashMap<>();

    private final ReaderService readerService;
    private final ReaderDetailsTempRepository readerDetailsJpaTempRepository;

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
            ReaderDetailsJpaTemp existing = readerDetailsJpaTempRepository
                    .findByCorrelationId(reply.correlationId())
                    .orElse(null);
            if (existing == null) {
                System.err.println(
                        "Ignorando reply com correlationId "
                                + reply.correlationId()
                                + ": sem pedido pendente correspondente."
                );
                return;
            }

            dto = new CreateReaderRequestDto(
                    existing.getUsername(),
                    existing.getPassword(),
                    existing.getFullName(),
                    existing.getBirthDate().getBirthDate().toString(),
                    existing.getPhoneNumber().getPhoneNumber(),
                    null,
                    existing.isGdprConsent(),
                    existing.isMarketingConsent(),
                    existing.isThirdPartySharingConsent(),
                    existing.getInterestList(),
                    existing.getReader()
            );
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
