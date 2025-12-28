package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.jpa.GenreJpaMapper;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa.ReaderDetailsJpa;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa.ReaderJpaMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@Profile("sql")
public class ReaderDetailsJpaMapper {
    private SpringDataReaderDetailsRepository repo;
    private IdGenerator idGenerator;
    private GenreJpaMapper genreJpaMapper;
    private ReaderJpaMapper readerJpaMapper;

    @Autowired
    public ReaderDetailsJpaMapper(SpringDataReaderDetailsRepository injectedRepo, IdGenerator injectedIdGenerator, GenreJpaMapper injectedGenreJpaMapper, ReaderJpaMapper injectedReaderJpaMapper) {
        this.idGenerator = injectedIdGenerator;
        this.repo = injectedRepo;
        this.genreJpaMapper = injectedGenreJpaMapper;
        this.readerJpaMapper = injectedReaderJpaMapper;
    }

    public ReaderDetails toDomain(ReaderDetailsJpa jpa) {
        if (jpa == null) return null;

        return new ReaderDetails(
                Integer.parseInt(jpa.getReaderNumber().getReaderNumber().split("/")[1]),
                new Reader(jpa.getReader().getUsername(),jpa.getReader().getPassword()),
                new BirthDate(jpa.getBirthDate().getBirthDate().toString()),
                new PhoneNumber(jpa.getPhoneNumber().getPhoneNumber()),
                jpa.isGdprConsent(),
                jpa.isMarketingConsent(),
                jpa.isThirdPartySharingConsent(),
                jpa.getPhoto() != null ? jpa.getPhoto().getPhotoFile() : null,
                jpa.getInterestList().stream().map(genreJpaMapper::toDomain).toList()

        );
    }

    public ReaderDetailsJpa toJpa(ReaderDetails readerDetails) {

        // Preferir devolver um BookJpa já existente (mesmo que ID técnico seja diferente)
        if (repo != null) {
            Optional<ReaderDetailsJpa> existing = repo.findByReaderNumber(readerDetails.getReaderNumber());
            if (existing.isPresent()) {
                return existing.get();
            }
        }


        assert readerDetails.getPhoto() != null;
        return new ReaderDetailsJpa(
                idGenerator.generateId(),
                Integer.parseInt(readerDetails.getReaderNumber().split("/")[1]),
                readerJpaMapper.toJpa(readerDetails.getReader()),
                readerDetails.getBirthDate().getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                readerDetails.getPhoneNumber(),
                readerDetails.isGdprConsent(),
                readerDetails.isMarketingConsent(),
                readerDetails.isThirdPartySharingConsent(),
                readerDetails.getPhoto() != null ? readerDetails.getPhoto().getPhotoFile() : null,
                readerDetails.getInterestList().stream().map(genreJpaMapper::toJpa).toList()
        );
    }
}
