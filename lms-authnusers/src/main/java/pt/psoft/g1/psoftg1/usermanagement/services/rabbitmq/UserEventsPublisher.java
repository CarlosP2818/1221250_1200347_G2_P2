package pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.shared.model.UserEvents;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa.UserJpaMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.CreateReaderRequestDto;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.RoleDto;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.UserDto;
import pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events.ReaderCreatedEvent;
import pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events.UserFoundReply;

import java.util.stream.Collectors;

@Service
public class UserEventsPublisher {

    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange exchange;
    private final UserJpaMapper userMapper;

    public UserEventsPublisher(RabbitTemplate rabbitTemplate,
                               @Qualifier("repliesExchange") DirectExchange userExchange, UserJpaMapper userMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.exchange = userExchange;
        this.userMapper = userMapper;
    }

    /**
     * OUTBOX-BASED publishing
     */
    public void publishUserCreated(UserFoundReply payload) {

        rabbitTemplate.convertAndSend(
                exchange.getName(),
                UserEvents.USER_REPLY,
                payload
        );
    }
}

