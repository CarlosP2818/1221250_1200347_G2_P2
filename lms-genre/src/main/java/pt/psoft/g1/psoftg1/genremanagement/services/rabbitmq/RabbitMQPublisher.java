package pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreTempCreatedEvent;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

@Service
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RabbitMQPublisher(
            RabbitTemplate rabbitTemplate,
            @Qualifier("genreExchange") DirectExchange directExchange
    ) {
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
    }

    public void publishGenreCreated(OutboxEventMongo genre) {
        try {
            GenreTempCreatedEvent event = new GenreTempCreatedEvent(genre.getName());
            rabbitTemplate.convertAndSend(directExchange.getName(), "genre.created", event);

            System.out.println("Published event for: " + genre.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}