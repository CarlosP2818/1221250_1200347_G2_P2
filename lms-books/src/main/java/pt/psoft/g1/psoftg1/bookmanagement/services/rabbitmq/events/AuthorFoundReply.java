package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events;

import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;

public record AuthorFoundReply(
        String correlationId,
        String authorId
) {}
