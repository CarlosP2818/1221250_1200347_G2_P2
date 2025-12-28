package pt.psoft.g1.psoftg1.readermanagement.services;

public record ReaderCreatedEvent(Long readerId,
                                 String username,
                                 String password,
                                 String fullName
) {}
