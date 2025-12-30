package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.LibrarianJpa;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;

import java.util.Optional;

@Component
public class LibrarianJpaMapper {

    private SpringDataUserRepository repo;
    private IdGenerator idGenerator;

    @Autowired
    public LibrarianJpaMapper(SpringDataUserRepository injectedRepo, IdGenerator injectedIdGenerator) {
        this.idGenerator = injectedIdGenerator;
        this.repo = injectedRepo;
    }

    public Librarian toDomain(LibrarianJpa jpa) {
        if (jpa == null) return null;
        Librarian reader = new Librarian(jpa.getUsername(), jpa.getPassword());
        reader.setName(jpa.getName().getName());
        jpa.getAuthorities().stream()
                .filter(Role.class::isInstance) // garante que é Role
                .map(Role.class::cast)          // faz o cast
                .forEach(reader::addAuthority);
        return reader;
    }

    public LibrarianJpa toJpa(Librarian librarian) {
        if (librarian == null) return null;

        // tenta buscar pelo username
        Optional<LibrarianJpa> existing = repo.findByUsername(librarian.getUsername())
                .map(LibrarianJpa.class::cast); // cast seguro

        return existing.orElseGet(() -> {
            // se não existe, cria novo
            LibrarianJpa jpa = new LibrarianJpa(idGenerator.generateId(), librarian.getUsername(), librarian.getPassword());
            jpa.setName(librarian.getName().getName());
            librarian.getAuthorities().forEach(jpa::addAuthority);
            return jpa;
        });
    }
}

