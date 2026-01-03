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
}
