package pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.jpa;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.jpa.BookJpa;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Profile("sql")
public class BookRepositoryJpaImpl implements BookRepository {
    private final SpringDataBookRepository jpaRepository;
    private final EntityManager em;
    private final BookJpaMapper bookJpaMapper;

    @Override
    public Optional<Book> findByIsbn(String isbn) {
        return jpaRepository.findByIsbn_Isbn(isbn)
                .map(bookJpaMapper::toDomain);
    }

    @Override
    public List<Book> findByGenreId(Long genreId) {
        return jpaRepository.findByGenre_GenreContaining(genreId)
                .stream().map(bookJpaMapper::toDomain).toList();
    }

    @Override
    public List<Book> findByTitle(String title) {
        return jpaRepository.findByTitle_TitleContaining(title)
                .stream().map(bookJpaMapper::toDomain).toList();
    }

    @Override
    public List<Book> findByAuthorIds(List<Long> authorsIds) {
        return jpaRepository.findByAuthorIds(authorsIds)
                .stream().map(bookJpaMapper::toDomain).toList();
    }

    @Override
    public List<Book> searchBooks(pt.psoft.g1.psoftg1.shared.services.Page page, SearchBooksQuery query) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<BookJpa> cq = cb.createQuery(BookJpa.class);
        final Root<BookJpa> root = cq.from(BookJpa.class);

        final List<Predicate> predicates = new ArrayList<>();

        if (StringUtils.hasText(query.getTitle())) {
            predicates.add(
                    cb.like(root.get("title").get("title"),
                            query.getTitle() + "%")
            );
        }

        if (StringUtils.hasText(query.getGenreId())) {
            predicates.add(
                    cb.equal(root.get("genreId"),
                            query.getGenreId())
            );
        }

        if (query.getAuthorIds() != null && !query.getAuthorIds().isEmpty()) {
            predicates.add(
                    root.join("authorIds").in(query.getAuthorIds())
            );
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.orderBy(cb.asc(root.get("title")));

        final TypedQuery<BookJpa> q = em.createQuery(cq);
        q.setFirstResult((page.getNumber() - 1) * page.getLimit());
        q.setMaxResults(page.getLimit());

        return q.getResultList()
                .stream()
                .map(bookJpaMapper::toDomain)
                .toList();
    }

    @Override
    public Book save(Book book) {
        var saved = jpaRepository.save(bookJpaMapper.toJpa(book));
        return bookJpaMapper.toDomain(saved);
    }

    @Override
    public Iterable<Book> findAll() {
        return jpaRepository.findAll().stream().map(bookJpaMapper::toDomain).toList();
    }

    @Override
    public void delete(Book book) {
        var toDelete = bookJpaMapper.toJpa(book);
        if (toDelete.getPk() != null) {
            jpaRepository.deleteById(toDelete.getPk());
        }
    }
}
