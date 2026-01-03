package pt.psoft.g1.psoftg1.genremanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreTempCreatedEvent;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

@Service
@RequiredArgsConstructor
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange; // configurada no RabbitConfig
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publishGenreCreated(Genre genre) {
        try {
            GenreTempCreatedEvent event = new GenreTempCreatedEvent(genre.getSagaId(), genre.getGenre());
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(directExchange.getName(), "genre.created", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishTempGenreCreated(OutboxEventMongo genre) {
        try {
            GenreTempCreatedEvent event = new GenreTempCreatedEvent(genre.getSagaId(), genre.getGenre());
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(directExchange.getName(), "genre.temp.created", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}