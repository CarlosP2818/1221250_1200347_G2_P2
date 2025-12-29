package pt.psoft.g1.psoftg1.authormanagement.messassing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorCreatedEvent;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorTempCreatedEvent;
import pt.psoft.g1.psoftg1.authormanagement.api.TempAuthor;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.TempAuthorRepository;

@Component
@RequiredArgsConstructor
public class AuthorRabbitListener {

    private final TempAuthorRepository tempAuthorRepository;
    private final RabbitTemplate template;
    private final DirectExchange direct;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @RabbitListener(queues = "author-temp-created-queue")
    public void handleTempAuthorCreation(String payload) throws Exception {
        AuthorTempCreatedEvent event = objectMapper.readValue(payload, AuthorTempCreatedEvent.class);

        // Cria autor temporário
        TempAuthor tempAuthor = new TempAuthor();
        tempAuthor.setName(event.getName());
        tempAuthor.setBio(event.getBio());
        tempAuthor.setSagaId(event.getSagaId());

        tempAuthorRepository.save(tempAuthor);

        // Cria evento de retorno com ID do autor
        var confirmationEvent = new AuthorCreatedEvent(event.getSagaId(), tempAuthor.getId());

        String response = objectMapper.writeValueAsString(confirmationEvent);
        template.convertAndSend(direct.getName(), "author-created", response);
    }
}
