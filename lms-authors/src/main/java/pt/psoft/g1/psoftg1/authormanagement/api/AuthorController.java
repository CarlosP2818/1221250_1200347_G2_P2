package pt.psoft.g1.psoftg1.authormanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorService;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Author", description = "Endpoints for managing Authors")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorViewMapper authorViewMapper;
    private final FileStorageService fileStorageService;
    private final ConcurrencyService concurrencyService;

    // Create
    @Operation(summary = "Creates a new Author")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<TempAuthor> create(@Valid CreateAuthorRequest resource) {
        UUID sagaId = UUID.randomUUID(); // id único para a saga
        TempAuthor temp = authorService.createTempAuthor(resource.getName(), resource.getBio(), sagaId);
        return ResponseEntity.status(HttpStatus.CREATED).body(temp);
    }

    // Update
    @Operation(summary = "Updates a specific author")
    @PatchMapping(value = "/{authorNumber}")
    public ResponseEntity<AuthorView> partialUpdate(
            @PathVariable("authorNumber") @Parameter(description = "The author ID") Long authorNumber,
            WebRequest request,
            @Valid UpdateAuthorRequest updateRequest
    ) {
        String ifMatch = request.getHeader(ConcurrencyService.IF_MATCH);
        if (ifMatch == null || ifMatch.isEmpty() || ifMatch.equals("null")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        MultipartFile file = updateRequest.getPhoto();
        String fileName = this.fileStorageService.getRequestPhoto(file);
        if (fileName != null) updateRequest.setPhotoURI(fileName);

        Author updatedAuthor = authorService.partialUpdate(
                authorNumber, updateRequest, concurrencyService.getVersionFromIfMatchHeader(ifMatch));

        return ResponseEntity.ok()
                .eTag(Long.toString(updatedAuthor.getVersion()))
                .body(authorViewMapper.toAuthorView(updatedAuthor));
    }

    // Gets
    @Operation(summary = "Know an author’s detail given its author number")
    @GetMapping(value = "/{authorNumber}")
    public ResponseEntity<AuthorView> findByAuthorNumber(
            @PathVariable("authorNumber") @Parameter(description = "The number of the Author to find") final Long authorNumber) {

        final var author = authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException(Author.class, authorNumber));

        return ResponseEntity.ok().eTag(Long.toString(author.getVersion())).body(authorViewMapper.toAuthorView(author));
    }

    @Operation(summary = "Search authors by name")
    @GetMapping
    public ListResponse<AuthorView> findByName(@RequestParam("name") final String name) {

        final var authors = authorService.findByName(name);
        return new ListResponse<>(authorViewMapper.toAuthorView(authors));
    }


    // get - Photo
    @Operation(summary = "Gets a author photo")
    @GetMapping("/{authorNumber}/photo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getSpecificAuthorPhoto(
            @PathVariable("authorNumber") @Parameter(description = "The number of the Author to find") final Long authorNumber) {

        Author authorDetails = authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException(Author.class, authorNumber));

        // In case the user has no photo, just return a 200 OK without body
        if (authorDetails.getPhoto() == null) {
            return ResponseEntity.ok().build();
        }

        String photoFile = authorDetails.getPhoto().getPhotoFile();
        byte[] image = this.fileStorageService.getFile(photoFile);
        String fileFormat = this.fileStorageService.getExtension(authorDetails.getPhoto().getPhotoFile())
                .orElseThrow(() -> new ValidationException("Unable to get file extension"));

        if (image == null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok().contentType(fileFormat.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG)
                .body(image);
    }

    // Delete a foto
    @Operation(summary = "Deletes a author photo")
    @DeleteMapping("/{authorNumber}/photo")
    public ResponseEntity<Void> deleteBookPhoto(@PathVariable("authorNumber") final Long authorNumber) {

        Author author = authorService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new AccessDeniedException("Author not found"));

        if (author.getPhoto() == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        fileStorageService.deleteFile(author.getPhoto().getPhotoFile());
        authorService.removeAuthorPhoto(author.getAuthorNumber(), author.getVersion());
        return ResponseEntity.ok().build();
    }
}
