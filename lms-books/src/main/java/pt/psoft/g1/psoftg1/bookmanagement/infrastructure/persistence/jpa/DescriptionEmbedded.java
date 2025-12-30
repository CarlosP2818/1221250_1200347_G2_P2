package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.jpa;

import jakarta.annotation.Nullable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import pt.psoft.g1.psoftg1.shared.model.StringUtilsCustom;

import java.io.Serializable;

@Embeddable
@Getter
public class DescriptionEmbedded implements Serializable {
    @Transient
    private final int DESC_MAX_LENGTH = 4096;

    @Size(max = DESC_MAX_LENGTH)
    @Column(length = DESC_MAX_LENGTH)
    String description;

    public DescriptionEmbedded(String description) {
        setDescription(description);
    }

    protected DescriptionEmbedded() {}

    public void setDescription(@Nullable String description) {
        if(description == null || description.isBlank()) {
            this.description = null;
        }else if(description.length() > DESC_MAX_LENGTH) {
            throw new IllegalArgumentException("Description has a maximum of 4096 characters");
        }else{
            this.description = StringUtilsCustom.sanitizeHtml(description);
        }
    }
}
