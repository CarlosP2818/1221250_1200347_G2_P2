package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange userEventsExchange() {
        return new DirectExchange("user.events.exchange");
    }

    // Exchange principal para eventos de Reader
    @Bean
    public DirectExchange readerExchange() {
        return new DirectExchange("reader.events.exchange");
    }

    // Fila para receber eventos de Reader criado
    @Bean
    public Queue readerCreatedQueue() {
        return new Queue("user.reader.created.queue");
    }

    // Binding entre exchange e fila
    @Bean
    public Binding readerCreatedBinding(DirectExchange readerExchange, Queue readerCreatedQueue) {
        return BindingBuilder.bind(readerCreatedQueue)
                .to(readerExchange)
                .with("reader.created");
    }

    // ------------------- Binding para replies do User -------------------
    @Bean
    public DirectExchange repliesExchange() {
        return new DirectExchange("user.replies.exchange");
    }

    @Bean
    public Queue userReplyQueue() {
        return new Queue("user.reply.queue", true, false, false);
    }

    @Bean
    public Binding userReplyBinding(DirectExchange repliesExchange, Queue userReplyQueue) {
        return BindingBuilder.bind(userReplyQueue)
                .to(repliesExchange)
                .with("user.reply");
    }

    // Conversor JSON para envio/recepção de eventos
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
