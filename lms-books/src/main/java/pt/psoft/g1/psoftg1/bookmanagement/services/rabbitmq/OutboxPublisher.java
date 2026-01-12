package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.bookmanagement.model.dto.BookDto;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.OutboxEventRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.BookFoundReply;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxRepository;
    private final BookEventsPublisher publisher;

    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {
        var events = outboxRepository.findUnprocessedEvents(new Page(1, 10));

        for (OutboxEventMongo event : events) {
            try {
                if ("BOOK_CREATION_REQUESTED".equals(event.getType()) && !event.isProcessed()) {

                    // 1. Prepara o Reply (Usa os dados do EVENTO, não da BD SQL)
                    BookFoundReply bookReply = new BookFoundReply(
                            event.getCorrelationId(),
                            BookDto.builder()
                                    .title(event.getAggregateId())
                                    .sagaId(UUID.fromString(event.getCorrelationId()))
                                    .build()
                    );

                    // 2. Publica para o RabbitMQ
                    publisher.publishBooKCreated(bookReply);
                    System.out.println("BOOKS-PUBLISHER: Mensagem enviada para o RabbitMQ.");

                }
            } catch (Exception e) {
                System.err.println("ERRO no Publisher: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

