package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.persistence.jpa.GenreJpa;

import java.util.Optional;

public interface SpringDataGenreRepository extends JpaRepository<GenreJpa, String> {
    Optional<GenreJpa> findByGenre(String genre);
    void deleteByGenre(String genre);
}
