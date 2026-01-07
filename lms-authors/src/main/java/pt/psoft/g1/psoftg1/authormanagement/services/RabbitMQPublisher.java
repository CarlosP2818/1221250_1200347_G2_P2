package pt.psoft.g1.psoftg1.authormanagement.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier; // Import necessário
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorTempCreatedEvent;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;

@Service
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Construtor manual para usar o @Qualifier
    public RabbitMQPublisher(
            RabbitTemplate rabbitTemplate,
            @Qualifier("authorExchange") DirectExchange directExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

    public void publishAuthorCreated(Author author) {
            AuthorTempCreatedEvent event = new AuthorTempCreatedEvent(author.getName(), author.getBio());

            rabbitTemplate.convertAndSend(directExchange.getName(), "author.created", event);
    }

    public void publishTempAuthorCreated(OutboxEventMongo author) {
            // Aqui usas o sagaId que vem do MongoDB
            AuthorTempCreatedEvent event = new AuthorTempCreatedEvent(author.getName(), author.getBio());

            // IMPORTANTE: O routing key deve ser o mesmo que definiste no Binding do RabbitConfig
            rabbitTemplate.convertAndSend(directExchange.getName(), "author.created", event);
    }
}