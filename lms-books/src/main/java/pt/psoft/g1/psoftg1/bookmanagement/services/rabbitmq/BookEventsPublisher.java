package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.BookMongoMapper;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.BookFoundReply;
import pt.psoft.g1.psoftg1.shared.model.BookEvents;

@Service
public class BookEventsPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;
    private final BookMongoMapper bookMapper;

    public BookEventsPublisher(RabbitTemplate rabbitTemplate,
                               @Qualifier("repliesExchange") DirectExchange userExchange, BookMongoMapper mongoMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = userExchange;
        this.bookMapper = mongoMapper;
    }

    /**
     * OUTBOX-BASED publishing
     */
    public void publishBooKCreated(BookFoundReply payload) {

        rabbitTemplate.convertAndSend(
                exchange.getName(),
                BookEvents.BOOK_REPLY,
                payload
        );
    }}
