package pt.psoft.g1.psoftg1.usermanagement.repositories;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import java.util.List;
import java.util.Optional;

public class UserRepositoryNoSQL implements UserRepository {

    private final MongoTemplate mongoTemplate;

    public UserRepositoryNoSQL(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        entities.forEach(mongoTemplate::save);
        return (List<S>) entities;
    }

    @Override
    public <S extends User> S save(S entity) {
        mongoTemplate.save(entity);
        return entity;
    }

    @Override
    public Optional<User> findById(Long objectId) {
        return Optional.ofNullable(mongoTemplate.findById(objectId, User.class));
    }

    @Override
    public User getById(Long id) {
        return UserRepository.super.getById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        Query query = new Query(Criteria.where("username").is(username));
        return Optional.ofNullable(mongoTemplate.findOne(query, User.class));
    }

    @Override
    public List<User> searchUsers(Page page, SearchUsersQuery query) {
        return List.of();
    }

    @Override
    public List<User> findByNameName(String name) {
        Query query = new Query(Criteria.where("name.name").is(name));
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public List<User> findByNameNameContains(String name) {
        Query query = new Query(Criteria.where("name.name").regex(".*" + name + ".*", "i"));
        return mongoTemplate.find(query, User.class);
    }

    @Override
    public void delete(User user) {
        mongoTemplate.remove(user);
    }
}
