package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import pt.psoft.g1.psoftg1.bookmanagement.services.AuthorInnerRequest;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateBookEvent {
    private List<AuthorInnerRequest> authors;
    private String description;
    private String genreId;
    private String isbn;
    private String title;
    private String version;
    private String photo;
    private String correlationId;
}