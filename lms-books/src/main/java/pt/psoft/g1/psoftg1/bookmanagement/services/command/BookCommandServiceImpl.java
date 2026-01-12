package pt.psoft.g1.psoftg1.bookmanagement.services.command;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewAMQP;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.command.BookCommandRepository;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.model.Description;
import pt.psoft.g1.psoftg1.bookmanagement.model.Isbn;
import pt.psoft.g1.psoftg1.bookmanagement.model.Title;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.OutboxEventRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.AuthorInnerRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.query.BookQueryService;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.UpdateBookRequest;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.external.service.BookIsbnGateway;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookCommandServiceImpl implements BookCommandService {

    private final BookCommandRepository bookCommandRepository;
    private final PhotoRepository photoRepository;
    private final BookIsbnGateway isbnGateway;

    private final OutboxEventRepository outboxRepository;
    private final BookQueryService bookQueryService;

    @Override
    @Transactional
    @CachePut(value = "books", key = "#result.isbn")
    public Book create(CreateBookRequest request) {

        String isbn = isbnGateway.getIsbnByTitle(request.getTitle())
                .orElseThrow(() -> new NotFoundException("ISBN not found for the given title"));

        try {
            bookQueryService.findByIsbn(isbn);
            throw new ConflictException("Book with ISBN " + isbn + " already exists");
        } catch (NotFoundException e) {
        }

        List<String> authorsNames = request.getAuthors().stream()
                .map(AuthorInnerRequest::getName)
                .toList();

        Book newBook = new Book(
                new Isbn(isbn),
                new Title(request.getTitle()),
                new Description(request.getDescription()),
                request.getGenreName(),
                authorsNames,
                request.getPhotoURI()
        );

        bookCommandRepository.save(newBook);

        return newBook;
    }

    @Override
    @Transactional
    @CacheEvict(value = "books", key = "#request.isbn")
    public Book update(UpdateBookRequest request, Long currentVersion) {

        Book book = bookQueryService.findByIsbn(request.getIsbn());

        if (request.getAuthorsIds() != null) {
            book.setAuthorsIds(request.getAuthorsIds());
        }

        if (StringUtils.hasText(String.valueOf(request.getGenreName()))) {
            book.setGenreName(request.getGenreName());
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

        return bookCommandRepository.save(book);
    }

    @Override
    public Book save(Book book) {
        return this.bookCommandRepository.save(book);
    }

    @Override
    public OutboxEventMongo createTemp(CreateBookRequest request, String photoURI, String correlationId) {
        // 1. Criar o evento para o MongoDB
        OutboxEventMongo event = new OutboxEventMongo();
        event.setType("BOOK_CREATION_REQUESTED");
        event.setAggregateType("Book");
        // Como ainda não temos o ISBN definitivo ou o ID do SQL, usamos o correlationId ou o título
        event.setAggregateId(request.getTitle());
        event.setCorrelationId(correlationId);
        event.setCreatedAt(LocalDateTime.now());
        event.setProcessed(false);

        event.setPayload(request.toString());

        return outboxRepository.save(event);
    }

    @Override
    @Transactional
    @CacheEvict(value = "books", key = "#isbn")
    public Book removeBookPhoto(String isbn, long desiredVersion) {
        Book book = bookQueryService.findByIsbn(isbn);

        String photoFile = book.getPhoto().getPhotoFile();
        if (photoFile == null) {
            throw new NotFoundException("Book did not have a photo assigned to it.");
        }

        book.setPhoto(null);
        var updatedBook = bookCommandRepository.save(book);
        photoRepository.deleteByPhotoFile(photoFile);

        return updatedBook;
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
