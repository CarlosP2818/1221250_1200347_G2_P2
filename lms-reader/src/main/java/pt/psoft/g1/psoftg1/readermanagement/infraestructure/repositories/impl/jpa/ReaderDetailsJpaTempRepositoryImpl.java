package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa.ReaderDetailsJpaTemp;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderDetailsTempRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderBookCountDTO;
import pt.psoft.g1.psoftg1.readermanagement.services.SearchReadersQuery;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@Profile("sql")
@RequiredArgsConstructor
public class ReaderDetailsJpaTempRepositoryImpl implements ReaderDetailsTempRepository {

    private final SpringReaderDetailsJpaTempRepository repo;

    public Optional<ReaderDetailsJpaTemp> findByReaderNumber(String readerNumber) {
        return repo.findByReaderNumber(readerNumber);
    }

    public List<ReaderDetailsJpaTemp> findByPhoneNumber(String phoneNumber) {
        return repo.findByPhoneNumber(phoneNumber);
    }

    public Optional<ReaderDetailsJpaTemp> findByUsername(String username) {
        return repo.findByUsername(username);
    }

    public Optional<ReaderDetailsJpaTemp> findByUserId(String userId) {
        return repo.findByUserId(userId);
    }

    public Optional<ReaderDetailsJpaTemp> findByCorrelationId(String correlationId) {
        return repo.findByCorrelationId(correlationId);
    }

    public int getCountFromCurrentYear() {
        return repo.getCountFromCurrentYear();
    }

    public ReaderDetailsJpaTemp save(ReaderDetailsJpaTemp readerDetails) {
        return repo.save(readerDetails);
    }

    public Iterable<ReaderDetailsJpaTemp> findAll() {
        return repo.findAll();
    }

    public Page<ReaderDetailsJpaTemp> findTopReaders(Pageable pageable) {
        return repo.findTopReaders(pageable);
    }

    public Page<ReaderBookCountDTO> findTopByGenre(
            Pageable pageable,
            String genre,
            LocalDate startDate,
            LocalDate endDate) {

        throw new UnsupportedOperationException(
                "findTopByGenre not implemented for ReaderDetailsJpaTemp");
    }

    public void delete(ReaderDetailsJpaTemp readerDetails) {
        repo.delete(readerDetails);
    }

    public List<ReaderDetailsJpaTemp> searchReaderDetails(
            pt.psoft.g1.psoftg1.shared.services.Page page,
            SearchReadersQuery query) {

        return repo.findAll().stream()
                .skip((long) (page.getNumber() - 1) * page.getLimit())
                .limit(page.getLimit())
                .toList();
    }
}
