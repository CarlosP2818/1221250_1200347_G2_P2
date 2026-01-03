package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events;

import java.util.List;

public record AuthorFoundReply(
        String correlationId,
        List<String> authorId
) {}
