package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.impl.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;

import java.util.*;

@Repository
@RequiredArgsConstructor
@Profile("sql")
public class GenreRepositoryJpaImpl implements GenreRepository {
    private final SpringDataGenreRepository jpaRepository;
    private final EntityManager entityManager;
    private final GenreJpaMapper genreJpaMapper;

    @Override
    public Iterable<Genre> findAll() {
        return jpaRepository.findAll().stream().map(genreJpaMapper::toDomain).toList();
    }

    @Override
    public Optional<Genre> findByString(String genreName) {
        return jpaRepository.findByGenre(genreName).map(genreJpaMapper::toDomain);
    }

    @Override
    public Genre save(Genre genre) {
        final var saved = jpaRepository.save(genreJpaMapper.toJpa(genre));
        return genreJpaMapper.toDomain(saved);
    }

    @NotNull
    private List<GenreLendingsPerMonthDTO> getGenreLendingsPerMonthDtos(Map<Integer, Map<Integer, List<GenreLendingsDTO>>> groupedResults) {
        List<GenreLendingsPerMonthDTO> lendingsPerMonth = new ArrayList<>();
        for (Map.Entry<Integer, Map<Integer, List<GenreLendingsDTO>>> yearEntry : groupedResults.entrySet()) {
            int yearValue = yearEntry.getKey();
            for (Map.Entry<Integer, List<GenreLendingsDTO>> monthEntry : yearEntry.getValue().entrySet()) {
                int monthValue = monthEntry.getKey();
                List<GenreLendingsDTO> values = monthEntry.getValue();
                lendingsPerMonth.add(new GenreLendingsPerMonthDTO(yearValue, monthValue, values));
            }
        }

        return lendingsPerMonth;
    }

    @Override
    public void delete(Genre genre) {
        jpaRepository.deleteByGenre(genre.getGenre());
    }
}
