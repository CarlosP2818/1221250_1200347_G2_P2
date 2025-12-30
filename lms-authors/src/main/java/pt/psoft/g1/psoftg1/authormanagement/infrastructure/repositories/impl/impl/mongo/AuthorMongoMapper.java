package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.impl.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.persistence.mongo.AuthorMongo;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.model.Bio;
import pt.psoft.g1.psoftg1.shared.model.Name;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;

@Component
@Profile("mongo")
public class AuthorMongoMapper {

    private IdGenerator idGenerator;

    @Autowired
    public AuthorMongoMapper(IdGenerator injectedIdGenerator) {
        this.idGenerator = injectedIdGenerator;
    }

    public Author toDomain(AuthorMongo entity) {
        if (entity == null) return null;

        Author author = new Author(
                entity.getName(),
                entity.getBio(),
                entity.getPhoto()
        );
        author.setAuthorNumber(entity.getAuthorNumber());

        return author;
    }

    public AuthorMongo toMongo(Author domain) {
        if (domain == null) return null;

        return new AuthorMongo(
                idGenerator.generateId(),
                domain.getAuthorNumber(),
                0L,
                domain.getName(),
                domain.getBio(),
                domain.getPhoto() != null ? domain.getPhoto().getPhotoFile() : null
        );
    }
}
