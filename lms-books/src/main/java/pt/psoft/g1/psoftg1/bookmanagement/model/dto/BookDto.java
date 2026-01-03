package pt.psoft.g1.psoftg1.bookmanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pt.psoft.g1.psoftg1.bookmanagement.model.BookStatus;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {

    private String isbn;

    private String title;

    private String description;

    private String genreId;

    private List<String> authorsIds;

    private String photoURI;

    private Long version;

    private UUID sagaId;

    private BookStatus status;
}
