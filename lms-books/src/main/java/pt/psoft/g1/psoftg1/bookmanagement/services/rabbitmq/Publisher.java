package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.CreateBookEvent;

@Service
public class Publisher {

    private final RabbitTemplate rabbitTemplate;

    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateAuthorEvent(CreateBookRequest request, String correlationId) {
        CreateBookEvent event = new CreateBookEvent();
        event.setCorrelationId(correlationId);
        event.setAuthors(request.getAuthors());

        rabbitTemplate.convertAndSend(
                "author.events.exchange", // O nome da Exchange de autores
                "author.created",         // A Routing Key exata que está no Config
                event
        );
        System.out.println("Authors" + event.getAuthors());
        System.out.println("BOOKS-PUBLISHER: Evento enviado para Author Service!");
    }

    public void sendCreateGenreEvent(CreateBookRequest request, String correlationId) {
        CreateBookEvent event = new CreateBookEvent();


        // Envia para a rota de géneros
        rabbitTemplate.convertAndSend(
                "genre.events.exchange",
                "genre.create",
                event
        );
    }
}