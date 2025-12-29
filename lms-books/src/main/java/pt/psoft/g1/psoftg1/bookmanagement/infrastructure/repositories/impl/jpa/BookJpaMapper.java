package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.jpa.BookJpa;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.bookmanagement.services.IdGenerators.IdGenerator;

import java.util.Optional;

@Component
public class BookJpaMapper {

    private SpringDataBookRepository repo;
    private IdGenerator idGenerator;

    @Autowired
    public BookJpaMapper(SpringDataBookRepository injectedRepo, IdGenerator injectedIdGenerator) {
        this.idGenerator = injectedIdGenerator;
        this.repo = injectedRepo;
    }

    public Book toDomain(BookJpa jpa) {
        if (jpa == null) return null;

        Book book = new Book(
                new Isbn(jpa.getIsbn().getIsbn()),
                new Title(jpa.getTitle().getTitle()),
                jpa.getDescription() != null ? new Description(jpa.getDescription().getDescription()) : null,
                jpa.getGenreId(),
                jpa.getAuthorsIds(),
                jpa.getPhoto() != null ? jpa.getPhoto().getPhotoFile() : null
        );

        return book;
    }

    public BookJpa toJpa(Book book) {
        if (book == null) return null;

        // Preferir devolver um BookJpa já existente (mesmo que ID técnico seja diferente)
        if (repo != null) {
            Optional<BookJpa> existing = repo.findByIsbn_Isbn(book.getIsbn());

            if (existing.isPresent()) {
                return existing.get();
            }
        }

        return new BookJpa(
                idGenerator.generateId(),
                book.getIsbn(),
                book.getTitle().getTitle(),
                book.getDescription() != null ? book.getDescription() : null,
                book.getGenreId(),
                book.getAuthorsIds(),
                book.getPhoto()
        );
    }
}
