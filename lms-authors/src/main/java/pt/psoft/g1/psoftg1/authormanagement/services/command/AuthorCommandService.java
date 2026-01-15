package pt.psoft.g1.psoftg1.authormanagement.services.command;

import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.CreateAuthorRequest;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;

import java.util.Optional;
import java.util.UUID;

public interface AuthorCommandService {

    Author create(CreateAuthorRequest resource);

    Author partialUpdate(String authorNumber, UpdateAuthorRequest resource, long desiredVersion);

    Optional<Author> removeAuthorPhoto(String authorNumber, long desiredVersion);

    OutboxEventMongo createTempAuthor(String name, String bio, UUID sagaId);
}
