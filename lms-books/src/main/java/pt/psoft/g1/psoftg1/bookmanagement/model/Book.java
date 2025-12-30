package pt.psoft.g1.psoftg1.bookmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "Book", uniqueConstraints = { @UniqueConstraint(name = "uc_book_isbn", columnNames = { "ISBN" }) })
public class Book extends EntityWithPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    long pk;

    @Version
    @Getter
    private Long version;

    @Embedded
    Isbn isbn;

    @Getter
    @Embedded
    @NotNull
    Title title;

    @Getter
    @NotNull
    Long genreId;

    @Getter
    private List<Long> authorsIds = new ArrayList<>();

    @Embedded
    Description description;

    @Setter
    @Column(unique = true)
    @Getter
    private UUID sagaId;

    // ID da saga TEMP
    @Enumerated(EnumType.STRING)
    @Getter
    private BookStatus status = BookStatus.STARTED;


    public void setTitle(Title title) {
        this.title = title;
    }

    public void setIsbn(Isbn isbn) {
        this.isbn = isbn;
    }

    public void setDescription(Description description) {
        this.description = description;
    }

    public void setGenreId(Long genre) {
        this.genreId = genre;
    }

    public void setAuthorsIds(List<Long> authors) {
        this.authorsIds = authors;
    }

    public String getDescription() {
        return this.description.toString();
    }

    public Book(Isbn isbn, Title title, Description description, Long genreId, List<Long> authorsIds, String photoURI) {
        if (isbn == null) throw new IllegalArgumentException("ISBN cannot be null");
        if (title == null) throw new IllegalArgumentException("Title cannot be null");
        if (genreId == null) throw new IllegalArgumentException("Genre cannot be null");
        if (authorsIds == null) throw new IllegalArgumentException("Authors list cannot be null");
        if (authorsIds.isEmpty()) throw new IllegalArgumentException("Authors list cannot be empty");

        setIsbn(isbn);
        setTitle(title);
        setDescription(description);
        setGenreId(genreId);
        setAuthorsIds(authorsIds);
        setPhotoInternal(photoURI);
    }

    protected Book() {
        // got ORM only
    }

    public void removePhoto(long desiredVersion) {
        if (desiredVersion != this.version) {
            throw new ConflictException("Provided version does not match latest version of this object");
        }

        setPhotoInternal(null);
    }

    public void applyPatch(final Long desiredVersion,
                           final String title,
                           final String description,
                           final String photoURI,
                           final Long genre,
                           final List<Long> authors ) {

        if (!Objects.equals(this.version, desiredVersion))
            throw new StaleObjectStateException("Object was already modified by another user", this.pk);

        if (title != null) {
            setTitle(new Title(title));
        }

        if (description != null) {
            setDescription(new Description(description));
        }

        if (genre != null) {
            setGenreId(genre);
        }

        if (authors != null) {
            setAuthorsIds(authors);
        }

        if (photoURI != null)
            setPhotoInternal(photoURI);

    }

    public String getIsbn() {
        return this.isbn.toString();
    }
}
