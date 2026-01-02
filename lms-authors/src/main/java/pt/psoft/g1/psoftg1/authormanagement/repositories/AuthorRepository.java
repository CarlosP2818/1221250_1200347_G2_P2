package pt.psoft.g1.psoftg1.authormanagement.repositories;

import pt.psoft.g1.psoftg1.authormanagement.model.Author;

import java.util.List;
import java.util.Optional;

public interface AuthorRepository {

    Optional<Author> findByAuthorNumber(String authorNumber);

    List<Author> searchByNameNameStartsWith(String name);

    List<Author> searchByNameName(String name);

    Author save(Author author);

    Iterable<Author> findAll();

    void delete(Author author);

}
