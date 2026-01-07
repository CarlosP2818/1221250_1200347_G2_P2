package pt.psoft.g1.psoftg1.bookmanagement.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.StaleObjectStateException;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.model.EntityWithPhoto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Book extends EntityWithPhoto implements Serializable {

    @Version
    @Getter
    private Long version;

    @Setter
    Isbn isbn;

    @Getter
    @Setter
    Title title;

    @Getter
    @Setter
    String genreName;

    @Getter
    @Setter
    private List<String> authorsIds = new ArrayList<>();

    @Setter
    Description description;

    @Setter
    @Column(unique = true)
    @Getter
    private UUID sagaId;

    // ID da saga TEMP
    @Enumerated(EnumType.STRING)
    @Getter
    private BookStatus status = BookStatus.STARTED;

    public String getDescription() {
        return this.description.toString();
    }

    public Book(Isbn isbn, Title title, Description description, String genreName, List<String> authorsIds, String photoURI) {
        if (isbn == null) throw new IllegalArgumentException("ISBN cannot be null");
        if (title == null) throw new IllegalArgumentException("Title cannot be null");
        if (genreName == null) throw new IllegalArgumentException("Genre cannot be null");
        if (authorsIds == null) throw new IllegalArgumentException("Authors list cannot be null");
        if (authorsIds.isEmpty()) throw new IllegalArgumentException("Authors list cannot be empty");

        setIsbn(isbn);
        setTitle(title);
        setDescription(description);
        setGenreName(genreName);
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
                           final String genre,
                           final List<String> authors ) {

        if (!Objects.equals(this.version, desiredVersion))
            throw new StaleObjectStateException(Book.class.getName(), this.isbn);

        if (title != null) {
            setTitle(new Title(title));
        }

        if (description != null) {
            setDescription(new Description(description));
        }

        if (genre != null) {
            setGenreName(genre);
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
