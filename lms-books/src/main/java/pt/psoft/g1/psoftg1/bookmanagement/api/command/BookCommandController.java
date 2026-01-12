package pt.psoft.g1.psoftg1.bookmanagement.api.command;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookView;
import pt.psoft.g1.psoftg1.bookmanagement.api.BookViewMapper;
import pt.psoft.g1.psoftg1.bookmanagement.infrastructure.repositories.impl.mongo.BookMongoMapper;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.services.command.BookCommandService;
import pt.psoft.g1.psoftg1.bookmanagement.services.query.BookQueryService;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.CreateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.dto.UpdateBookRequest;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.BookReplyListener;
import pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.Publisher;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import java.util.UUID;

@Tag(name = "Books", description = "Endpoints for managing Books")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookCommandController {

    private final BookQueryService bookQueryService;
    private final BookCommandService bookCommandService;
    private final FileStorageService fileStorageService;
    private final ConcurrencyService concurrencyService;
    private final Publisher publisher;
    private final BookReplyListener bookReplyListener;
    private final BookViewMapper bookViewMapper;
    private final BookMongoMapper bookMongoMapper;

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

        bookCommandService.createTemp(resource, null, correlationId);

        resource.setCorrelationId(correlationId);

        // 1. Guardar contexto local
        bookReplyListener.registerCreateBook(
                correlationId,
                resource
        );

        // 2. Publicar evento para criar Author
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
            book = bookCommandService.update(resource, concurrencyService.getVersionFromIfMatchHeader(ifMatchValue));
        }catch (Exception e){
            throw new ConflictException("Could not update book: "+ e.getMessage());
        }
        return ResponseEntity.ok()
                .eTag(Long.toString(bookMongoMapper.toMongo(book).getVersion()))
                .body(bookViewMapper.toBookView(book));
    }

    @Operation(summary = "Deletes a book photo")
    @DeleteMapping("/{isbn}/photo")
    public ResponseEntity<Void> deleteBookPhoto(@PathVariable("isbn") final String isbn) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        var book = bookQueryService.findByIsbn(isbn);
        if(book.getPhoto() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        fileStorageService.deleteFile(book.getPhoto().getPhotoFile());
        bookCommandService.removeBookPhoto(book.getIsbn(), bookMongoMapper.toMongo(book).getVersion());

        return ResponseEntity.ok().build();
    }

}
