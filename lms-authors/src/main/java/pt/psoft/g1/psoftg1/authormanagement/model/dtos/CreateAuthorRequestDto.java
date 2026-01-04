package pt.psoft.g1.psoftg1.authormanagement.model.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for creating an Author")
public class CreateAuthorRequestDto {

    @Size(min = 1, max = 150)
    private String name;

    @Size(min = 1, max = 4096)
    private String bio;

    @Nullable
    private String photoURI;

    private String correlationId;
}