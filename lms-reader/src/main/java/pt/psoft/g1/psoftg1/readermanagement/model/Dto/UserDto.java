package pt.psoft.g1.psoftg1.readermanagement.model.Dto;

import lombok.*;
import pt.psoft.g1.psoftg1.shared.model.Name;

import java.util.HashSet;
import java.util.Set;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    @Setter
    private String Id;

    @Setter
    private boolean enabled = true;

    @Setter
    private String username;

    private String password;

    //	@Setter
    private Name name;

    private final Set<RoleDto> authorities = new HashSet<>();
}
