package pt.psoft.g1.psoftg1.authormanagement.api.query;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorView;
import pt.psoft.g1.psoftg1.authormanagement.api.AuthorViewMapper;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.query.AuthorQueryService;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;

@Tag(name = "Author", description = "Endpoints for managing Authors")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/authors")
public class AuthorQueryController {

    private final AuthorQueryService authorQueryService;
    private final AuthorViewMapper authorViewMapper;
    private final FileStorageService fileStorageService;

    @Value("${feature.maintenance.killswitch}")
    private boolean isKilled;

    // Gets
    @Operation(summary = "Know an author’s detail given its author number")
    @GetMapping(value = "/{authorNumber}")
    public ResponseEntity<AuthorView> findByAuthorNumber(
            @PathVariable("authorNumber") @Parameter(description = "The number of the Author to find") final String authorNumber) {


        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        final var author = authorQueryService.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException(Author.class, authorNumber));

        return ResponseEntity.ok().eTag(Long.toString(author.getVersion())).body(authorViewMapper.toAuthorView(author));
    }

    @Operation(summary = "Search authors by name")
    @GetMapping
    public ListResponse<AuthorView> findByName(@RequestParam("name") final String name) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        final var authors = authorQueryService.findByName(name);
        return new ListResponse<>(authorViewMapper.toAuthorView(authors));
    }


    // get - Photo
    @Operation(summary = "Gets a author photo")
    @GetMapping("/{authorNumber}/photo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getSpecificAuthorPhoto(
            @PathVariable("authorNumber") @Parameter(description = "The number of the Author to find") final String authorNumber) {


        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        Author authorDetails = authorQueryService.findByAuthorNumber(authorNumber)
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

}
