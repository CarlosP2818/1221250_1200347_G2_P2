package pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq.events;

public record GenreFoundReply(
        String correlationId,
        String genreName
) {}
