package pt.psoft.g1.psoftg1.authormanagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthorTempCreatedEvent {
    private UUID sagaId;
    private String name;
    private String bio;
}
