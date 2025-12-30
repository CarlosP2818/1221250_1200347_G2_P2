package pt.psoft.g1.psoftg1.authormanagement.infrastructure.repositories.impl.impl.jpa;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
@Profile("sql")
public class AuthorRepositoryJpaImpl implements AuthorRepository {
    private final SpringDataAuthorRepository jpaRepository;
    private final AuthorJpaMapper authorJpaMapper;

    @Override
    public Optional<Author> findByAuthorNumber(String authorNumber) {
        return jpaRepository.findByAuthorNumber(authorNumber).map(authorJpaMapper::toDomain);
    }

    @Override
    public List<Author> searchByNameNameStartsWith(String name) {
        return jpaRepository.findByName_NameStartsWith(name)
                .stream().map(authorJpaMapper::toDomain).toList();
    }

    @Override
    public List<Author> searchByNameName(String name) {
        return jpaRepository.findByName_Name(name)
                .stream().map(authorJpaMapper::toDomain).toList();
    }

    @Override
    public Author save(Author author) {
        final var saved = jpaRepository.save(authorJpaMapper.toJpa(author));
        return authorJpaMapper.toDomain(saved);
    }

    @Override
    public Iterable<Author> findAll() {
        return jpaRepository.findAll().stream().map(authorJpaMapper::toDomain).toList();
    }

    @Override
    public void delete(Author author) {
        if (author.getId() != null) {
            jpaRepository.deleteById(authorJpaMapper.toJpa(author).getAuthorNumber());
        }
    }
}
