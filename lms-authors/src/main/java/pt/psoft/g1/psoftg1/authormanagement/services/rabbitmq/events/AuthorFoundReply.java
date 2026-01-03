package pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.events;

import pt.psoft.g1.psoftg1.readermanagement.model.Dto.UserDto;

public record AuthorFoundReply(
        String correlationId,
        UserDto user
) {}
