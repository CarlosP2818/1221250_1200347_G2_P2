package pt.psoft.g1.psoftg1.usermanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.shared.model.UserEvents;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.ReaderCreatedEventDTO;

@Service
@RequiredArgsConstructor
public class UserEventsPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;

    public void publishReaderUserCreated(User user) {
        ReaderCreatedEventDTO dto = new ReaderCreatedEventDTO(
                user.getUsername(),
                user.getName().getName()
        );

        rabbitTemplate.convertAndSend(
                exchange.getName(),
                UserEvents.TEMP_USER_CREATED, // usar a routing key do evento
                dto
        );
    }
}

