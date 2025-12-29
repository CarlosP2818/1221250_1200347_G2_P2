package pt.psoft.g1.psoftg1.genremanagement.api;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "genre_temp")
public class TempGenre {

    @Transient
    private final int GENRE_MAX_LENGTH = 100;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Size(min = 1, max = GENRE_MAX_LENGTH)
    @Column(nullable = false, length = GENRE_MAX_LENGTH)
    @Getter @Setter
    private String genre;

    @Column(nullable = false, updatable = false)
    @Getter @Setter
    private UUID sagaId;

    @Getter @Setter
    private boolean processed = false;
}
