package pt.psoft.g1.psoftg1.bookmanagement.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthorInnerRequest {
    private String id;
    private String name;
    private String bio;
}
