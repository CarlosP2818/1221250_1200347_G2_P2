package pt.psoft.g1.psoftg1.authormanagement.services.query;

import pt.psoft.g1.psoftg1.authormanagement.api.BookShortView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorQueryService {

    Iterable<Author> findAll();

    Optional<Author> findByAuthorNumber(String authorNumber);

    List<Author> findByName(String name);

    List<BookShortView> findBooksByAuthorNumber(String authorNumber);

}
