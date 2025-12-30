package pt.psoft.g1.psoftg1.shared.infrastructure.repositories.impl.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Embeddable public class NameEmbeddable implements Serializable {

    @Column(name = "NAME", length = 150, nullable = false)
    private String name;

    protected NameEmbeddable() {
        // for JPA
    }

    public NameEmbeddable(String name) {
        this.name = name;
    }

}
