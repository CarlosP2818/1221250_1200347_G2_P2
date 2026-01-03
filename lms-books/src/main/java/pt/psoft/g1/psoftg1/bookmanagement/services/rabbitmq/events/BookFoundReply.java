package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events;


import pt.psoft.g1.psoftg1.bookmanagement.model.dto.BookDto;

public record BookFoundReply(
        String correlationId,
        BookDto book) {}