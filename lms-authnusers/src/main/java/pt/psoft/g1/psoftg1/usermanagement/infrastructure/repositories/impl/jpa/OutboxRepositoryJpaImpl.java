package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.OutboxEventJpa;
import pt.psoft.g1.psoftg1.usermanagement.repositories.OutboxEventRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class OutboxRepositoryJpaImpl implements OutboxEventRepository {

    private final EntityManager em;
    private final SpringDataOutboxEventRepository repo;

    @Override
    public OutboxEventJpa save(OutboxEventJpa event) {
        return repo.save(event);
    }

    @Override
    public List<OutboxEventJpa> saveAll(List<OutboxEventJpa> events) {
        return repo.saveAll(events);
    }

    @Override
    public Optional<OutboxEventJpa> findById(Long id) {
        return repo.findById(id);
    }

    @Override
    public List<OutboxEventJpa> findAll(Page page) {
        TypedQuery<OutboxEventJpa> query = em.createQuery("SELECT e FROM OutboxEventJpa e ORDER BY e.createdAt DESC", OutboxEventJpa.class);
        query.setFirstResult((page.getNumber() - 1) * page.getLimit());
        query.setMaxResults(page.getLimit());
        return query.getResultList();
    }

    @Override
    public List<OutboxEventJpa> findUnprocessedEvents(Page page) {
        TypedQuery<OutboxEventJpa> query = em.createQuery(
                "SELECT e FROM OutboxEventJpa e WHERE e.processed = false ORDER BY e.createdAt ASC",
                OutboxEventJpa.class
        );
        query.setFirstResult((page.getNumber() - 1) * page.getLimit());
        query.setMaxResults(page.getLimit());
        return query.getResultList();
    }

    @Override
    public void markAsProcessed(Long id) {
        repo.findById(id).ifPresent(event -> {
            event.setProcessed(true);
            repo.save(event);
        });
    }

}
