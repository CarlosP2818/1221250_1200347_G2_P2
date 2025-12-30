package pt.psoft.g1.psoftg1.readermanagement.infraestructure.persistence.jpa;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.PropertySource;

import java.time.LocalDate;


@Embeddable
@NoArgsConstructor
@PropertySource({"classpath:config/library.properties"})
public class BirthDateEmbedded {
    @Getter
    @Column(nullable = false, updatable = false)
    @Temporal(TemporalType.DATE)
    LocalDate birthDate;

    @Transient
    private final String dateFormatRegexPattern = "\\d{4}-\\d{2}-\\d{2}";

    public BirthDateEmbedded(int year, int month, int day) {
        setBirthDate(year, month, day);
    }

    public BirthDateEmbedded(String birthDate) {
        if(!birthDate.matches(dateFormatRegexPattern)) {
            throw new IllegalArgumentException("Provided birth date is not in a valid format. Use yyyy-MM-dd");
        }

        String[] dateParts = birthDate.split("-");

        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

        setBirthDate(year, month, day);
    }

    private void setBirthDate(int year, int month, int day) {
        this.birthDate = LocalDate.of(year, month, day);
    }
}
