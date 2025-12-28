package pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.mongo;

import pt.psoft.g1.psoftg1.usermanagement.model.Role;

public class ReaderMongo extends UserMongo{

    protected ReaderMongo() { super(); }

    public ReaderMongo(String id, String username, String password) {
        super(id, username, password);
        this.addAuthority(new Role(Role.READER));
    }

    public static ReaderMongo newReader(String id, final String username, final String password, final String name) {
        final var u = new ReaderMongo(id, username, password);
        u.setName(name);
        return u;
    }

}
