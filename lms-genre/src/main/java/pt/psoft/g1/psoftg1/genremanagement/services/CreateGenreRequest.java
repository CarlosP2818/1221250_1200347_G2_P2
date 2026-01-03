package pt.psoft.g1.psoftg1.genremanagement.services;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateGenreRequest {

    @Getter
    @Setter
    private String id;

    @NonNull
    @NotBlank
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private String correlationId;
}
