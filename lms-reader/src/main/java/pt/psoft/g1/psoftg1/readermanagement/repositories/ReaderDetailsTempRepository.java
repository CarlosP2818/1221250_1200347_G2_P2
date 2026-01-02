package pt.psoft.g1.psoftg1.readermanagement.repositories;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa.ReaderDetailsJpaTemp;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Infrastructure repository for ReaderDetails temporary JPA projections
 */
public interface ReaderDetailsTempRepository {

    Optional<ReaderDetailsJpaTemp> findByReaderNumber(@NotNull String readerNumber);

    List<ReaderDetailsJpaTemp> findByPhoneNumber(@NotNull String phoneNumber);

    Optional<ReaderDetailsJpaTemp> findByUsername(@NotNull String username);

    Optional<ReaderDetailsJpaTemp> findByUserId(@NotNull String userId);

    Optional<ReaderDetailsJpaTemp> findByCorrelationId(@NotNull String correlationId);

    int getCountFromCurrentYear();

    ReaderDetailsJpaTemp save(ReaderDetailsJpaTemp readerDetails);

    Iterable<ReaderDetailsJpaTemp> findAll();

    Page<ReaderDetailsJpaTemp> findTopReaders(Pageable pageable);

    Page<ReaderBookCountDTO> findTopByGenre(
            Pageable pageable,
            String genre,
            LocalDate startDate,
            LocalDate endDate
    );

    void delete(ReaderDetailsJpaTemp readerDetails);

    List<ReaderDetailsJpaTemp> searchReaderDetails(
            pt.psoft.g1.psoftg1.shared.services.Page page,
            SearchReadersQuery query
    );
}

