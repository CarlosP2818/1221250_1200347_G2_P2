package pt.psoft.g1.psoftg1.usermanagement.infrastructure.repositories.impl.mongo;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.shared.services.IdGenerators.IdGenerator;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.mongo.ReaderMongo;
import pt.psoft.g1.psoftg1.usermanagement.infrastructure.persistence.mongo.UserMongo;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.User;

@Component
@Profile("mongo")
public class ReaderMongoMapper {

    private IdGenerator idGenerator;

    public ReaderMongoMapper(IdGenerator injectedIdGenerator) {
        this.idGenerator = injectedIdGenerator;
    }

    public User toDomain(UserMongo mongo) {
        if (mongo == null) return null;

        Reader reader = new Reader(mongo.getUsername(), mongo.getPassword());
        reader.setEnabled(mongo.isEnabled());

        if (mongo.getName() != null)
            reader.setName(mongo.getName());

        if (mongo.getAuthorities() != null && !mongo.getAuthorities().isEmpty())
            mongo.getAuthorities().forEach(reader::addAuthority);

        return reader;
    }

    public ReaderMongo toMongo(Reader reader) {
        if (reader == null) return null;

        ReaderMongo mongo = new ReaderMongo(idGenerator.generateId(), reader.getUsername(), reader.getPassword());
        mongo.setEnabled(reader.isEnabled());

        if (reader.getName() != null)
            mongo.setName(reader.getName().getName());

        if (reader.getAuthorities() != null && !reader.getAuthorities().isEmpty())
            reader.getAuthorities().forEach(mongo::addAuthority);

        return mongo;
    }
}
