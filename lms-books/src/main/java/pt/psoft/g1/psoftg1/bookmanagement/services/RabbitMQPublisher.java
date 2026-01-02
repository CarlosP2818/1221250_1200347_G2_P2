package pt.psoft.g1.psoftg1.bookmanagement.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookTempCreatedEvent;
import pt.psoft.g1.psoftg1.bookmanagement.api.TempBook;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;

@Service
@RequiredArgsConstructor
public class RabbitMQPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public void publishBookCreated(Book book) {
        try {
            BookTempCreatedEvent event = new BookTempCreatedEvent(
                    book.getSagaId(),
                    book.getVersion(),
                    new Isbn(book.getIsbn()),
                    book.getTitle(),
                    book.getGenreId(),
                    book.getAuthorsIds(),
                    book.getDescription()
            );
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(directExchange.getName(), "book.created", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void publishTempBookCreated(TempBook tempBook) {
        try {
            BookTempCreatedEvent event = new BookTempCreatedEvent(
                    tempBook.getSagaId(),
                    tempBook.getVersion(),
                    new Isbn(tempBook.getIsbn()),
                    new Title(tempBook.getTitle()),
                    tempBook.getGenreId(),
                    tempBook.getAuthorsIds(),
                    tempBook.getDescription().toString()
            );
            String message = objectMapper.writeValueAsString(event);
            rabbitTemplate.convertAndSend(directExchange.getName(), "book.temp.created", message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}