package pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.genremanagement.services.CreateGenreRequest;

@Service
public class Publisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange genreExchange;

    public Publisher(RabbitTemplate rabbitTemplate, @Qualifier("genreExchange") DirectExchange genreExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.genreExchange = genreExchange;
    }

    public void sendCreateGenreEvent(CreateGenreRequest request, String correlationId) {
        CreateGenreRequest event = new CreateGenreRequest(
                request.getGenreName(),
                correlationId
        );

        rabbitTemplate.convertAndSend(genreExchange.getName(), "author.created", event);
    }
}
