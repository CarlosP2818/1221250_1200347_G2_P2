package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.psoft.g1.psoftg1.shared.model.UserEvents;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange userExchange() {
        return new DirectExchange("user.events.exchange");
    }

    @Bean
    public Queue tempUserCreatedQueue() {
        return new AnonymousQueue();
    }

    @Bean
    public Binding tempUserCreatedBinding(DirectExchange userExchange, Queue tempUserCreatedQueue) {
        return BindingBuilder.bind(tempUserCreatedQueue)
                .to(userExchange)
                .with(UserEvents.TEMP_USER_CREATED);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

