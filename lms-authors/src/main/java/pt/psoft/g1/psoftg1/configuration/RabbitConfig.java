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
    public DirectExchange authorExchange() { return new DirectExchange("author.events.exchange"); }

    @Bean
    public Queue authorQueue() {
        return new Queue("author.created.queue", true); // Esta é a fila real
    }


    @Bean
    public DirectExchange bookAuthorRepliesExchange() {
        return new DirectExchange("book.author.replies.exchange");
    }

    @Bean
    public Queue authorBookReplyQueue() {
        return new Queue("author.book.reply.queue", true);
    }

    @Bean
    public Binding authorBookReplyBinding(
            DirectExchange bookAuthorRepliesExchange,
            @Qualifier("authorBookReplyQueue") Queue queue
    ) {
        return BindingBuilder.bind(queue)
                .to(bookAuthorRepliesExchange)
                .with("author.reply");
    }

    @Bean
    public Binding authorQueueBinding(
            DirectExchange authorExchange,
            @Qualifier("authorQueue") Queue queue
    ) {
        return BindingBuilder.bind(queue)
                .to(authorExchange)
                .with("author.created");
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
