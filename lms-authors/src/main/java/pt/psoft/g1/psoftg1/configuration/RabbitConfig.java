package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange authorExchange() {
        return new DirectExchange("author.events.exchange");
    }

    @Bean
    public DirectExchange bookRepliesExchange() {
        return new DirectExchange("book.replies.exchange");
    }

    @Bean
    public DirectExchange genreRepliesExchange() {
        return new DirectExchange("LMS.genres.replies");
    }

    // Queues
    @Bean
    public Queue authorCreatedQueue() {
        return new Queue("author.created.queue", true, false, false);
    }

    @Bean
    public Queue bookReplyQueue() {
        return new Queue("author.reply.queue", true, false, false);
    }

    @Bean
    public Queue genreReplyQueue() {
        return new Queue("author.genre.reply.queue", true, false, false);
    }

    // Bindings
    @Bean
    public Binding authorCreatedBinding(
            @Qualifier("authorExchange") DirectExchange authorExchange,
            Queue authorCreatedQueue) {
        return BindingBuilder.bind(authorCreatedQueue).to(authorExchange).with("author.created");
    }

    @Bean
    public Binding bookReplyBinding(
            @Qualifier("bookRepliesExchange") DirectExchange bookRepliesExchange,
            Queue bookReplyQueue) {
        return BindingBuilder.bind(bookReplyQueue)
                .to(bookRepliesExchange)
                .with("author.reply"); // Antes era book.created.reply
    }

    @Bean
    public Binding genreReplyBinding(
            @Qualifier("genreRepliesExchange") DirectExchange genreRepliesExchange,
            Queue genreReplyQueue) {
        return BindingBuilder.bind(genreReplyQueue).to(genreRepliesExchange).with("genre.created.reply");
    }

    // Conversor JSON para envio/recepção de eventos
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }


    // RabbitAdmin
    @Bean
    public RabbitAdmin rabbitAdmin(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
