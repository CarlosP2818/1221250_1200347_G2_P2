package pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

@Entity
@DiscriminatorValue("READER")
public class ReaderJpa extends UserJpa {
    protected ReaderJpa() {
        // for ORM only
    }
    public ReaderJpa(String id, String username, String password) {
        super(id,username, password);
        this.addAuthority(new Role(Role.READER));
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

    public static ReaderJpa newReader(String id ,final String username, final String password, final String name) {
        final var u = new ReaderJpa(id,username, password);
        u.setName(name);
        return u;
    }
}
