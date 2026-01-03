package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookCreationFailedEvent {
    private String username;
    private String reason;
    private String correlationId;
}
