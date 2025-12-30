package pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pt.psoft.g1.psoftg1.shared.infrastructure.persistence.jpa.EntityWithPhotoEmbeddable;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "READER_DETAILS")
public class ReaderDetailsJpa extends EntityWithPhotoEmbeddable {

    @Id
    private String pk;

    @Getter
    @Setter
    private String reader;

    @Getter
    private ReaderNumberEmbedded readerNumber;

    @Embedded
    @Getter
    private BirthDateEmbedded birthDate;

    @Embedded
    @Getter
    private PhoneNumberEmbedded phoneNumber;

    @Setter
    @Getter
    @Basic
    private boolean gdprConsent;

    @Setter
    @Basic
    @Getter
    private boolean marketingConsent;

    @Setter
    @Basic
    @Getter
    private boolean thirdPartySharingConsent;

    @Version
    @Getter
    private Long version;

    @Getter
    @Setter
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "reader_interest_list",
            joinColumns = @JoinColumn(name = "reader_pk")
    )
    @Column(name = "interest")
    private List<String> interestList;

    public ReaderDetailsJpa(String pk,int readerNumber, String reader, String birthDate, String phoneNumber, boolean gdpr, boolean marketing, boolean thirdParty, String photoURI, List<String> interestList) {
        if(reader == null || phoneNumber == null) {
            throw new IllegalArgumentException("Provided argument resolves to null object");
        }

        if(!gdpr) {
            throw new IllegalArgumentException("Readers must agree with the GDPR rules");
        }

        this.pk = pk;
        setReader(reader);
        setReaderNumber(new ReaderNumberEmbedded(readerNumber));
        setPhoneNumber(new PhoneNumberEmbedded(phoneNumber));
        setBirthDate(new BirthDateEmbedded(birthDate));
        //By the client specifications, gdpr can only have the value of true. A setter will be created anyways in case we have accept no gdpr consent later on the project
        setGdprConsent(true);

        setPhotoInternal(photoURI);
        setMarketingConsent(marketing);
        setThirdPartySharingConsent(thirdParty);
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

    protected ReaderDetailsJpa() {
        // for ORM only
    }
}
