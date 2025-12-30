package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.mongo.ReaderDetailsMongo;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;

import java.time.format.DateTimeFormatter;

@Component
@Profile("mongo")
public class ReaderDetailsMongoMapper {

    private IdGenerator idGenerator;

    @Autowired
    public ReaderDetailsMongoMapper(IdGenerator injectedIdGenerator) {
        this.idGenerator = injectedIdGenerator;
    }

    public ReaderDetails toDomain(ReaderDetailsMongo mongo) {
        if (mongo == null) return null;

        return new ReaderDetails(
                Integer.parseInt(mongo.getReaderNumber().getReaderNumber().split("/")[1]),
                mongo.getReader(),
                new BirthDate(mongo.getBirthDate().getBirthDate().toString()),
                new PhoneNumber(mongo.getPhoneNumber().getPhoneNumber()),
                mongo.isGdprConsent(),
                mongo.isMarketingConsent(),
                mongo.isThirdPartySharingConsent(),
                mongo.getPhoto() != null ? mongo.getPhoto().getPhotoFile() : null,
                mongo.getInterestList()

        );
    }

    public ReaderDetailsMongo toMongo(ReaderDetails readerDetails) {
        if (readerDetails == null) return null;

        return new ReaderDetailsMongo(
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
