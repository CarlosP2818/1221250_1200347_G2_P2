package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public  class RabbitConfig {

    @Bean
    public DirectExchange bookExchange() {
        return new DirectExchange("book.events.exchange");
    }

    @Bean
    public DirectExchange authorExchange() {
        return new DirectExchange("author.events.exchange");
    }

    @Bean
    public DirectExchange genreExchange() {
        return new DirectExchange("genre.events.exchange");
    }

    @Bean
    public DirectExchange repliesExchange() {
        return new DirectExchange("book.replies.exchange");
    }

    // -------------------- Queues --------------------
    @Bean
    public Queue authorReplyQueue() {
        return new Queue("author.reply.queue", true); // true = durable
    }

    @Bean
    public Queue genreReplyQueue() {
        return new Queue("genre.reply.queue", true);
    }

    @Bean
    public Queue authorCreatedQueue() {
        return new Queue("author.created.queue", true, false, false);
    }

    @Bean
    public Queue genreCreatedQueue() {
        return new Queue("genre.created.queue", true, false, false);
    }

    // -------------------- Bindings --------------------
    @Bean
    public Binding authorReplyBinding(DirectExchange repliesExchange,
                                      @Qualifier("authorReplyQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(repliesExchange)
                .with("author.reply");
    }

    @Bean
    public Binding genreReplyBinding(DirectExchange repliesExchange,
                                     @Qualifier("genreReplyQueue") Queue queue) {
        return BindingBuilder.bind(queue)
                .to(repliesExchange)
                .with("genre.reply");
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitAdmin rabbitAdmin(org.springframework.amqp.rabbit.connection.ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }
}
