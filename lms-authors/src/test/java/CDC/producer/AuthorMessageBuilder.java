package CDC.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;

public class AuthorMessageBuilder {

    private ObjectMapper mapper = new ObjectMapper();
    private Author author;

    public AuthorMessageBuilder withAuthor(Author author) {
        this.author = author;
        return this;
    }

    public Message<String> build() throws JsonProcessingException {
        if (author == null) {
            throw new IllegalStateException("Author cannot be null");
        }

        return MessageBuilder.withPayload(this.mapper.writeValueAsString(this.author))
                .setHeader("Content-Type", "application/json; charset=utf-8")
                .build();
    }
}
