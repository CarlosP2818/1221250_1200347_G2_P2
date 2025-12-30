package pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events;

import pt.psoft.g1.psoftg1.usermanagement.model.dto.CreateReaderRequestDto;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.UserDto;

public record UserFoundReply(
        String correlationId,
        UserDto user,
        CreateReaderRequestDto createReaderRequestDto
) {}
