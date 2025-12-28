package pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.mongo;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReaderNumberMongo {

    String readerNumber;

    public ReaderNumberMongo(int year, int number) {
        this.readerNumber = year + "/" + number;
    }

    public ReaderNumberMongo(int number) {
        this.readerNumber = LocalDate.now().getYear() + "/" + number;
    }

    protected ReaderNumberMongo() {}
}