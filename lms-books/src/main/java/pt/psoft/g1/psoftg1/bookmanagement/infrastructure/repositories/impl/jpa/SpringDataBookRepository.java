package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.jpa.BookJpa;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface SpringDataBookRepository extends JpaRepository<BookJpa, String> {

    Optional<BookJpa> findByIsbn_Isbn(String isbn);

    List<BookJpa> findByGenre_GenreContaining(Long genre);

    List<BookJpa> findByTitle_TitleContaining(String title);

    List<BookJpa> findByAuthorIds(List<Long> authorsIds);
}
