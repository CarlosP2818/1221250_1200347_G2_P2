package pt.psoft.g1.psoftg1.usermanagement.model.dto;

import lombok.*;
import pt.psoft.g1.psoftg1.shared.model.Name;

import java.util.HashSet;
import java.util.Set;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String id;

    @Setter
    private boolean enabled = true;

    @Setter
    private String username;

    private String password;

    //	@Setter
    private Name name;

    private Set<RoleDto> authorities = new HashSet<>();
}
