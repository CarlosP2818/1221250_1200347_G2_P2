package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.jpa;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Book", uniqueConstraints = {
        @UniqueConstraint(name = "uc_book_isbn", columnNames = {"ISBN"})
})
@Setter
@Getter
public class BookJpa implements Serializable {
    @Id
    String pk;

    @Version
    @Getter
    private Long version;

    @Embedded
    IsbnEmbedded isbn;

    @Getter
    @Embedded
    @NotNull
    TitleEmbedded title;

    @Getter
    @NotNull
    Long genreId;

    @Getter
    private List<Long> authorsIds = new ArrayList<>();

    @Embedded
    DescriptionEmbedded description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id")
    @Getter
    private Photo photo;

    public BookJpa(String pk,String isbn, String title, String description, Long genreId, List<Long> authorsIds, Photo photoURI) {
        this.pk = pk;
        setTitle(new TitleEmbedded(title));
        setIsbn(new IsbnEmbedded(isbn));
        if(description != null)
            setDescription(new DescriptionEmbedded(description));
        setGenreId(genreId);
        setAuthorsIds(authorsIds);
        setPhoto(photoURI);
    }

    protected BookJpa() {
        // got ORM only
    }
}
