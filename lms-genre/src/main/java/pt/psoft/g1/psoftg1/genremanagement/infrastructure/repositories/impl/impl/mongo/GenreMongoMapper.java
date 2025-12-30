package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.impl.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.persistence.mongo.GenreMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;

@Component
@Profile("mongo")
public class GenreMongoMapper {

    private final IdGenerator idGenerator;

    @Autowired
    public GenreMongoMapper(IdGenerator injectedIdGenerator) {
        this.idGenerator = injectedIdGenerator;
    }

    public Genre toDomain(GenreMongo jpa) {
        if (jpa == null) return null;
        return new Genre(jpa.getPk(), jpa.getGenre());
    }

    public GenreMongo toMongo(Genre genre) {
        if (genre == null) return null;
        return new GenreMongo(idGenerator.generateId(), genre.getGenre());
    }
}
