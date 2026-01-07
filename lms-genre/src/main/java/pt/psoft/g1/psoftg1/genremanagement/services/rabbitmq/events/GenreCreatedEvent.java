package pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreCreatedEvent {
    private String genreName;
    private String correlationId;
}
