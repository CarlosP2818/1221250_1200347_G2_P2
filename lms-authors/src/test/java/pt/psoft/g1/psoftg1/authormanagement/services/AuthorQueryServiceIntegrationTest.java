package pt.psoft.g1.psoftg1.authormanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pt.psoft.g1.psoftg1.TestConfig;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.repositories.AuthorRepository;
import pt.psoft.g1.psoftg1.authormanagement.services.query.AuthorQueryServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.NONE,
        classes = {TestConfig.class},
        properties = {
                "stubrunner.amqp.mockConnection=true",
                "spring.profiles.active=test"
        }
)
@ExtendWith(SpringExtension.class)
class AuthorQueryServiceIntegrationTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AuthorQueryServiceImpl authorQueryService;

    @BeforeEach
    void setUp() {
        Author alex = new Author("Alex", "O Alex escreveu livros", null);

        List<Author> list = new ArrayList<>();
        list.add(alex);

        Mockito.when(authorRepository.searchByNameNameStartsWith("Alex"))
                .thenReturn(list);

        Mockito.when(authorRepository.findByAuthorNumber("1L"))
                .thenReturn(Optional.of(alex));
    }

    @Test
    void whenValidAuthorNumber_thenAuthorShouldBeFound() {
        Optional<Author> found = authorQueryService.findByAuthorNumber("1L");

        assertThat(found).isPresent();
        assertThat(found.get().getName()).isEqualTo("Alex");
    }

    @Test
    void whenNameProvided_thenAuthorsReturned() {
        var authors = authorQueryService.findByName("Alex");

        assertThat(authors).isNotNull();
        assertThat(authors.size()).isEqualTo(1);
    }
}