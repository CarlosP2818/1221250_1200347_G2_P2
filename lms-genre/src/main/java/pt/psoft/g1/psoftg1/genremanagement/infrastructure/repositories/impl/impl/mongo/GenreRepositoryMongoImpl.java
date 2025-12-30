package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.impl.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.persistence.mongo.GenreMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Profile("mongo")
public class GenreRepositoryMongoImpl implements GenreRepository {

    private final MongoTemplate mongoTemplate;

    private final GenreMongoMapper genreMongoMapper;

    @Override
    public Iterable<Genre> findAll() {
        List<GenreMongo> genres = mongoTemplate.findAll(GenreMongo.class);
        return genres.stream().map(genreMongoMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Genre> findByString(String genreName) {
        Query query = new Query(Criteria.where("genre").is(genreName));
        GenreMongo genre = mongoTemplate.findOne(query, GenreMongo.class);
        return Optional.ofNullable(genre).map(genreMongoMapper::toDomain);    }

    @Override
    public Genre save(Genre genre) {
        GenreMongo genreMongo = genreMongoMapper.toMongo(genre);
        GenreMongo saved = mongoTemplate.save(genreMongo);
        return genreMongoMapper.toDomain(saved);
    }

    @Override
    public void delete(Genre genre) {
        Query query = new Query(Criteria.where("genre").is(genre.getGenre()));
        mongoTemplate.remove(query, GenreMongo.class);
    }
}
