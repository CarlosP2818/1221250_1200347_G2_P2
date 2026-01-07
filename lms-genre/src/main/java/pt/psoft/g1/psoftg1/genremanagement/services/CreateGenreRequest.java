package pt.psoft.g1.psoftg1.genremanagement.services;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGenreRequest {

    @NonNull
    @NotBlank
    @Getter
    @Setter
    private String genreName;

    @Getter
    @Setter
    private String correlationId;
}
