package pt.psoft.g1.psoftg1.genremanagement.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.psoft.g1.psoftg1.genremanagement.api.TempGenre;

import java.util.Optional;
import java.util.UUID;

public interface TempGenreRepository extends JpaRepository<TempGenre, Long> {
    Optional<TempGenre> findBySagaId(UUID sagaId);
}
