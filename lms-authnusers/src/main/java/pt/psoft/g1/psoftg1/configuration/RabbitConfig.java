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
        return new Queue("temp.user.created.queue", true, false, false);
    }

    @Bean
    public Queue getUserByUsernameQueue() {
        return new Queue("get.user.by.username.queue", true, false, false);
    }

    @Bean
    public Queue userCreateQueue() {
        return new Queue("user.create.queue", true, false, false);
    }

    // Binding para TEMP_USER_CREATED
    @Bean
    public Binding tempUserCreatedBinding(DirectExchange userExchange, Queue tempUserCreatedQueue) {
        return BindingBuilder.bind(tempUserCreatedQueue)
                .to(userExchange)
                .with(UserEvents.TEMP_USER_CREATED);
    }

    // Binding para USER.CREATE
    @Bean
    public Binding userCreateBinding(DirectExchange userExchange, Queue userCreateQueue) {
        return BindingBuilder.bind(userCreateQueue)
                .to(userExchange)
                .with("user.create");
    }

    @Bean
    public DirectExchange repliesExchange() {
        return new DirectExchange("user.replies.exchange");
    }

    // Binding para USER_REPLY
    @Bean
    public Binding getUserReplyBinding(Queue getUserByUsernameQueue, DirectExchange repliesExchange) {
        return BindingBuilder.bind(getUserByUsernameQueue)
                .to(repliesExchange)
                .with("user.reply");
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


