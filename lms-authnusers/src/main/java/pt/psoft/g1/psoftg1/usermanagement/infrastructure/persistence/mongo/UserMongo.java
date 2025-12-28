package pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.mongo;

import org.springframework.data.annotation.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

import java.util.HashSet;
import java.util.Set;

@Document(collection = "users")
@Getter
@Setter
public class UserMongo implements UserDetails {

    @Id
    private String id;

    private boolean enabled=true;

    private String username;

    private String password;

    private String name;

    private Set<Role> authorities = new HashSet<>();

    public UserMongo() {
        // for ORM
    }

    public UserMongo(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public void setPassword(final String password) {
        final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public void addAuthority(final Role r) {
        authorities.add(r);
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }
}
