package pt.psoft.g1.psoftg1.authormanagement.messassing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorRabbitListener {

    private final TempAuthorRepository tempAuthorRepository;
    private final RabbitTemplate template;
    private final DirectExchange direct;

    private final ObjectMapper objectMapper = new ObjectMapper();
}
