package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events;

import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;

public record GenreFoundReply(
        String correlationId,
        String genreId
) {}
