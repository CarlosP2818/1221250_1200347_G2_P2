package pt.psoft.g1.psoftg1.authormanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.api.BookShortView;
import pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.OutboxEventRepository;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final AuthorMapper mapper;
    private final PhotoRepository photoRepository;
    private final RabbitMQPublisher rabbitMQPublisher;

    private final OutboxEventRepository tempAuthorRepository;

    @Override
    public Iterable<Author> findAll() {
        return authorRepository.findAll();
    }

    @Override
    public Optional<Author> findByAuthorNumber(final String authorNumber) {
        return authorRepository.findByAuthorNumber(authorNumber);
    }

    @Override
    public List<Author> findByName(String name) {
        return authorRepository.searchByNameNameStartsWith(name);
    }

    @Override
    public Author partialUpdate(final String authorNumber, final UpdateAuthorRequest request, final long desiredVersion) {

        Author author = findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException("Cannot update an object that does not exist"));

        MultipartFile photo = request.getPhoto();
        String photoURI = request.getPhotoURI();
        if ((photo == null && photoURI != null) || (photo != null && photoURI == null)) {
            request.setPhoto(null);
            request.setPhotoURI(null);
        }

        author.applyPatch(desiredVersion, request);
        return authorRepository.save(author);

    }

    @Override
    public List<BookShortView> findBooksByAuthorNumber(String authorNumber) {
        return List.of();
    }

    @Override
    public OutboxEventMongo createTempAuthor(String name, String bio, UUID sagaId) {
        OutboxEventMongo tempAuthor = new OutboxEventMongo();
        tempAuthor.setName(name);
        tempAuthor.setBio(bio);
        tempAuthor.setCorrelationId(sagaId.toString());

        tempAuthorRepository.save(tempAuthor);

        // Envia evento de autor temporário criado para outros serviços (ex: BookService)
        rabbitMQPublisher.publishTempAuthorCreated(tempAuthor);

        return tempAuthor;
    }

    @Override
    public Author create(CreateAuthorRequest resource) {
        MultipartFile photo = resource.getPhoto();
        String photoURI = resource.getPhotoURI();
        if ((photo == null && photoURI != null) || (photo != null && photoURI == null)) {
            resource.setPhoto(null);
            resource.setPhotoURI(null);
        }
        Author author = mapper.create(resource);
        Author saved = authorRepository.save(author);

        rabbitMQPublisher.publishAuthorCreated(saved);

        return saved;
    }

    @Override
    public Optional<Author> removeAuthorPhoto(String authorNumber, long desiredVersion) {
        Author author = authorRepository.findByAuthorNumber(authorNumber)
                .orElseThrow(() -> new NotFoundException("Cannot find reader"));

        String photoFile = author.getPhoto().getPhotoFile();
        author.removePhoto(desiredVersion);
        Optional<Author> updatedAuthor = Optional.of(authorRepository.save(author));
        photoRepository.deleteByPhotoFile(photoFile);
        return updatedAuthor;
    }

}
