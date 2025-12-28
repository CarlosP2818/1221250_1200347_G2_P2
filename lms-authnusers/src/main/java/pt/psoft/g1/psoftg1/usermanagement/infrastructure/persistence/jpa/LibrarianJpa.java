package pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

@Entity
@DiscriminatorValue("LIBRARIAN")
public class LibrarianJpa  extends UserJpa {
    protected LibrarianJpa() {
        // for ORM only
    }
    public LibrarianJpa(String id,String username, String password) {
        super(id,username, password);
    }

    /**
     * factory method. since mapstruct does not handle protected/private setters
     * neither more than one public constructor, we use these factory methods for
     * helper creation scenarios
     *
     * @param username
     * @param password
     * @param name
     * @return
     */

    public static LibrarianJpa newLibrarian(String id,final String username, final String password, final String name) {
        final var u = new LibrarianJpa(id,username, password);
        u.setName(name);
        u.addAuthority(new Role(Role.LIBRARIAN));
        return u;
    }
}
