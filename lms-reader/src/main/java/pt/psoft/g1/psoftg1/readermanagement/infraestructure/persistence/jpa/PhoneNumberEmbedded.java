package pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa;

import jakarta.persistence.Embeddable;
import lombok.Getter;

@Getter
@Embeddable
public class PhoneNumberEmbedded {
    String phoneNumber;

    public PhoneNumberEmbedded(String phoneNumber) {
        setPhoneNumber(phoneNumber);
    }

    protected PhoneNumberEmbedded() {}

    private void setPhoneNumber(String number) {
        if(!(number.startsWith("9") || number.startsWith("2")) || number.length() != 9) {
            throw new IllegalArgumentException("Phone number is not valid: " + number);
        }

        this.phoneNumber = number;
    }
}
