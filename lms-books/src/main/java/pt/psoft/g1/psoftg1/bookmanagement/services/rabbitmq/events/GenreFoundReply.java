package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events;

public record GenreFoundReply(
        String correlationId,
        String genreName
) {}
