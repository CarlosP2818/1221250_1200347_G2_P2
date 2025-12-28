package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.jpa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.jpa.ReaderJpa;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

@Component
@Profile("sql")
public class ReaderJpaMapper {

    private SpringDataUserRepository repo;
    private SpringDataReaderRepository readerRepo;
    private IdGenerator idGenerator;

    @Autowired
    public ReaderJpaMapper(SpringDataUserRepository injectedRepo, SpringDataReaderRepository injectedReaderRepo, IdGenerator injectedIdGenerator) {
        this.idGenerator = injectedIdGenerator;
        this.repo = injectedRepo;
        this.readerRepo = injectedReaderRepo;
    }
    public Reader toDomain(ReaderJpa jpa) {
        if (jpa == null) return null;
        Reader reader = new Reader(jpa.getUsername(), jpa.getPassword());
        reader.setName(jpa.getName().getName());
        jpa.getAuthorities().forEach(reader::addAuthority);
        return reader;
    }

    public ReaderJpa toJpa(Reader reader) {
        if (reader == null) return null;

        return readerRepo.findByUsername(reader.getUsername())
                .orElseGet(() -> {
                    ReaderJpa jpa = new ReaderJpa(idGenerator.generateId(), reader.getUsername(), reader.getPassword());
                    jpa.setName(reader.getName().getName());
                    reader.getAuthorities().forEach(jpa::addAuthority);
                    return jpa;
                });
    }

}
