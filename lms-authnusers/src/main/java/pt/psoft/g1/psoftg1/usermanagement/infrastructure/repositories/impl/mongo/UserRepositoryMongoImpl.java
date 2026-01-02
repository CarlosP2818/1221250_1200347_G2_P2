package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.mongo.UserMongo;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
@Profile("mongo")
public class UserRepositoryMongoImpl implements UserRepository {

    private final MongoTemplate mongoTemplate;

    private final UserMongoMapper userMongoMapper;

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        return ((List<S>) entities).stream()
                .map(this::save)
                .collect(Collectors.toList());
    }

    @Override
    public <S extends User> S save(S entity) {
        UserMongo mongo = userMongoMapper.toMongo(entity);
        UserMongo saved = mongoTemplate.save(mongo);
        return (S) userMongoMapper.toDomain(saved);
    }

    @Override
    public Optional<User> findById(Long objectId) {
        Query query = new Query(Criteria.where("_id").is(String.valueOf(objectId)));
        UserMongo userMongo = mongoTemplate.findOne(query, UserMongo.class);
        return Optional.ofNullable(userMongoMapper.toDomain(userMongo));
    }

    @Override
    public Optional<User> findById(String objectId) {
        return Optional.empty();
    }

    @Override
    public User getById(Long id) {
        return findById(id).orElse(null);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        UserMongo userMongo = mongoTemplate.findOne(query, UserMongo.class);
        return Optional.ofNullable(userMongoMapper.toDomain(userMongo));
    }

    @Override
    public List<User> searchUsers(Page page, SearchUsersQuery query) {
        Query mongoQuery = new Query();

        if (query.getUsername() != null && !query.getUsername().isEmpty()) {
            mongoQuery.addCriteria(Criteria.where("username").regex(".*" + query.getUsername() + ".*", "i"));
        }

        mongoQuery.skip(page.getNumber());
        mongoQuery.limit(page.getLimit());

        List<UserMongo> results = mongoTemplate.find(mongoQuery, UserMongo.class);
        return results.stream().map(userMongoMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<User> findByNameName(String name) {
        Query query = new Query(Criteria.where("name").is(name));
        List<UserMongo> results = mongoTemplate.find(query, UserMongo.class);
        return results.stream().map(userMongoMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public List<User> findByNameNameContains(String name) {
        Query query = new Query(Criteria.where("name").regex(".*" + name + ".*", "i"));
        List<UserMongo> results = mongoTemplate.find(query, UserMongo.class);
        return results.stream().map(userMongoMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public void delete(User user) {
        Query query = new Query(Criteria.where("_id").is(user.getName()));
        mongoTemplate.remove(query, UserMongo.class);
    }
}
