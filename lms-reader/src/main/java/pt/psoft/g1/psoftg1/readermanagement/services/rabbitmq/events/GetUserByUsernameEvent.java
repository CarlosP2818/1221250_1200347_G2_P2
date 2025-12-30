package pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq.events;

public record GetUserByUsernameEvent(
        String username,
        String correlationId
) {}

