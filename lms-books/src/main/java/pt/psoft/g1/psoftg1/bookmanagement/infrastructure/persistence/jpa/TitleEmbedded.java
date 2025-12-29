package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.jpa;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.io.Serializable;

@Embeddable
public class TitleEmbedded implements Serializable {

    private static final int TITLE_MAX_LENGTH = 128;

    @NotBlank(message = "Title cannot be blank")
    @Size(min = 1, max = TITLE_MAX_LENGTH)
    @Getter
    private String title;

    protected TitleEmbedded() {
        // Construtor padrão necessário para JPA
    }

    public TitleEmbedded(String title) {
        setTitle(title);
    }

    public void setTitle(String title) {
        if(title == null)
            throw new IllegalArgumentException("Title cannot be null");
        if(title.isBlank())
            throw new IllegalArgumentException("Title cannot be blank");
        if(title.length() > TITLE_MAX_LENGTH)
            throw new IllegalArgumentException("Title has a maximum of " + TITLE_MAX_LENGTH + " characters");
        this.title = title.strip();
    }

    @Override
    public String toString() {
        return this.title;
    }
}
