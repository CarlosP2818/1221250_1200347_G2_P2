package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events;

import pt.psoft.g1.psoftg1.bookmanagement.services.AuthorInnerRequest;

import java.util.List;

public record AuthorFoundReply(
        String correlationId,
        List<AuthorInnerRequest> authors
) {}
