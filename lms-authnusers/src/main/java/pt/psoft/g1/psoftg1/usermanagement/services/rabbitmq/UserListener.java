package pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa.UserJpaMapper;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.RoleDto;
import pt.psoft.g1.psoftg1.usermanagement.model.dto.UserDto;
import pt.psoft.g1.psoftg1.usermanagement.services.CreateUserRequest;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;
import pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events.CreateUserEvent;
import pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events.GetUserByUsernameEvent;
import pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events.UserFoundReply;

import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserListener {

    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;
    private final UserJpaMapper userMapper;
    private final UserEventsPublisher userEventsPublisher;

    @RabbitListener(queues = "get.user.by.username.queue")
    public void handle(GetUserByUsernameEvent cmd) {

        User realUser = userService.findByUsername(cmd.username()).orElseThrow();

        UserDto user = new UserDto(
                userMapper.toJpa(realUser).getId(),
                realUser.isEnabled(),
                realUser.getUsername(),
                realUser.getPassword(),
                realUser.getName(),
                realUser.getAuthorities()
                        .stream()
                        .map(a -> new RoleDto(a.getAuthority()))
                        .collect(Collectors.toSet())
        );

        UserFoundReply reply =
                new UserFoundReply(cmd.correlationId(), user);

        rabbitTemplate.convertAndSend(
                "user.replies.exchange",
                "user.reply",
                reply
        );
    }

    @RabbitListener(queues = "user.create.queue")
    public void handleCreateUser(CreateUserEvent event) {
        try {
            // Monta o request
            CreateUserRequest request = new CreateUserRequest(
                    event.getUsername(),
                    event.getFullName(),
                    event.getPassword()
            );
            request.setRole("READER");
            request.setAuthorities(new HashSet<>(Arrays.asList("READER", "ADMIN")));

            // Cria usuário
            User user = userService.create(request);
            user.addAuthority(new Role("READER"));
            user.addAuthority(new Role("ADMIN"));

            // Constrói DTO para reply
            UserDto userDto = new UserDto(
                    userMapper.toJpa(user).getId(),
                    user.isEnabled(),
                    user.getUsername(),
                    user.getPassword(),
                    user.getName(),
                    user.getAuthorities().stream()
                            .map(a -> new RoleDto(a.getAuthority()))
                            .collect(Collectors.toSet())
            );
            userEventsPublisher.publishReaderUserCreated(user, event.getCorrelationId());

            System.out.println("Usuário criado: " + user.getUsername());

        } catch (ConflictException e) {
            // Username já existe, apenas loga e considera mensagem processada
            System.out.println("Usuário já existe, ignorando: " + event.getUsername());
        } catch (Exception e) {
            // Outras falhas
            System.err.println("Erro ao processar evento de criação de usuário: " + e.getMessage());
            e.printStackTrace();
        }
    }


}
