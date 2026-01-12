package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.command;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.CreateBookRequest;

public interface BookCommandRepository {
    Book save(Book book);
    void delete(Book book);
}
