package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorRabbitmqController;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.shared.model.AuthorsEvents;

@Profile("!test")
@Configuration
public class RabbitmqClientConfig {

    @Bean
    public DirectExchange direct() {
        return new DirectExchange("LMS.authors");
    }

    public static class ReceiverConfig {

        @Bean(name = "autoDeleteQueue_Author_Created")
        public Queue autoDeleteQueue_Author_Created() {

            System.out.println("autoDeleteQueue_Author_Created created!");
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Author_Updated() {
            return new AnonymousQueue();
        }

        @Bean
        public Queue autoDeleteQueue_Author_Deleted() {
            return new AnonymousQueue();
        }

        @Bean
        public Binding binding1(DirectExchange direct,
                                @Qualifier("autoDeleteQueue_Author_Created") Queue autoDeleteQueue_Author_Created) {
            return BindingBuilder.bind(autoDeleteQueue_Author_Created)
                    .to(direct)
                    .with(AuthorsEvents.AUTHOR_CREATED);
        }

        @Bean
        public Binding binding2(DirectExchange direct,
                                Queue autoDeleteQueue_Author_Updated) {
            return BindingBuilder.bind(autoDeleteQueue_Author_Updated)
                    .to(direct)
                    .with(AuthorsEvents.AUTHOR_UPDATED);
        }

        @Bean
        public Binding binding3(DirectExchange direct,
                                Queue autoDeleteQueue_Author_Deleted) {
            return BindingBuilder.bind(autoDeleteQueue_Author_Deleted)
                    .to(direct)
                    .with(AuthorsEvents.AUTHOR_DELETED);
        }

        @Bean
        public AuthorRabbitmqController receiver(AuthorService authorService, @Qualifier("autoDeleteQueue_Author_Created") Queue autoDeleteQueue_Author_Created) {
            return new AuthorRabbitmqController();
        }
    }
}
