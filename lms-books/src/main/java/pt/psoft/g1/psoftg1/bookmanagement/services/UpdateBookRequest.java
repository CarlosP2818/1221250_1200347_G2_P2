package pt.psoft.g1.psoftg1.bookmanagement.services;

import jakarta.annotation.Nullable;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
public class UpdateBookRequest {
    @Setter
    private String isbn;

    @Setter
    private String description;

    private String title;

    @Nullable
    @Setter
    private String photoURI;

    @Nullable
    @Getter
    @Setter
    private MultipartFile photo;

    private String genreId;

    private List<String> authorsIds;

    public UpdateBookRequest(String isbn, String title, String genreId, List<String> authorsIds, String description) {
        this.isbn = isbn;
        this.title = title;
        this.genreId = genreId;
        this.authorsIds = authorsIds;
        this.description = description;
    }
}
