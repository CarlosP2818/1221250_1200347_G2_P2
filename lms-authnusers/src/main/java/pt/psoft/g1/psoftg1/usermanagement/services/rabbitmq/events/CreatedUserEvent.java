package pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.CreateReaderRequestDto;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.UserDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatedUserEvent {
    private UserDto user;
    private CreateReaderRequestDto createReaderRequest;
}
