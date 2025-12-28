package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.LibrarianJpa;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.ReaderJpa;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.UserJpa;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.SearchUsersQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
@Profile("sql")
class UserRepositoryJpaImpl implements UserRepository {

    // get the underlying JPA Entity Manager via spring thru constructor dependency
    // injection
    private final EntityManager em;
    private final ReaderJpaMapper readerJpaMapper;
    private final LibrarianJpaMapper librarianJpaMapper;
    private final UserJpaMapper userJpaMapper;
    private final SpringDataUserRepository repo;

    @Override
    public <S extends User> S save(S entity) {
        if (entity instanceof Reader r) {
            ReaderJpa jpa = readerJpaMapper.toJpa(r);
            ReaderJpa saved = repo.save(jpa);
            return (S) readerJpaMapper.toDomain(saved);
        } else if (entity instanceof Librarian l) {
            LibrarianJpa jpa = librarianJpaMapper.toJpa(l);
            LibrarianJpa saved = repo.save(jpa);
            return (S) librarianJpaMapper.toDomain(saved);
        } else {
            UserJpa jpa = userJpaMapper.toJpa(entity);
            UserJpa saved = repo.save(jpa);
            return (S) userJpaMapper.toDomain(saved);
        }
    }

    @Override
    public <S extends User> List<S> saveAll(Iterable<S> entities) {
        List<UserJpa> jpas = new ArrayList<>();
        entities.forEach(u -> jpas.add(userJpaMapper.toJpa(u)));

        List<UserJpa> saved = repo.saveAll(jpas);
        return (List<S>) saved.stream()
                .map(userJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<User> findById(Long objectId) {
        return repo.findById(objectId)
                .map(userJpaMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return repo.findByUsername(username)
                .map(userJpaMapper::toDomain);
    }

    @Override
    public List<User> findByNameName(String name) {
        return repo.findAll().stream()
                .filter(u -> u.getName() != null && u.getName().getName().equalsIgnoreCase(name))
                .map(userJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> findByNameNameContains(String name) {
        return repo.findAll().stream()
                .filter(u -> u.getName() != null && u.getName().getName().toLowerCase().contains(name.toLowerCase()))
                .map(userJpaMapper::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(User user) {
        repo.findByUsername(user.getUsername())
                .ifPresent(repo::delete);
    }
    @Override
    public List<User> searchUsers(final Page page, final SearchUsersQuery query) {

        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<User> cq = cb.createQuery(User.class);
        final Root<User> root = cq.from(User.class);
        cq.select(root);

        final List<Predicate> where = new ArrayList<>();
        if (StringUtils.hasText(query.getUsername())) {
            where.add(cb.equal(root.get("username"), query.getUsername()));
        }
        if (StringUtils.hasText(query.getFullName())) {
            where.add(cb.like(root.get("fullName"), "%" + query.getFullName() + "%"));
        }

        // search using OR
        if (!where.isEmpty()) {
            cq.where(cb.or(where.toArray(new Predicate[0])));
        }

        cq.orderBy(cb.desc(root.get("createdAt")));

        final TypedQuery<User> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList();
    }
}