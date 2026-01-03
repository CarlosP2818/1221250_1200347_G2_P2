package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.dto.BookDto;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.OutboxEventRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events.BookFoundReply;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxRepository;
    private final BookRepository bookRepository;
    private final BookEventsPublisher publisher;

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {

        var events =
                outboxRepository.findUnprocessedEvents(new Page(1, 10));

        for (OutboxEventMongo event : events) {
            try {
                if (event.getType().equals("BOOK_CREATED") && !event.isProcessed()) {

                    Optional<Book> book = bookRepository.findByIsbn(event.getAggregateId());

                    if (book.isEmpty()) {
                        throw new IllegalStateException(
                                "User with ID " + event.getAggregateId() + " not found"
                        );
                    }

                    Book bookEntity = book.get();

                    BookFoundReply bookReply = new BookFoundReply(
                            event.getCorrelationId(),
                            BookDto.builder()
                                    .isbn(bookEntity.getIsbn())
                                    .title(bookEntity.getTitle().toString())
                                    .description(bookEntity.getDescription())
                                    .genreId(bookEntity.getGenreId())
                                    .authorsIds(bookEntity.getAuthorsIds())
                                    .photoURI(
                                            bookEntity.getPhoto() != null
                                                    ? bookEntity.getPhoto().getPhotoFile()
                                                    : null
                                    )
                                    .version(bookEntity.getVersion())
                                    .sagaId(bookEntity.getSagaId())
                                    .status(bookEntity.getStatus())
                                    .build()
                    );

                    publisher.publishBooKCreated(bookReply);
                }

                event.setProcessed(true);

                outboxRepository.save(event);

            } catch (Exception e) {
                // Log the exception and continue with the next event
                e.printStackTrace();
            }
        }
    }
}

