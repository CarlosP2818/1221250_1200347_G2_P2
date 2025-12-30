package pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.readermanagement.services.CreateReaderRequest;
import pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq.events.CreateUserEvent;
import pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq.events.GetUserByUsernameEvent;
import org.springframework.beans.factory.annotation.Qualifier;

@Service
public class Publisher {

    private final RabbitTemplate rabbitTemplate;

    // Aqui usamos o @Qualifier para garantir que injetamos o exchange correto
    private final DirectExchange userExchange;

    public Publisher(RabbitTemplate rabbitTemplate, @Qualifier("userEventsExchange") DirectExchange userExchange) {
        this.rabbitTemplate = rabbitTemplate;
        this.userExchange = userExchange;
    }

    public void sendCreateUserEvent(CreateReaderRequest createReaderRequest, String correlationId) {
        CreateUserEvent event = new CreateUserEvent();
        event.setUsername(createReaderRequest.getUsername());
        event.setPassword(createReaderRequest.getPassword());
        event.setFullName(createReaderRequest.getFullName());
        event.setCorrelationId(correlationId);
        event.setCreateReaderRequest(createReaderRequest);

        rabbitTemplate.convertAndSend(
                userExchange.getName(),
                "user.create",
                event
        );
    }

    public void requestUserByUsername(String username, String correlationId) {
        GetUserByUsernameEvent cmd = new GetUserByUsernameEvent(username, correlationId);

        rabbitTemplate.convertAndSend(
                userExchange.getName(),
                "user.get.by.username",
                cmd
        );
    }
}
