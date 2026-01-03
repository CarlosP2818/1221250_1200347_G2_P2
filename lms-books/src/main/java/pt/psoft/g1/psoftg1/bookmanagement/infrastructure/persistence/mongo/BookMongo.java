package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "book")
@Setter
@Getter
public class BookMongo {

    @Id
    private String pk;

    @Version
    private Long version;

    private String isbn;

    private String title;

    private String genreId;

    private List<String> authorsIds = new ArrayList<>();

    private String description;

    private String photo;

    public BookMongo() {
        // got ORM only
    }

    public BookMongo(String pk, String isbn, String title, String description, String genre, List<String> authors, String photo) {
        this.pk = pk;
        this.isbn = isbn;
        this.title = title;
        if(description != null)
            setDescription(description);
        if(genre==null)
            throw new IllegalArgumentException("Genre cannot be null");
        setGenreId(genre);
        if(authors == null)
            throw new IllegalArgumentException("Author list is null");
        if(authors.isEmpty())
            throw new IllegalArgumentException("Author list is empty");
        setAuthorsIds(authors);
        this.photo = photo;
    }


}
