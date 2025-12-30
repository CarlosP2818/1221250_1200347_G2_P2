package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.psoft.g1.psoftg1.bookmanagement.api.TempBook;

import java.util.Optional;
import java.util.UUID;

public interface TempBookRepository extends JpaRepository<TempBook, Long> {
    Optional<TempBook> findBySagaId(UUID sagaId);
}
