package pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;


@Embeddable
@Getter
public class ReaderNumberEmbedded implements Serializable {
    @Column(name = "READER_NUMBER")
    private String readerNumber;

    public ReaderNumberEmbedded(int year, int number) {
        this.readerNumber = year + "/" + number;
    }

    public ReaderNumberEmbedded(int number) {
        this.readerNumber = LocalDate.now().getYear() + "/" + number;
    }

    protected ReaderNumberEmbedded() {}
}
