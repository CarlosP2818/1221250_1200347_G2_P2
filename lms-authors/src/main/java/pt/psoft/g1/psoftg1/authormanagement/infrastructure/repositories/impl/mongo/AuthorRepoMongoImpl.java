package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.AuthorMongo;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Profile("mongo")
public class AuthorRepoMongoImpl implements AuthorRepository {

    private final MongoTemplate mongoTemplate;

    private final AuthorMongoMapper authorMongoMapper;

    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {
        Query query = new Query(Criteria.where("authorNumber").is(authorNumber));
        AuthorMongo author = mongoTemplate.findOne(query, AuthorMongo.class);
        return Optional.ofNullable(author).map(authorMongoMapper::toDomain);
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        Query query = new Query(Criteria.where("name").regex("^" + name, "i"));
        List<AuthorMongo> authors = mongoTemplate.find(query, AuthorMongo.class);
        return authors.stream()
                .map(authorMongoMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Author> searchByNameName(String name) {
        Query query = new Query(Criteria.where("name").regex(name, "i"));
        List<AuthorMongo> authors = mongoTemplate.find(query, AuthorMongo.class);
        return authors.stream().map(authorMongoMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Author save(Author author) {
        AuthorMongo authorMongo = authorMongoMapper.toMongo(author);
        AuthorMongo saved = mongoTemplate.save(authorMongo);
        return authorMongoMapper.toDomain(saved);
    }

    @Override
    public Iterable<Author> findAll() {
        List<AuthorMongo> authors = mongoTemplate.findAll(AuthorMongo.class);
        return authors.stream().map(authorMongoMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void delete(Author author) {
        Query query = new Query(Criteria.where("authorNumber").is(author.getAuthorNumber()));
        mongoTemplate.remove(query, AuthorMongo.class);
    }

}
