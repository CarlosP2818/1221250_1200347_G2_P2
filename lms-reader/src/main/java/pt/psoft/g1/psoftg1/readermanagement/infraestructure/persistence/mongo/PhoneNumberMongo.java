package pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.mongo;

import lombok.Getter;

@Getter
public class PhoneNumberMongo {

    String phoneNumber;

    public PhoneNumberMongo(String phoneNumber) {
        setPhoneNumber(phoneNumber);
    }

    private void setPhoneNumber(String number) {
        if(!(number.startsWith("9") || number.startsWith("2")) || number.length() != 9) {
            throw new IllegalArgumentException("Phone number is not valid: " + number);
        }

        this.phoneNumber = number;
    }

    protected PhoneNumberMongo() {}
}
