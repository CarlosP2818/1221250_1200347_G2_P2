package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.CreateBookEvent;

@Service
public class Publisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange bookExchange;

    // Ajustado o Qualifier para o seu contexto de Books
    public Publisher(RabbitTemplate rabbitTemplate, @Qualifier("bookExchange") DirectExchange bookExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.bookExchange = bookExchange;
    }

    public void sendCreateAuthorEvent(CreateBookRequest request, String correlationId) {
        // Importante: O evento deve levar o correlationId
        CreateBookEvent event = new CreateBookEvent();
        event.setCorrelationId(correlationId);
        event.setAuthorsId(request.getAuthorsIds());
        event.setDescription(request.getDescription());
        event.setGenreId(request.getGenreId());
        event.setTitle(request.getTitle());

        // Envia para a rota de autores
        rabbitTemplate.convertAndSend(
                bookExchange.getName(),
                "author.create",
                event
        );
    }

    public void sendCreateGenreEvent(CreateBookRequest request, String correlationId) {
        CreateBookEvent event = new CreateBookEvent();


        // Envia para a rota de géneros
        rabbitTemplate.convertAndSend(
                bookExchange.getName(),
                "genre.create",
                event
        );
    }
}