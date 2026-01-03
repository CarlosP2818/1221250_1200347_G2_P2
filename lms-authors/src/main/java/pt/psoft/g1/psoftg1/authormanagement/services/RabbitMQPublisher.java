package pt.psoft.g1.psoftg1.authormanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorTempCreatedEvent;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;

@Service
@RequiredArgsConstructor
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange; // configurada no RabbitConfig
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publishAuthorCreated(Author author) {
        try {
            AuthorTempCreatedEvent event = new AuthorTempCreatedEvent(author.getSagaId(), author.getName(), author.getBio());
            String payload = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(directExchange.getName(), "author-created", payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // ou logar de forma adequada
        }
    }

    public void publishTempAuthorCreated(OutboxEventMongo author) {
        try {
            AuthorTempCreatedEvent event = new AuthorTempCreatedEvent(author.getSagaId(), author.getName(), author.getBio());
            String payload = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(directExchange.getName(), "author-temp-created", payload);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // ou logar de forma adequada
        }
    }
}
