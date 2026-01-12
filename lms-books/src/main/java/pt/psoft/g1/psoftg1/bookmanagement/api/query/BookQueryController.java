package pt.psoft.g1.psoftg1.bookmanagement.api.query;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookView;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewMapper;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.BookMongoMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.query.BookQueryService;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.SearchBooksQuery;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Books", description = "Endpoints for managing Books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookQueryController {

    private final BookQueryService bookQueryService;
    private final FileStorageService fileStorageService;

    private final BookViewMapper bookViewMapper;
    private final BookMongoMapper bookMongoMapper;

    @Value("${feature.maintenance.killswitch}")
    private boolean isKilled;

    @Operation(summary = "Gets a specific Book by isbn")
    @GetMapping(value = "/{isbn}")
    public ResponseEntity<BookView> findByIsbn(@PathVariable final String isbn) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        final var book = bookQueryService.findByIsbn(isbn);

        BookView bookView = bookViewMapper.toBookView(book);

        return ResponseEntity.ok()
                .eTag(Long.toString(bookMongoMapper.toMongo(book).getVersion()))
                .body(bookView);
    }

    @Operation(summary= "Gets a book photo")
    @GetMapping("/{isbn}/photo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getSpecificBookPhoto(@PathVariable("isbn") final String isbn){

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        Book book = bookQueryService.findByIsbn(isbn);

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
            booksByTitle = bookQueryService.findByTitle(title);

        List<Book> booksByGenre = null;
        if (genre != null)
            booksByGenre = bookQueryService.findByGenre(genre);

        List<Book> booksByAuthorName = null;
        if (authorName != null)
            booksByAuthorName = bookQueryService.findByAuthorsIds(authorName);

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
        final var bookList = bookQueryService.searchBooks(request.getPage(), request.getQuery());
        return new ListResponse<>(bookViewMapper.toBookView(bookList));
    }

}
