package CDC.producer;

import au.com.dius.pact.provider.MessageAndMetadata;
import au.com.dius.pact.provider.PactVerifyProvider;
import au.com.dius.pact.provider.junit5.MessageTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.loader.PactFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.messaging.Message;
import pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq.events.GenreCreatedEvent;

import java.util.HashMap;
import java.util.Map;

@Provider("genre_event-producer")
@PactFolder("target/pacts")
public class GenreProducerCDCIT {

    @BeforeEach
    void setup(PactVerificationContext context) {
        if (context != null) {
            context.setTarget(new MessageTestTarget());
        }
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @PactVerifyProvider("genre created event")
    public MessageAndMetadata verifyGenreCreatedEvent() throws Exception {

        GenreCreatedEvent event = new GenreCreatedEvent(
                "Fiction",
                "abcd-uuid"
        );

        Message<String> message = new GenreMessageBuilder()
                .withEvent(event)
                .build();

        Map<String, Object> metadata = new HashMap<>();
        message.getHeaders().forEach(metadata::put);

        return new MessageAndMetadata(
                message.getPayload().getBytes(),
                metadata
        );
    }
}