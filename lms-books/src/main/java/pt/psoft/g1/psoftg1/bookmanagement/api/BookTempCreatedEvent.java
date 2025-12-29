package pt.psoft.g1.psoftg1.bookmanagement.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class BookTempCreatedEvent {
    private UUID sagaId;
    private Long version;
    private Isbn isbn;
    private Title title;
    private Long genreId;
    private List<Long> authorsIds;
    private String description;
}
