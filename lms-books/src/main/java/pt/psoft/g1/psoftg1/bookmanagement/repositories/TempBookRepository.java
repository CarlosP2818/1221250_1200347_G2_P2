package pt.psoft.g1.psoftg1.bookmanagement.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import pt.psoft.g1.psoftg1.bookmanagement.api.TempBook;

import java.util.Optional;
import java.util.UUID;

public interface TempBookRepository extends MongoRepository<TempBook, Long> {
    Optional<TempBook> findBySagaId(UUID sagaId);
}
