package pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReaderCreatedEvent {
    private String username;
    private String fullName;
}
