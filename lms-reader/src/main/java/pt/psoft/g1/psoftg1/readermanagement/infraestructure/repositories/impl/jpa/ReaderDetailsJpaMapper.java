package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.jpa;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa.ReaderDetailsJpa;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;

import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Component
@Profile("sql")
public class ReaderDetailsJpaMapper {
    private final SpringDataReaderDetailsRepository repo;
    private final IdGenerator idGenerator;

    @Autowired
    public ReaderDetailsJpaMapper(SpringDataReaderDetailsRepository injectedRepo, IdGenerator injectedIdGenerator) {
        this.idGenerator = injectedIdGenerator;
        this.repo = injectedRepo;
    }

    public ReaderDetails toDomain(ReaderDetailsJpa jpa) {
        if (jpa == null) return null;

        return new ReaderDetails(
                Integer.parseInt(jpa.getReaderNumber().getReaderNumber().split("/")[1]),
                jpa.getReader(),
                new BirthDate(jpa.getBirthDate().getBirthDate().toString()),
                new PhoneNumber(jpa.getPhoneNumber().getPhoneNumber()),
                jpa.isGdprConsent(),
                jpa.isMarketingConsent(),
                jpa.isThirdPartySharingConsent(),
                jpa.getPhoto() != null ? jpa.getPhoto().getPhotoFile() : null,
                jpa.getInterestList()
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
                readerDetails.getReader(),
                readerDetails.getBirthDate().getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                readerDetails.getPhoneNumber(),
                readerDetails.isGdprConsent(),
                readerDetails.isMarketingConsent(),
                readerDetails.isThirdPartySharingConsent(),
                readerDetails.getPhoto() != null ? readerDetails.getPhoto().getPhotoFile() : null,
                readerDetails.getInterestList()
        );
    }
}
