package pt.psoft.g1.psoftg1.bookmanagement.api;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "book_temp")
public class TempBook {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long pk;

    @Version
    @Getter
    @Setter
    private Long version;

    @Embedded
    @Getter
    @Setter
    Isbn isbn;

    @Getter
    @Embedded
    @NotNull
    @Setter
    Title title;

    @Getter
    @NotNull
    @Setter
    private Long genreId;

    @Getter
    @Setter
    private List<Long> authorsIds = new ArrayList<>();

    @Embedded
    @Getter
    @Setter
    Description description;

    @Getter
    @Setter
    private UUID sagaId;

    @Getter
    @Setter
    private boolean processed = false;
}
