package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.AuthorBookEvent;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.GenreCreatedEvent;

@Service
public class Publisher {

    private final RabbitTemplate rabbitTemplate;

    public Publisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCreateAuthorEvent(CreateBookRequest request, String correlationId) {
        System.out.println("DEBUG: Enviando autores: " + request.getAuthors());
        AuthorBookEvent event = new AuthorBookEvent();
        event.setCorrelationId(correlationId);
        event.setAuthors(request.getAuthors());

        rabbitTemplate.convertAndSend(
                "author.events.exchange",
                "author.created",
                event
        );
        System.out.println("Authors" + event.getAuthors());
        System.out.println("BOOKS-PUBLISHER: Evento enviado para Author Service!");
    }

    public void sendCreateGenreEvent(CreateBookRequest request, String correlationId) {
        GenreCreatedEvent event = new GenreCreatedEvent();
        event.setCorrelationId(correlationId);
        event.setGenreName(request.getGenreName());

        // Envia para a rota de géneros
        rabbitTemplate.convertAndSend(
                "genre.events.exchange",
                "genre.created",
                event
        );
        System.out.println("Genre: " + event.getGenreName());
        System.out.println("BOOKS-PUBLISHER: Evento enviado para Genre Service!");
    }
}