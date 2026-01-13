package pt.psoft.g1.psoftg1.genremanagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class GenreTempCreatedEvent {
    private String genreName;
}
