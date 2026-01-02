package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.jpa;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa.ReaderDetailsJpaTemp;

import java.util.List;
import java.util.Optional;

public interface SpringReaderDetailsJpaTempRepository extends JpaRepository<ReaderDetailsJpaTemp, String> {
    @Query("SELECT r " +
            "FROM ReaderDetailsJpaTemp r " +
            "WHERE r.readerNumber.readerNumber = :readerNumber")
    Optional<ReaderDetailsJpaTemp> findByReaderNumber(@Param("readerNumber") @NotNull String readerNumber);

    @Query("SELECT r " +
            "FROM ReaderDetailsJpaTemp r " +
            "WHERE r.phoneNumber.phoneNumber = :phoneNumber")
    List<ReaderDetailsJpaTemp> findByPhoneNumber(@Param("phoneNumber") @NotNull String phoneNumber);

    @Query("SELECT r " +
            "FROM ReaderDetailsJpaTemp r")
    Optional<ReaderDetailsJpaTemp> findByUsername(@Param("username") @NotNull String username);

    @Query("SELECT r " +
            "FROM ReaderDetailsJpaTemp r " +
            "WHERE r.reader = :userId")
    Optional<ReaderDetailsJpaTemp> findByUserId(@Param("userId") @NotNull String userId);

    @Query("SELECT r " +
            "FROM ReaderDetailsJpaTemp r " +
            "WHERE r.correlationId = :correlationId")
    Optional<ReaderDetailsJpaTemp> findByCorrelationId(@Param("correlationId") @NotNull String correlationId);


    @Query("SELECT COUNT (rd) " +
            "FROM ReaderDetailsJpaTemp rd ")
    int getCountFromCurrentYear();

    @Query("SELECT rd " +
            "FROM ReaderDetailsJpaTemp rd " +
            "GROUP BY rd ")
    Page<ReaderDetailsJpaTemp> findTopReaders(Pageable pageable);

//    @Query("SELECT NEW pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO(rd, count(l)) " +
//            "FROM ReaderDetailsJpa rd " +
//            "JOIN LendingJpa l ON l.readerDetails.pk = rd.pk " +
//            "JOIN BookJpa b ON b.pk = l.book.pk " +
//            "JOIN GenreJpa g ON g.pk = b.genre.pk " +
//            "WHERE g.genre = :genre " +
//            "AND l.startDate >= :startDate " +
//            "AND l.startDate <= :endDate " +
//            "GROUP BY rd.pk " +
//            "ORDER BY COUNT(l.pk) DESC")
//    Page<ReaderBookCountDTO> findTopByGenre(Pageable pageable, String genre, LocalDate startDate, LocalDate endDate);
}