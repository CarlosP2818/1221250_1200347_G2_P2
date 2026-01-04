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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ExtendWith(PactConsumerTestExt.class)
@PactConsumerTest
@PactTestFor(providerName = "author_event-producer", providerType = ProviderType.ASYNCH, pactVersion = PactSpecVersion.V4)
public class AuthorCDCDefinitionTest {

    @MockBean
    AuthorService authorService;

    @Pact(consumer = "author_created-consumer")
    V4Pact createAuthorCreatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody()
                .stringType("authorNumber", "1")
                .stringType("name", "J.K. Rowling")
                .stringType("bio", "Famous author of Harry Potter")
                .stringType("photoURI", "http://example.com/photo.jpg")
                .stringMatcher("version", "[0-9]+", "1");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("an author created event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    @Pact(consumer = "author_updated-consumer")
    V4Pact createAuthorUpdatedPact(MessagePactBuilder builder) {
        PactDslJsonBody body = new PactDslJsonBody()
                .stringType("authorNumber", "1")
                .stringType("name", "Joanne Rowling")
                .stringType("bio", "Updated bio")
                .stringType("photoURI", "http://example.com/newphoto.jpg")
                .stringMatcher("version", "[0-9]+", "2");

        Map<String, Object> metadata = new HashMap<>();
        metadata.put("Content-Type", "application/json");

        return builder.expectsToReceive("an author updated event")
                .withMetadata(metadata)
                .withContent(body)
                .toPact();
    }

    @Test
    @PactTestFor(pactMethod = "createAuthorCreatedPact")
    void testAuthorCreated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        // Aqui podes adicionar o código para simular a recepção da mensagem no listener
        // Exemplo comentado:
        // String jsonReceived = messages.get(0).contentsAsString();
        // assertDoesNotThrow(() -> listener.receiveAuthorCreatedMsg(jsonReceived));
        // verify(authorService, times(1)).create(any(CreateAuthorRequest.class));
    }

    @Test
    @PactTestFor(pactMethod = "createAuthorUpdatedPact")
    void testAuthorUpdated(List<V4Interaction.AsynchronousMessage> messages) throws Exception {
        // Similar ao teste acima
    }
}
