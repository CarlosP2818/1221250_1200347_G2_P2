package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.impl.jpa;

import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.persistence.jpa.AuthorJpa;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.persistence.jpa.BioEmbeddable;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.shared.infrastructure.persistence.jpa.NameEmbeddable;

import java.util.Optional;

@Component
public class AuthorJpaMapper {

    private SpringDataAuthorRepository repo;

    public AuthorJpaMapper(SpringDataAuthorRepository repo) {
        this.repo = repo;
    }

    public Author toDomain(AuthorJpa jpa) {
        String photoUri = jpa.getPhoto() != null ? jpa.getPhoto().getPhotoFile() : null;
        String authorName = jpa.getName() != null ? jpa.getName().getName() : null;
        String authorBio = jpa.getBio() != null ? jpa.getBio().getBio() : null;

        Author domain = new Author(authorName, authorBio, photoUri);
        domain.setAuthorNumber(jpa.getAuthorNumber());
        return domain;
    }

    public AuthorJpa toJpa(Author domain) {
        // Evita duplicar se já existir
        Optional<AuthorJpa> existing = repo.findByAuthorNumber(domain.getAuthorNumber());
        if (existing.isPresent()) {
            return existing.get();
        }

        var name = new NameEmbeddable(domain.getName());
        var bio = new BioEmbeddable(domain.getBio());
        return new AuthorJpa(domain.getId(), name, bio, domain.getPhoto());
    }
}
