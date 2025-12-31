package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.shared.infrastructure.persistence.jpa.NameEmbeddable;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "Author")
public class AuthorJpa implements Serializable {


    @Id
    @Column(name = "AUTHOR_NUMBER")
    private String authorNumber;

    @Version
    private Long version;

    @Embedded
    private NameEmbeddable name;

    @Embedded
    private BioEmbeddable bio;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "photo_id")
    private Photo photo;

    protected AuthorJpa() {
        // for JPA
    }

    public AuthorJpa(String id, NameEmbeddable name, BioEmbeddable bio, Photo photo) {
        this.authorNumber = id;
        this.name = name;
        this.bio = bio;
        this.photo = photo;
    }

}