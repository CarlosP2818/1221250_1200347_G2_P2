package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.jpa;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa.ReaderDetailsJpa;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


public interface SpringDataReaderDetailsRepository extends JpaRepository<ReaderDetailsJpa, String> {
    @Query("SELECT r " +
            "FROM ReaderDetailsJpa r " +
            "WHERE r.readerNumber.readerNumber = :readerNumber")
    Optional<ReaderDetailsJpa> findByReaderNumber(@Param("readerNumber") @NotNull String readerNumber);

    @Query("SELECT r " +
            "FROM ReaderDetailsJpa r " +
            "WHERE r.phoneNumber.phoneNumber = :phoneNumber")
    List<ReaderDetailsJpa> findByPhoneNumber(@Param("phoneNumber") @NotNull String phoneNumber);

    @Query("SELECT r " +
            "FROM ReaderDetailsJpa r ")
    Optional<ReaderDetailsJpa> findByUsername(@Param("username") @NotNull String username);

    @Query("SELECT r " +
            "FROM ReaderDetailsJpa r " +
            "WHERE r.reader = :userId")
    Optional<ReaderDetailsJpa> findByUserId(@Param("userId") @NotNull String userId);


    @Query("SELECT COUNT (rd) " +
            "FROM ReaderDetailsJpa rd ")
    int getCountFromCurrentYear();

    @Query("SELECT rd " +
            "FROM ReaderDetailsJpa rd " +
            "GROUP BY rd ")
    Page<ReaderDetails> findTopReaders(Pageable pageable);

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

