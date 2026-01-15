package CDC.consumer;

import au.com.dius.pact.consumer.MessagePactBuilder;
import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.junit5.PactConsumerTest;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.consumer.junit5.ProviderType;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.V4Interaction;
import au.com.dius.pact.core.model.V4Pact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import pt.psoft.g1.psoftg1.authormanagement.services.command.AuthorCommandService;
import pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.AuthorReplyListener;
import pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.events.CreateBookEvent;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
@PactConsumerTest
@PactTestFor(
        providerName = "author_event-producer",
        providerType = ProviderType.ASYNCH,
        pactVersion = PactSpecVersion.V4
)
class AuthorCDCDefinitionTest {

    AuthorReplyListener listener = Mockito.mock(AuthorReplyListener.class);
    AuthorCommandService commandService = Mockito.mock(AuthorCommandService.class);

    @Pact(consumer = "author_created-consumer")
    V4Pact authorCreatedPact(MessagePactBuilder builder) {

        PactDslJsonBody authorBody = new PactDslJsonBody()
                .stringType("name", "J.K. Rowling")
                .stringType("bio", "Famous author");

        PactDslJsonBody body = new PactDslJsonBody()
                .stringType("correlationId", "1234-uuid")
                .minArrayLike("authors", 1, authorBody, 1);

        Map<String, Object> metadata = Map.of(
                "Content-Type", "application/json"
        );

        return builder.expectsToReceive("author created event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "authorCreatedPact")
    void testAuthorCreated(List<V4Interaction.AsynchronousMessage> messages) {

        String json = messages.get(0).contentsAsString();

        ObjectMapper mapper = new ObjectMapper();

        assertDoesNotThrow(() -> {
            CreateBookEvent event = mapper.readValue(json, CreateBookEvent.class);
            assertEquals(1, event.getAuthors().size());
            assertEquals("J.K. Rowling", event.getAuthors().get(0).getName());
        });
    }
}
