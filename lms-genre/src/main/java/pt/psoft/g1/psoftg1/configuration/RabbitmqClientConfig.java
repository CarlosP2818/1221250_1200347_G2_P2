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
        return new DirectExchange("LMS.genre");
    }

    private static class ReceiverConfig {

        @Bean
        public Queue genreTempCreatedQueue() {
            return QueueBuilder.durable("genre-temp-created-queue").build();
        }

        @Bean
        public Binding genreTempCreatedBinding(Queue genreTempCreatedQueue, DirectExchange genreExchange) {
            return BindingBuilder.bind(genreTempCreatedQueue).to(genreExchange).with("genre.temp.created");
        }
    }
}
