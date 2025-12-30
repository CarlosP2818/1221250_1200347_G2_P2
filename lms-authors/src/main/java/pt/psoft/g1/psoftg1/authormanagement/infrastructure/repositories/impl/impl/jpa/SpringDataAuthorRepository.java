package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.impl.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.persistence.jpa.AuthorJpa;

import java.util.List;
import java.util.Optional;

public interface SpringDataAuthorRepository extends JpaRepository<AuthorJpa, String> {
    Optional<AuthorJpa> findByAuthorNumber(String authorNumber);

    List<AuthorJpa> findByName_NameStartsWith(String name);

    List<AuthorJpa> findByName_Name(String name); // Queries that require Book/Lending mappings (top/co-authors) will be added once their JPA models exist.
}

