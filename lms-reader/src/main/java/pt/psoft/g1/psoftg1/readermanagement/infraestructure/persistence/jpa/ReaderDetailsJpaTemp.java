package pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.shared.infrastructure.persistence.jpa.EntityWithPhotoEmbeddable;
import pt.psoft.g1.psoftg1.shared.model.Photo;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "READER_DETAILS_TEMP")
public class ReaderDetailsJpaTemp extends EntityWithPhotoEmbeddable {

    @Id
    private String correlationId;

    @Setter
    private String reader;

    private ReaderNumberEmbedded readerNumber;

    @Embedded
    private BirthDateEmbedded birthDate;

    @Embedded
    private PhoneNumberEmbedded phoneNumber;

    @Setter
    @Basic
    private boolean gdprConsent;

    @Setter
    @Basic
    private boolean marketingConsent;

    @Setter
    @Basic
    private boolean thirdPartySharingConsent;

    @Version
    private Long version;

    @NotBlank
    @Email
    @NonNull
    private String username;

    @NotBlank
    @NonNull
    private String password;

    @NotBlank
    private String fullName;

    @Nullable
    @Setter
    private String photo;

    @Setter
    private boolean gdpr;

    @Setter
    private boolean marketing;

    @Setter
    private boolean thirdParty;

    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "reader_interest_list_temp",
            joinColumns = @JoinColumn(name = "correlation_id")
    )
    @Column(name = "interest")
    private List<String> interestList;

    public ReaderDetailsJpaTemp(String correlationId,int readerNumber, String reader, String birthDate, String phoneNumber, boolean gdpr, boolean marketing, boolean thirdParty, String photoURI, List<String> interestList, String username, String password, String fullName) {
        if(reader == null || phoneNumber == null) {
            throw new IllegalArgumentException("Provided argument resolves to null object");
        }

        if(!gdpr) {
            throw new IllegalArgumentException("Readers must agree with the GDPR rules");
        }

        this.correlationId = correlationId;
        setReader(reader);
        setReaderNumber(new ReaderNumberEmbedded(readerNumber));
        setPhoneNumber(new PhoneNumberEmbedded(phoneNumber));
        setBirthDate(new BirthDateEmbedded(birthDate));
        //By the client specifications, gdpr can only have the value of true. A setter will be created anyways in case we have accept no gdpr consent later on the project
        setGdprConsent(true);

        setPhotoInternal(photoURI);
        setMarketingConsent(marketing);
        setThirdPartySharingConsent(thirdParty);

        this.username = username;
        this.password = password;
        this.fullName = fullName;
        if(interestList == null) {
            setInterestList(new ArrayList<String>());
        }else {
            setInterestList(interestList);
        }
    }

    private void setPhoneNumber(PhoneNumberEmbedded number) {
        if(number != null) {
            this.phoneNumber = number;
        }
    }

    private void setReaderNumber(ReaderNumberEmbedded readerNumber) {
        if(readerNumber != null) {
            this.readerNumber = readerNumber;
        }
    }

    private void setBirthDate(BirthDateEmbedded date) {
        if(date != null) {
            this.birthDate = date;
        }
    }

    protected ReaderDetailsJpaTemp() {
        // for ORM only
    }

    @Nullable
    public Photo getPhoto() {
        assert this.photo != null;
        return new Photo(Path.of(this.photo));
    }
}
