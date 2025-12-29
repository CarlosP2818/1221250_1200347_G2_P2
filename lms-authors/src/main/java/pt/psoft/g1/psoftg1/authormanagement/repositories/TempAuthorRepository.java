package pt.psoft.g1.psoftg1.authormanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.psoft.g1.psoftg1.authormanagement.api.TempAuthor;

import java.util.UUID;
import java.util.Optional;

public interface TempAuthorRepository extends JpaRepository<TempAuthor, Long> {
    Optional<TempAuthor> findBySagaId(UUID sagaId);
}