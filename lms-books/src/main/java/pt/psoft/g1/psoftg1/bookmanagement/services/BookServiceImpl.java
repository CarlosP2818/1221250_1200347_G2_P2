package pt.psoft.g1.psoftg1.bookmanagement.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.model.*;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import lombok.RequiredArgsConstructor;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.external.service.BookIsbnGateway;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository bookRepository;
    private final PhotoRepository photoRepository;
    private final BookIsbnGateway isbnGateway;

    @Value("${suggestionsLimitPerGenre}")
    private long suggestionsLimitPerGenre;

    @Override
    @Transactional
    @CachePut(value = "books", key = "#result.isbn")
    public Book create(CreateBookRequest request) {

        String isbn = isbnGateway.getIsbnByTitle(request.getTitle())
                .orElseThrow(() -> new NotFoundException("ISBN not found for the given title"));

        if(bookRepository.findByIsbn(isbn).isPresent()){
            throw new ConflictException("Book with ISBN " + isbn + " already exists");
        }

        Book newBook = new Book(
                new Isbn(isbn),
                new Title(request.getTitle()),
                new Description(request.getDescription()),
                request.getGenreId(),
                request.getAuthorsIds(),
                request.getPhotoURI()
        );

        return bookRepository.save(newBook);
    }


    @Override
    @Transactional
    @CacheEvict(value = "books", key = "#request.isbn")
    public Book update(UpdateBookRequest request, Long currentVersion) {

        Book book = findByIsbn(request.getIsbn());

        if (request.getAuthorsIds() != null) {
            book.setAuthorsIds(request.getAuthorsIds());
        }

        if (StringUtils.hasText(String.valueOf(request.getGenreId()))) {
            book.setGenreId(request.getGenreId());
        }

        if (StringUtils.hasText(request.getTitle())) {
            book.setTitle(new Title(request.getTitle()));
        }

        if (StringUtils.hasText(request.getDescription())) {
            book.setDescription(new Description(request.getDescription()));
        }

        MultipartFile photo = request.getPhoto();
        String photoURI = request.getPhotoURI();
        if (photo == null && photoURI != null || photo != null && photoURI == null) {
            book.setPhoto(null);
        } else if (StringUtils.hasText(photoURI)) {
            book.setPhoto(photoURI);
        }

        return bookRepository.save(book);
    }

    @Override
    public Book save(Book book) {
        return this.bookRepository.save(book);
    }

    @Override
    @Transactional
    @CacheEvict(value = "books", key = "#isbn")
    public Book removeBookPhoto(String isbn, long desiredVersion) {
        Book book = findByIsbn(isbn);

        String photoFile = book.getPhoto().getPhotoFile();
        if (photoFile == null) {
            throw new NotFoundException("Book did not have a photo assigned to it.");
        }

        book.setPhoto(null);
        var updatedBook = bookRepository.save(book);
        photoRepository.deleteByPhotoFile(photoFile);

        return updatedBook;
    }

    @Override
    @Cacheable(value = "books", key = "#isbn")
    public Book findByIsbn(String isbn) {
        return bookRepository.findByIsbn(isbn)
                .orElseThrow(() -> new NotFoundException(Book.class, isbn));
    }

    public List<Book> getBooksSuggestionsForReader(List<Long> genreIds) {
        List<Book> books = new ArrayList<>();

        for (Long genreId : genreIds) {
            List<Book> tempBooks = bookRepository.findByGenreId(genreId);
            if (tempBooks.isEmpty()) continue;

            long genreBookCount = 0;
            for (Book loopBook : tempBooks) {
                if (genreBookCount >= suggestionsLimitPerGenre) break;
                books.add(loopBook);
                genreBookCount++;
            }
        }

        return books;
    }

    @Override
    public List<Book> searchBooks(Page page, SearchBooksQuery query) {
        if (page == null) {
            page = new Page(1, 10);
        }
        if (query == null) {
            query = new SearchBooksQuery("", "", "");
        }
        return bookRepository.searchBooks(page, query);
    }

    @Override
    @Cacheable(value = "booksByGenre", key = "#genre")
    public List<Book> findByGenre(Long genre) {
        return bookRepository.findByGenreId(genre);
    }

    @Override
    @Cacheable(value = "booksByTitle", key = "#title")
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitle(title);
    }

    @Override
    @Cacheable(value = "booksByAuthor", key = "#authorName")
    public List<Book> findByAuthorsIds(List<Long> authorsIds) {
        return bookRepository.findByAuthorIds(authorsIds);
    }

    @Override
    @Transactional
    public Book create(BookViewAMQP bookViewAMQP) {
        // Converte o AMQP DTO para o CreateBookRequest
            CreateBookRequest request = new CreateBookRequest();

            request.setTitle(bookViewAMQP.getTitle());
            request.setGenreId(bookViewAMQP.getGenre());
            request.setAuthorsIds(bookViewAMQP.getAuthorIds());
            request.setDescription(bookViewAMQP.getDescription());

        return this.create(request);
    }

    @Override
    @Transactional
    public Book update(BookViewAMQP bookViewAMQP) {
        UpdateBookRequest request = new UpdateBookRequest(
                bookViewAMQP.getIsbn(),
                bookViewAMQP.getTitle(),
                bookViewAMQP.getGenre(),
                bookViewAMQP.getAuthorIds(),
                bookViewAMQP.getDescription()
        );
        return this.update(request, bookViewAMQP.getVersion());
    }

}
