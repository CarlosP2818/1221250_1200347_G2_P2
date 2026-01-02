package pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.shared.services.Page;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.OutboxEventJpa;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa.UserJpaMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.UserDto;
import pt.psoft.g1.psoftg1.usermanagement.repositories.OutboxEventRepository;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events.UserFoundReply;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class OutboxPublisher {

    private final OutboxEventRepository outboxRepository;
    private final UserRepository userRepository;
    private final UserJpaMapper userMapper;
    private final UserEventsPublisher publisher;

    @Transactional
    @Scheduled(fixedDelay = 5000)
    public void publishEvents() {

        var events =
                outboxRepository.findUnprocessedEvents(new Page(1, 10));

        for (OutboxEventJpa event : events) {
            try {
                if (event.getType().equals("USER_CREATED") && !event.isProcessed()) {

                    Optional<User> user = userRepository.findById(event.getAggregateId());

                    if (user.isEmpty()) {
                        throw new IllegalStateException(
                                "User with ID " + event.getAggregateId() + " not found"
                        );
                    }

                    UserFoundReply userReply = new UserFoundReply(
                            event.getCorrelationId(),
                            new UserDto(
                                    userMapper.toJpa(user.get()).getId(),
                                    user.get().isEnabled(),
                                    user.get().getUsername(),
                                    user.get().getPassword(),
                                    user.get().getName(),
                                    user.get().getAuthorities()
                                            .stream()
                                            .map(a -> new pt.psoft.g1.psoftg1.usermanagement.model.dto.RoleDto(a.getAuthority()))
                                            .collect(Collectors.toSet())
                            )

                    );

                    publisher.publishUserCreated(userReply);
                }

                event.setProcessed(true);

                outboxRepository.save(event);

            } catch (Exception e) {
                // Log the exception and continue with the next event
                e.printStackTrace();
            }
        }
    }
}

