package pt.psoft.g1.psoftg1.bookmanagement.services.command;

import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.UpdateBookRequest;

public interface BookCommandService {

    Book update(UpdateBookRequest request, Long currentVersion);

    Book update(BookViewAMQP bookViewAMQP);

    Book create(CreateBookRequest request); // REST request

    Book save(Book book);

    OutboxEventMongo createTemp(CreateBookRequest request, String photoURI, String correlationId);

    Book removeBookPhoto(String isbn, long desiredVersion);

}
