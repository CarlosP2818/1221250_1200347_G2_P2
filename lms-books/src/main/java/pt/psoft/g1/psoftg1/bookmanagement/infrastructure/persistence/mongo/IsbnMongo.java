package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo;

import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.io.Serializable;

@EqualsAndHashCode
public class IsbnMongo implements Serializable {

    @Size(min = 10, max = 13)
    @Getter
    String isbn;

    public IsbnMongo(String isbn) {
        this.isbn = isbn;
    }

    protected IsbnMongo() {}


}
