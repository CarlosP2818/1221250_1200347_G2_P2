package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.shared.model.StringUtilsCustom;

@Document(collection = "author")
public class BioMongo {
    private static final int BIO_MAX_LENGTH = 4096;

    @NotNull
    @Size(min = 1, max = BIO_MAX_LENGTH)
    private String bio;

    public BioMongo(String bio) {
        setBioMongo(bio);
    }

    protected BioMongo() {
    }

    public void setBioMongo(String bio) {
        if(bio == null)
            throw new IllegalArgumentException("Bio cannot be null");
        if(bio.isBlank())
            throw new IllegalArgumentException("Bio cannot be blank");
        if(bio.length() > BIO_MAX_LENGTH)
            throw new IllegalArgumentException("Bio has a maximum of 4096 characters");
        this.bio = StringUtilsCustom.sanitizeHtml(bio);
    }

    @Override
    public String toString() {
        return bio;
    }
}
