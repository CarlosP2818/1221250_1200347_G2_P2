package CDC.consumer;

import au.com.dius.pact.core.model.DefaultPactReader;
import au.com.dius.pact.core.model.Pact;
import au.com.dius.pact.core.model.PactReader;
import au.com.dius.pact.core.model.messaging.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.mongo.MongoDataOutboxEventRepo;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq.GenreReplyListener;
import pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq.events.GenreCreatedEvent;

import java.io.File;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = { GenreReplyListener.class }
)
class GenreCDCConsumerIT {

    @MockBean
    RabbitTemplate rabbitTemplate;

    @MockBean
    MongoDataOutboxEventRepo mongoDataOutboxEventRepo;

    @MockBean
    GenreRepository genreRepository;

    @Test
    void testGenreCreatedEventProcessing() throws Exception {

        File pactFile = new File(
                "target/pacts/genre_created-consumer-genre_event-producer.json"
        );

        PactReader pactReader = DefaultPactReader.INSTANCE;
        Pact pact = pactReader.loadPact(pactFile);

        List<Message> messages =
                pact.asMessagePact().get().getMessages();

        ObjectMapper mapper = new ObjectMapper();

        for (Message pactMessage : messages) {

            String json = pactMessage.contentsAsString();

            GenreCreatedEvent event =
                    mapper.readValue(json, GenreCreatedEvent.class);

            GenreReplyListener listener = new GenreReplyListener(
                    rabbitTemplate,
                    mongoDataOutboxEventRepo,
                    genreRepository
            );

            assertDoesNotThrow(() -> listener.receive(event));

            // verificações mínimas de comportamento
            verify(mongoDataOutboxEventRepo, times(2)).save(any());
            verify(genreRepository, times(1)).save(any());
            verify(rabbitTemplate, times(1))
                    .convertAndSend(
                            any(String.class),
                            any(String.class),
                            Optional.ofNullable(any())
                    );
        }
    }
}