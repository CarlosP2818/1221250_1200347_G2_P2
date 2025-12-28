package pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.mongo;

import jakarta.persistence.Basic;
import jakarta.persistence.Id;
import jakarta.persistence.Version;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.persistence.mongo.GenreMongo;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.mongo.ReaderMongo;

import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Document(collection = "readerDetails")
public class ReaderDetailsMongo {

    @Id
    private String id;

    private ReaderMongo reader;

    private ReaderNumberMongo readerNumber;

    private BirthDateMongo birthDate;

    private PhoneNumberMongo phoneNumber;


    @Basic
    private boolean gdprConsent;

    @Basic
    private boolean marketingConsent;

    @Basic
    private boolean thirdPartySharingConsent;

    @Version
    private Long version;

    private List<GenreMongo> interestList;

    private Photo photo;

    public ReaderDetailsMongo(String id, int readerNumber, ReaderMongo reader, String birthDate, String phoneNumber, boolean gdpr, boolean marketing, boolean thirdParty, String photoURI, List<GenreMongo> interestList) {
        if(reader == null || phoneNumber == null) {
            throw new IllegalArgumentException("Provided argument resolves to null object");
        }

        if(!gdpr) {
            throw new IllegalArgumentException("Readers must agree with the GDPR rules");
        }

        this.id = id;
        setReader(reader);
        setReaderNumber(new ReaderNumberMongo(readerNumber));
        setPhoneNumber(new PhoneNumberMongo(phoneNumber));
        setBirthDate(new BirthDateMongo(birthDate));
        //By the client specifications, gdpr can only have the value of true. A setter will be created anyways in case we have accept no gdpr consent later on the project
        setGdprConsent(true);

        setPhotoInternal(photoURI);
        setMarketingConsent(marketing);
        setThirdPartySharingConsent(thirdParty);
        if(interestList == null) {
            setInterestList(new ArrayList<GenreMongo>());
        }else {
            setInterestList(interestList);
        }
    }

    private void setReaderNumber(ReaderNumberMongo readerNumber) {
        if(readerNumber != null) {
            this.readerNumber = readerNumber;
        }
    }

    private void setPhoneNumber(PhoneNumberMongo phoneNumber) {
        if(phoneNumber != null) {
            this.phoneNumber = phoneNumber;
        }
    }

    private void setBirthDate(BirthDateMongo birthDateMongo) {
        if(birthDateMongo != null) {
            this.birthDate = birthDateMongo;
        }
    }

    protected void setPhotoInternal(String photoURI) {
        if (photoURI == null) {
            this.photo = null;
        } else {
            try {
                //If the Path object instantiation succeeds, it means that we have a valid Path
                this.photo = new Photo(Path.of(photoURI));
            } catch (InvalidPathException e) {
                //For some reason it failed, let's set to null to avoid invalid references to photos
                this.photo = null;
            }
        }
    }

    protected ReaderDetailsMongo() {
        // for ORM only
    }


}
