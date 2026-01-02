package pt.psoft.g1.psoftg1.authormanagement.services;

import pt.psoft.g1.psoftg1.authormanagement.api.BookShortView;
import pt.psoft.g1.psoftg1.authormanagement.api.TempAuthor;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthorService {

    Iterable<Author> findAll();

    Optional<Author> findByAuthorNumber(String authorNumber);

    List<Author> findByName(String name);

    Author create(CreateAuthorRequest resource);

    Author partialUpdate(String authorNumber, UpdateAuthorRequest resource, long desiredVersion);

    List<BookShortView> findBooksByAuthorNumber(String authorNumber);

    Optional<Author> removeAuthorPhoto(String authorNumber, long desiredVersion);

    TempAuthor createTempAuthor(String name, String bio, UUID sagaId);
}
