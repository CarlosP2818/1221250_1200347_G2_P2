package pt.psoft.g1.psoftg1.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public  class RabbitConfig {

    @Bean
    public DirectExchange genreExchange() { return new DirectExchange("genre.events.exchange"); }

    @Bean
    public Queue genreQueue() { return new Queue("genre.created.queue", true); }

    @Bean
    public DirectExchange bookGenreRepliesExchange() {
        return new DirectExchange("book.genre.replies.exchange");
    }

    @Bean
    public Queue genreBookReplyQueue() {
        return new Queue("genre.book.reply.queue", true);
    }

    @Bean
    public Binding genreBookReplyBinding(
            DirectExchange bookGenreRepliesExchange,
            @Qualifier("genreBookReplyQueue") Queue queue
    ) {
        return BindingBuilder.bind(queue)
                .to(bookGenreRepliesExchange)
                .with("genre.reply"); // routing key usada no send do listener
    }

    @Bean
    public Binding genreQueueBinding(
            DirectExchange genreExchange,
            @Qualifier("genreQueue") Queue queue
    ) {
        return BindingBuilder.bind(queue)
                .to(genreExchange)
                .with("genre.created"); // routing key
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
