package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.psoft.g1.psoftg1.shared.model.AuthorsEvents;

@Profile("!test")
@Configuration
public  class RabbitConfig {

    public static final String AUTHOR_EXCHANGE = "author.exchange";

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(AUTHOR_EXCHANGE);
    }


    @Bean
    public Queue authorCreatedQueue() {
        return new Queue("author-created-queue", true);
    }

    @Bean
    public Queue tempAuthorCreatedQueue() {
        return new Queue("temp-author-created-queue", true);
    }

    @Bean
    public Binding bindingAuthorCreated(Queue authorCreatedQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(authorCreatedQueue)
                .to(directExchange)
                .with(AuthorsEvents.AUTHOR_CREATED);
    }

    @Bean
    public Binding bindingTempAuthorCreated(Queue tempAuthorCreatedQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(tempAuthorCreatedQueue)
                .to(directExchange)
                .with(AuthorsEvents.AUTHOR_TEMP_CREATED);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
