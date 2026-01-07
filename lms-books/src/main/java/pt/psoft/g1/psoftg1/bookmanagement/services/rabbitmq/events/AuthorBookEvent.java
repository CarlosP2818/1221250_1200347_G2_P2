package pt.psoft.g1.psoftg1.bookmanagement.services.rabbitmq.events;

import lombok.*;
import pt.psoft.g1.psoftg1.bookmanagement.services.AuthorInnerRequest;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorBookEvent {
    private List<AuthorInnerRequest> authors;
    private String correlationId;
}