package pt.psoft.g1.psoftg1.authormanagement.api.command;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.command.AuthorCommandService;
import pt.psoft.g1.psoftg1.authormanagement.services.query.AuthorQueryService;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

@Tag(name = "Author", description = "Endpoints for managing Authors")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorCommandController {

    private final AuthorQueryService authorQueryService;
    private final AuthorCommandService authorCommandService;
    private final AuthorViewMapper authorViewMapper;
    private final FileStorageService fileStorageService;
    private final ConcurrencyService concurrencyService;

    @Value("${feature.maintenance.killswitch}")
    private boolean isKilled;

    @Operation(summary = "Creates a new Author")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AuthorView> create(@Valid CreateAuthorRequest resource) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        //Guarantee that the client doesn't provide a link on the body, null = no photo or error
        resource.setPhotoURI(null);
        MultipartFile file = resource.getPhoto();

        String fileName = this.fileStorageService.getRequestPhoto(file);

        if (fileName != null) {
            resource.setPhotoURI(fileName);
        }

        final var author = authorCommandService.create(resource);

        final var newauthorUri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .build().toUri();

        return ResponseEntity.created(newauthorUri)
                .eTag(Long.toString(author.getVersion()))
                .body(authorViewMapper.toAuthorView(author));
    }

    // Update
    @Operation(summary = "Updates a specific author")
    @PatchMapping(value = "/{authorNumber}")
    public ResponseEntity<AuthorView> partialUpdate(
            @PathVariable("authorNumber") @Parameter(description = "The author ID") String authorNumber,
            WebRequest request,
            @Valid UpdateAuthorRequest updateRequest
    ) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        String ifMatch = request.getHeader(ConcurrencyService.IF_MATCH);
        if (ifMatch == null || ifMatch.isEmpty() || ifMatch.equals("null")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        MultipartFile file = updateRequest.getPhoto();
        String fileName = this.fileStorageService.getRequestPhoto(file);
        if (fileName != null) updateRequest.setPhotoURI(fileName);

        Author updatedAuthor = authorCommandService.partialUpdate(
                authorNumber, updateRequest, concurrencyService.getVersionFromIfMatchHeader(ifMatch));

        return ResponseEntity.ok()
                .eTag(Long.toString(updatedAuthor.getVersion()))
                .body(authorViewMapper.toAuthorView(updatedAuthor));
    }

    // Delete a foto
    @Operation(summary = "Deletes a author photo")
    @DeleteMapping("/{authorNumber}/photo")
    public ResponseEntity<Void> deleteBookPhoto(@PathVariable("authorNumber") final String authorNumber) {


        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        Author author = authorQueryService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new AccessDeniedException("Author not found"));

        if (author.getPhoto() == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        fileStorageService.deleteFile(author.getPhoto().getPhotoFile());
        authorCommandService.removeAuthorPhoto(author.getAuthorNumber(), author.getVersion());
        return ResponseEntity.ok().build();
    }
}
