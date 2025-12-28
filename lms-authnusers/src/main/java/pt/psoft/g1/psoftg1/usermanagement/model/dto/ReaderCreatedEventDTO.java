package pt.psoft.g1.psoftg1.usermanagement.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReaderCreatedEventDTO {
    private String username;
    private String fullName;
}
