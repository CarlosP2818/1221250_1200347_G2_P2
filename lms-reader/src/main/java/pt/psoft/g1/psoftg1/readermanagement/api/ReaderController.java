package pt.psoft.g1.psoftg1.readermanagement.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderCountView;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderQuoteView;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderView;
import pt.psoft.g1.psoftg1.readermanagement.api.ReaderViewMapper;
import pt.psoft.g1.psoftg1.readermanagement.model.Dto.CreateReaderRequestDto;
import pt.psoft.g1.psoftg1.readermanagement.model.Dto.RoleDto;
import pt.psoft.g1.psoftg1.readermanagement.model.Dto.UserDto;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.services.*;
import pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq.Publisher;
import pt.psoft.g1.psoftg1.readermanagement.services.rabbitmq.UserReplyListener;
import pt.psoft.g1.psoftg1.service.ApiNinjasService;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.ConcurrencyService;
import pt.psoft.g1.psoftg1.shared.services.FileStorageService;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Tag(name = "Readers", description = "Endpoints to manage readers")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/readers")
class ReaderController {
    private final ReaderService readerService;
    private final ReaderViewMapper readerViewMapper;
    private final ConcurrencyService concurrencyService;
    private final FileStorageService fileStorageService;
    private final Publisher publisher;
    private final ApiNinjasService apiNinjasService;
    private final UserReplyListener userReplyListener;

    @Operation(summary = "Gets the reader data if authenticated as Reader or all readers if authenticated as Librarian")
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            // Use the `array` property instead of `schema`
            array = @ArraySchema(schema = @Schema(implementation = ReaderView.class))) })
    @GetMapping
    public ResponseEntity<?> getData(Authentication authentication) {
        String username = authentication.getName();

        // Determine role from authentication authorities
        boolean isReader = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleDto.READER));

        if (isReader) {
            ReaderDetails readerDetails = readerService.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException(ReaderDetails.class, username));
            return ResponseEntity.ok().eTag(Long.toString(readerDetails.getVersion())).body(readerViewMapper.toReaderView(readerDetails));
        }

        return ResponseEntity.ok().body(readerViewMapper.toReaderView(readerService.findAll()));
    }

    @Operation(summary = "Gets reader by number")
    @ApiResponse(description = "Success", responseCode = "200", content = { @Content(mediaType = "application/json",
            // Use the `array` property instead of `schema`
            array = @ArraySchema(schema = @Schema(implementation = ReaderView.class))) })
    @GetMapping(value="/{year}/{seq}")
    //This is just for testing purposes, therefore admin role has been set
    //@RolesAllowed(Role.LIBRARIAN)
    public ResponseEntity<ReaderQuoteView> findByReaderNumber(@PathVariable("year")
                                                              @Parameter(description = "The year of the Reader to find")
                                                              final Integer year,
                                                              @PathVariable("seq")
                                                              @Parameter(description = "The sequencial of the Reader to find")
                                                              final Integer seq) {
        String readerNumber = year+"/"+seq;
        final var readerDetails = readerService.findByReaderNumber(readerNumber)
                .orElseThrow(() -> new NotFoundException("Could not find reader from specified reader number"));

        var readerQuoteView = readerViewMapper.toReaderQuoteView(readerDetails);

        int birthYear = readerDetails.getBirthDate().getBirthDate().getYear();
        int birhMonth = readerDetails.getBirthDate().getBirthDate().getMonthValue();

        readerQuoteView.setQuote(apiNinjasService.getRandomEventFromYearMonth(birthYear, birhMonth));

        return ResponseEntity.ok()
                .eTag(Long.toString(readerDetails.getVersion()))
                .body(readerQuoteView);
    }

    @Operation(summary = "Gets a list of Readers by phoneNumber")
    @GetMapping(params = "phoneNumber")
    public ListResponse<ReaderView> findByPhoneNumber(@RequestParam(name = "phoneNumber", required = false) final String phoneNumber) {

        List<ReaderDetails> readerDetailsList  = readerService.findByPhoneNumber(phoneNumber);

        if(readerDetailsList.isEmpty()) {
            throw new NotFoundException(ReaderDetails.class, phoneNumber);
        }

        return new ListResponse<>(readerViewMapper.toReaderView(readerDetailsList));
    }

    @Operation(summary= "Gets a reader photo")
    @GetMapping("/{year}/{seq}/photo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getSpecificReaderPhoto(@PathVariable("year")
                                                         @Parameter(description = "The year of the Reader to find")
                                                         final Integer year,
                                                         @PathVariable("seq")
                                                         @Parameter(description = "The sequencial of the Reader to find")
                                                         final Integer seq,
                                                         Authentication authentication) {
        String username = authentication.getName();

        boolean isLibrarian = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(RoleDto.LIBRARIAN));

        // if not librarian, ensure the requested reader number belongs to the logged-in reader
        if (!isLibrarian) {
            final var loggedReaderDetails = readerService.findByUsername(username)
                    .orElseThrow(() -> new NotFoundException(ReaderDetails.class, username));

            if (!loggedReaderDetails.getReaderNumber().equals(year + "/" + seq)) {
                throw new AccessDeniedException("Reader does not have permission to view another reader's photo");
            }
        }

        String requestedReaderNumber = year + "/" + seq;
        ReaderDetails readerDetails = readerService.findByReaderNumber(requestedReaderNumber)
                .orElseThrow(() -> new NotFoundException(ReaderDetails.class, requestedReaderNumber));

        //In case the user has no photo, just return a 404
        if(readerDetails.getPhoto() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        String photoFile = readerDetails.getPhoto().getPhotoFile();
        byte[] image = this.fileStorageService.getFile(photoFile);
        String fileFormat = this.fileStorageService.getExtension(readerDetails.getPhoto().getPhotoFile()).orElseThrow(() -> new ValidationException("Unable to get file extension"));

        if(image == null) {
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.ok().contentType(fileFormat.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG).body(image);
    }

    @Operation(summary= "Gets a reader photo")
    @GetMapping("/photo")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<byte[]> getReaderOwnPhoto(Authentication authentication) {

        String username = authentication.getName();

        Optional<ReaderDetails> optReaderDetails = readerService.findByUsername(username);
        if(optReaderDetails.isEmpty()) {
            throw new AccessDeniedException("Could not find a valid reader from current auth");
        }

        ReaderDetails readerDetails = optReaderDetails.get();

        //In case the user has no photo, just return a 404
        if(readerDetails.getPhoto() == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        byte[] image = this.fileStorageService.getFile(readerDetails.getPhoto().getPhotoFile());

        if(image == null) {
            return ResponseEntity.ok().build();
        }

        String fileFormat = this.fileStorageService.getExtension(readerDetails.getPhoto().getPhotoFile()).orElseThrow(() -> new ValidationException("Unable to get file extension"));

        return ResponseEntity.ok().contentType(fileFormat.equals("png") ? MediaType.IMAGE_PNG : MediaType.IMAGE_JPEG).body(image);
    }

    @Operation(summary = "Creates a reader")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ReaderView> createReader(@Valid @RequestBody CreateReaderRequest request) throws ValidationException {
        // Publica evento para criar usuário
        String correlationId = UUID.randomUUID().toString();

        // 1. Guardar contexto local
        userReplyListener.registerCreateReader(
                correlationId,
                request
        );

        // 2. Publicar evento para criar User
        publisher.sendCreateUserEvent(
                request,
                correlationId
        );

        // 3. Resposta assíncrona
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Deletes a reader photo")
    @DeleteMapping("/photo")
    public ResponseEntity<Void> deleteReaderPhoto(Authentication authentication) {
        String username = authentication.getName();

        Optional<ReaderDetails> optReaderDetails = readerService.findByUsername(username);
        if(optReaderDetails.isEmpty()) {
            throw new AccessDeniedException("Could not find a valid reader from current auth");
        }

        ReaderDetails readerDetails = optReaderDetails.get();

        if(readerDetails.getPhoto() == null) {
            throw new NotFoundException("Reader has no photo to delete");
        }

        this.fileStorageService.deleteFile(readerDetails.getPhoto().getPhotoFile());
        readerService.removeReaderPhoto(readerDetails.getReaderNumber(), readerDetails.getVersion());

        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Updates a reader")
    @RolesAllowed(RoleDto.READER)
    @PatchMapping
    public ResponseEntity<ReaderView> updateReader(
            @Valid UpdateReaderRequest readerRequest,
            Authentication authentication,
            final WebRequest request) {

        final String ifMatchValue = request.getHeader(ConcurrencyService.IF_MATCH);
        if (ifMatchValue == null || ifMatchValue.isEmpty() || ifMatchValue.equals("null")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "You must issue a conditional PATCH using 'if-match'");
        }

        MultipartFile file = readerRequest.getPhoto();

        String fileName = this.fileStorageService.getRequestPhoto(file);

        String username = authentication.getName();

        // find the reader details for the logged-in user and use its id for update
        ReaderDetails loggedReader = readerService.findByUsername(username)
                .orElseThrow(() -> new AccessDeniedException("Could not find a valid reader from current auth"));

        ReaderDetails readerDetails = readerService
                .update(loggedReader.getReader(), readerRequest, concurrencyService.getVersionFromIfMatchHeader(ifMatchValue), fileName);

        return ResponseEntity.ok()
                .eTag(Long.toString(readerDetails.getVersion()))
                .body(readerViewMapper.toReaderView(readerDetails));
    }

    @GetMapping("/top5")
    public ListResponse<ReaderView> getTop() {
        return new ListResponse<>(readerViewMapper.toReaderView(readerService.findTopReaders(5)));
    }

    @GetMapping("/top5ByGenre")
    public ListResponse<ReaderCountView> getTop5ReaderByGenre(
            @RequestParam("genre") String genre,
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate)
    {
        final var books = readerService.findTopByGenre(genre,startDate,endDate);

        if(books.isEmpty())
            throw new NotFoundException("No lendings found with provided parameters");

        return new ListResponse<>(readerViewMapper.toReaderCountViewList(books));
    }

    @PostMapping("/search")
    public ListResponse<ReaderView> searchReaders(
            @RequestBody final SearchRequest<SearchReadersQuery> request) {
        final var readerList = readerService.searchReaders(request.getPage(), request.getQuery());
        return new ListResponse<>(readerViewMapper.toReaderView(readerList));
    }
}