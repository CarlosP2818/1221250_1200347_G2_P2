package pt.psoft.g1.psoftg1.authormanagement.services.rabbitmq.events;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorCreatedEvent {
    private String name;
    private String bio;
    private MultipartFile photo;
    private String correlationId;
}

