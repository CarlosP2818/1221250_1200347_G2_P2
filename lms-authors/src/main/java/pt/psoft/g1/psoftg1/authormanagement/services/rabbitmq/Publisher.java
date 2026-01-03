package pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.events.AuthorCreatedEvent;

@Service
public class Publisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange authorExchange;

    public Publisher(RabbitTemplate rabbitTemplate, @Qualifier("authorExchange") DirectExchange authorExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.authorExchange = authorExchange;
    }

    public void sendCreateAuthorEvent(CreateAuthorRequest request, String correlationId) {
        AuthorCreatedEvent event = new AuthorCreatedEvent(
                request.getName(),
                request.getBio(),
                request.getPhoto(),
                correlationId
        );

        rabbitTemplate.convertAndSend(authorExchange.getName(), "author.create", event);
    }
}
