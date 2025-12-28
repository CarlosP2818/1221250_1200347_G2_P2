package pt.psoft.g1.psoftg1.readermanagement.infraestructure.repositories.impl.mongo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.mongo.GenreMongoMapper;
import pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.mongo.ReaderDetailsMongo;
import pt.psoft.g1.psoftg1.readermanagement.model.BirthDate;
import pt.psoft.g1.psoftg1.readermanagement.model.PhoneNumber;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.mongo.ReaderMongoMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.time.format.DateTimeFormatter;

@Component
@Profile("mongo")
public class ReaderDetailsMongoMapper {

    private ReaderMongoMapper readerMongoMapper;

    private IdGenerator idGenerator;

    private GenreMongoMapper genreMongoMapper;

    @Autowired
    public ReaderDetailsMongoMapper(IdGenerator injectedIdGenerator, ReaderMongoMapper readerMongoMapper, GenreMongoMapper genreMongoMapper) {
        this.idGenerator = injectedIdGenerator;
        this.readerMongoMapper = readerMongoMapper;
        this.genreMongoMapper = genreMongoMapper;
    }

    public ReaderDetails toDomain(ReaderDetailsMongo mongo) {
        if (mongo == null) return null;

        return new ReaderDetails(
                Integer.parseInt(mongo.getReaderNumber().getReaderNumber().split("/")[1]),
                new Reader(mongo.getReader().getUsername(),mongo.getReader().getPassword()),
                new BirthDate(mongo.getBirthDate().getBirthDate().toString()),
                new PhoneNumber(mongo.getPhoneNumber().getPhoneNumber()),
                mongo.isGdprConsent(),
                mongo.isMarketingConsent(),
                mongo.isThirdPartySharingConsent(),
                mongo.getPhoto() != null ? mongo.getPhoto().getPhotoFile() : null,
                mongo.getInterestList().stream().map(genreMongoMapper::toDomain).toList()

        );
    }

    public ReaderDetailsMongo toMongo(ReaderDetails readerDetails) {
        if (readerDetails == null) return null;

        return new ReaderDetailsMongo(
                idGenerator.generateId(),
                Integer.parseInt(readerDetails.getReaderNumber().split("/")[1]),
                readerMongoMapper.toMongo(readerDetails.getReader()),
                readerDetails.getBirthDate().getBirthDate().format(DateTimeFormatter.ISO_LOCAL_DATE),
                readerDetails.getPhoneNumber(),
                readerDetails.isGdprConsent(),
                readerDetails.isMarketingConsent(),
                readerDetails.isThirdPartySharingConsent(),
                readerDetails.getPhoto() != null ? readerDetails.getPhoto().getPhotoFile() : null,
                readerDetails.getInterestList().stream().map(genreMongoMapper::toMongo).toList()
        );
    }
}
