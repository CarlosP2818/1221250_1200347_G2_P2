package pt.psoft.g1.psoftg1.authormanagement.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class AuthorDto {

    private String authorNumber;
    private String name;
    private String bio;
    private String photoURI;
    private UUID sagaId;
    private String status;
    private long version;
}
