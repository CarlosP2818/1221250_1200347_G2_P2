package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa;

import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.ReaderJpa;

import java.util.List;
import java.util.Optional;

@Repository
@Profile("sql")
public interface SpringDataReaderRepository extends JpaRepository<ReaderJpa, Long> {
        Optional<ReaderJpa> findByUsername(String username);
        List<ReaderJpa> findByNameName(String name);
}
