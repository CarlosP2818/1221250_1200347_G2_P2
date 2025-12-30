package pt.psoft.g1.psoftg1.usermanagement.services.rabbitmq.events;

public record GetUserByUsernameEvent(
        String username,
        String correlationId
) {}

