package pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.events;

public record AuthorFoundReply(
        String correlationId,
        String authorId
) {}
