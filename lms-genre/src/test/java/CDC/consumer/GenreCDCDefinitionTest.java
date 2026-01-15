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
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq.GenreReplyListener;
import pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq.events.GenreCreatedEvent;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactConsumerTest
@PactTestFor(
        providerName = "genre_event-producer",
        providerType = ProviderType.ASYNCH,
        pactVersion = PactSpecVersion.V4
)
class GenreCDCDefinitionTest {

  GenreReplyListener listener = Mockito.mock(GenreReplyListener.class);
  GenreService genreService = Mockito.mock(GenreService.class);

  @Pact(consumer = "genre_created-consumer")
  V4Pact genreCreatedPact(MessagePactBuilder builder) {

    PactDslJsonBody genreBody = new PactDslJsonBody()
            .stringType("id", "1")
            .stringType("name", "Fiction");

    PactDslJsonBody body = new PactDslJsonBody()
            .stringType("correlationId", "abcd-uuid")
            .stringType("genreName", "Fiction");

    Map<String, Object> metadata = Map.of(
            "Content-Type", "application/json"
    );

    return builder.expectsToReceive("genre created event")
            .withMetadata(metadata)
            .withContent(body)
            .toPact();
  }

  @Test
  @PactTestFor(pactMethod = "genreCreatedPact")
  void testGenreCreated(List<V4Interaction.AsynchronousMessage> messages) {

    String json = messages.get(0).contentsAsString();
    ObjectMapper mapper = new ObjectMapper();

    assertDoesNotThrow(() -> {
      GenreCreatedEvent event = mapper.readValue(json, GenreCreatedEvent.class);
      assertEquals("Fiction", event.getGenreName());
      assertEquals("abcd-uuid", event.getCorrelationId());
    });
  }
}
