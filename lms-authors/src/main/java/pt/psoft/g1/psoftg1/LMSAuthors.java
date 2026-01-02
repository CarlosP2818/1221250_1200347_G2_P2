package pt.psoft.g1.psoftg1;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableRabbit
public class LMSAuthors {

    public static void main(String[] args) {
        SpringApplication.run(LMSAuthors.class, args);
    }

}
