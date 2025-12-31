package pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq.events;

import pt.psoft.g1.psoftg1.readermanagement.model.Dto.CreateReaderRequestDto;
import pt.psoft.g1.psoftg1.readermanagement.model.Dto.UserDto;
import pt.psoft.g1.psoftg1.readermanagement.services.CreateReaderRequest;

public record UserFoundReply(
        String correlationId,
        UserDto user
) {}
