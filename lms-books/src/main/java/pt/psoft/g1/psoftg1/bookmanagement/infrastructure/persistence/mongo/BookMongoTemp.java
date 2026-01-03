package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "temp_book")
@Setter
@Getter
public class BookMongoTemp {

    @Id
    private String pk;

    @Version
    private Long version;

    private String isbn;

    private String title;

    private Long genreId;

    private List<Long> authorsIds = new ArrayList<>();

    private String description;

    private String photo;

}
