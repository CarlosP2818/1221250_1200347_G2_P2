package pt.psoft.g1.psoftg1.bookmanagement.api;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Document(collection = "book_temp")
public class TempBook {

    @Id
    private String id; // Mongo usa String ou ObjectId

    @Getter @Setter
    private String isbn;

    @Getter @Setter @NotNull
    private String title;

    @Getter @Setter @NotNull
    private Long genreId;

    @Getter @Setter
    private List<Long> authorsIds = new ArrayList<>();

    @Getter @Setter
    private String description;

    @Getter @Setter
    private UUID sagaId;

    @Getter @Setter
    private Long version;

    @Getter @Setter
    private boolean processed = false;
}
