package pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq.events;

import lombok.*;
import pt.psoft.g1.psoftg1.readermanagement.services.CreateReaderRequest;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserEvent {
    private String username;
    private String password;
    private String fullName;
    private String correlationId;
    private CreateReaderRequest createReaderRequest;
}

