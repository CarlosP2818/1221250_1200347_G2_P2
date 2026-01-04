package pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.events;

import pt.psoft.g1.psoftg1.authormanagement.model.dtos.AuthorDto;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorInnerRequest;

import java.util.List;

public record AuthorFoundReply(
        String correlationId,
        List<AuthorInnerRequest> authors
) {}
