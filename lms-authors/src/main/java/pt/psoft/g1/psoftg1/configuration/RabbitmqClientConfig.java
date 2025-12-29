package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("!test")
@Configuration
public  class RabbitmqClientConfig {

    @Bean
    public DirectExchange direct() {
        return new DirectExchange("LMS.books");
    }

    @Bean
    public Queue authorTempCreatedQueue() {
        return new Queue("author-temp-created-queue", true);
    }

    @Bean
    public Queue genreTempCreatedQueue() {
        return new Queue("genre-temp-created-queue", true);
    }

}
