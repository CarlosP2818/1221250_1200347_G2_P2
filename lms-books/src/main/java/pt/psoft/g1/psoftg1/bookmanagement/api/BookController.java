package pt.psoft.g1.psoftg1.bookmanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.BookMongoMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookService;
import pt.psoft.g1.psoftg1.bookmanagement.services.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.SearchBooksQuery;
import pt.psoft.g1.psoftg1.bookmanagement.services.UpdateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.BookReplyListener;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.Publisher;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;

import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "Books", description = "Endpoints for managing Books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private final BookService bookService;
    private final ConcurrencyService concurrencyService;
    private final FileStorageService fileStorageService;

    private final BookViewMapper bookViewMapper;
    private final BookMongoMapper bookMongoMapper;
    private final BookReplyListener bookReplyListener;

    private final Publisher publisher;

    @Value("${feature.maintenance.killswitch}")
    private boolean isKilled;

    @Operation(summary = "Register a new Book")
    @PutMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BookView> create(@RequestBody CreateBookRequest resource) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        //Guarantee that the client doesn't provide a link on the body, null = no photo or error
        resource.setPhotoURI(null);
        MultipartFile file = resource.getPhoto();

        String fileName = fileStorageService.getRequestPhoto(file);

        if (fileName != null) {
            resource.setPhotoURI(fileName);
        }

        String correlationId = UUID.randomUUID().toString();

        bookService.createTemp(resource, null, correlationId);

        // 1. Guardar contexto local
        bookReplyListener.registerCreateBook(
                correlationId,
                resource
        );

        // 2. Publicar evento para criar User
        publisher.sendCreateAuthorEvent(
                resource,
                correlationId
        );

        publisher.sendCreateGenreEvent(
                resource,
                correlationId
        );

        // 4. Responder ao cliente que o processo começou
        return ResponseEntity.accepted()
                .header("X-Correlation-ID", correlationId)
                .build();
    }

    @Operation(summary = "Gets a specific Book by isbn")
    @GetMapping(value = "/{isbn}")
    public ResponseEntity<BookView> findByIsbn(@PathVariable final String isbn) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        final var book = bookService.findByIsbn(isbn);

        BookView bookView = bookViewMapper.toBookView(book);

        return ResponseEntity.ok()
                .eTag(Long.toString(bookMongoMapper.toMongo(book).getVersion()))
                .body(bookView);
    }

    @Operation(summary = "Deletes a book photo")
    @DeleteMapping("/{isbn}/photo")
    public ResponseEntity<Void> deleteBookPhoto(@PathVariable("isbn") final String isbn) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        var book = bookService.findByIsbn(isbn);
        if(book.getPhoto() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        fileStorageService.deleteFile(book.getPhoto().getPhotoFile());
        bookService.removeBookPhoto(book.getIsbn(), bookMongoMapper.toMongo(book).getVersion());

        return ResponseEntity.ok().build();
    }

    @Operation(summary= "Gets a book photo")
    @GetMapping("/{isbn}/photo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getSpecificBookPhoto(@PathVariable("isbn") final String isbn){

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        Book book = bookService.findByIsbn(isbn);

        //In case the user has no photo, just return a 200 OK without body
        if(book.getPhoto() == null) {
            return ResponseEntity.ok().build();
        }

        String photoFile = book.getPhoto().getPhotoFile();
        byte[] image = fileStorageService.getFile(photoFile);
        String fileFormat = fileStorageService.getExtension(book.getPhoto().getPhotoFile()).orElseThrow(() -> new ValidationException("Unable to get file extension"));

        if(image == null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok().contentType(fileFormat.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG).body(image);

    }


    @Operation(summary = "Updates a specific Book")
    @PatchMapping(value = "/{isbn}")
    public ResponseEntity<BookView> updateBook(@PathVariable final String isbn,
                                               final WebRequest request,
                                               @Valid final UpdateBookRequest resource) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        final String ifMatchValue = request.getHeader(ConcurrencyService.IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty() || ifMatchValue.equals("null")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        MultipartFile file = resource.getPhoto();

        String fileName = fileStorageService.getRequestPhoto(file);

        if (fileName != null) {
            resource.setPhotoURI(fileName);
        }

        Book book;
        resource.setIsbn(isbn);
        try {
            book = bookService.update(resource, concurrencyService.getVersionFromIfMatchHeader(ifMatchValue));
        }catch (Exception e){
            throw new ConflictException("Could not update book: "+ e.getMessage());
        }
        return ResponseEntity.ok()
                .eTag(Long.toString(bookMongoMapper.toMongo(book).getVersion()))
                .body(bookViewMapper.toBookView(book));
    }

    @Operation(summary = "Gets Books by title or genre")
    @GetMapping
    public ListResponse<BookView> findBooks(@RequestParam(value = "title", required = false) final String title,
                                            @RequestParam(value = "genre", required = false) final String genre,
                                            @RequestParam(value = "authorName", required = false) final List<String> authorName) {

        //Este método, como está, faz uma junção 'OR'.
        //Para uma junção 'AND', ver o "/search"

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        List<Book> booksByTitle = null;
        if (title != null)
            booksByTitle = bookService.findByTitle(title);

        List<Book> booksByGenre = null;
        if (genre != null)
            booksByGenre = bookService.findByGenre(genre);

        List<Book> booksByAuthorName = null;
        if (authorName != null)
            booksByAuthorName = bookService.findByAuthorsIds(authorName);

        Set<Book> bookSet = new HashSet<>();
        if (booksByTitle!= null)
            bookSet.addAll(booksByTitle);
        if(booksByGenre != null)
            bookSet.addAll(booksByGenre);
        if(booksByAuthorName != null)
            bookSet.addAll(booksByAuthorName);

        List<Book> books = bookSet.stream()
                .sorted(Comparator.comparing(b -> b.getTitle().toString()))
                .collect(Collectors.toList());

        if(books.isEmpty())
            throw new NotFoundException("No books found with the provided criteria");

        return new ListResponse<>(bookViewMapper.toBookView(books));
    }

    @PostMapping("/search")
    public ListResponse<BookView> searchBooks(
            @RequestBody final SearchRequest<SearchBooksQuery> request) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }
        final var bookList = bookService.searchBooks(request.getPage(), request.getQuery());
        return new ListResponse<>(bookViewMapper.toBookView(bookList));
    }
}

