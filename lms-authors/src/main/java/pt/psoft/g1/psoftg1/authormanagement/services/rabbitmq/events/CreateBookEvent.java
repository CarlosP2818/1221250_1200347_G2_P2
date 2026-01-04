package pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.events;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorInnerRequest;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateBookEvent {
    private List<AuthorInnerRequest> authors;
    private String correlationId;
}