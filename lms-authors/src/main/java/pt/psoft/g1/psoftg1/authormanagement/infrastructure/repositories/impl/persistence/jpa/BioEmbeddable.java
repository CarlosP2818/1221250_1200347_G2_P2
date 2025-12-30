package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class BioEmbeddable implements Serializable {

    @Column(length = 4096, nullable = false)
    private String bio;

    protected BioEmbeddable() {
        // for JPA
    }

    public BioEmbeddable(String bio) {
        this.bio = bio;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }
}