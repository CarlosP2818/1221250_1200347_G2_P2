package pt.psoft.g1.psoftg1.authormanagement.services.query;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.authormanagement.api.BookShortView;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.repositories.OutboxEventRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.AuthorMapper;
import pt.psoft.g1.psoftg1.authormanagement.services.RabbitMQPublisher;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthorQueryServiceImpl implements AuthorQueryService {
    private final AuthorRepository authorRepository;

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
    public List<BookShortView> findBooksByAuthorNumber(String authorNumber) {
        return List.of();
    }

}
