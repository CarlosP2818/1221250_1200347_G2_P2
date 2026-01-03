package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookEvent {
    private List<String> authorsId;
    private String description;
    private String genreId;
    private String isbn;
    private String title;
    private String version;
    private String photo;
    private String correlationId;
}