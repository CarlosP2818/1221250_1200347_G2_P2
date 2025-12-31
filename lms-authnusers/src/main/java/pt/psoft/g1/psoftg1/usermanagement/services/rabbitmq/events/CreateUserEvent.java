package pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.CreateReaderRequestDto;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserEvent {
    private String username;
    private String password;
    private String fullName;
    private String correlationId;
}

