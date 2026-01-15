package pt.psoft.g1.psoftg1.genremanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class GenreServiceImplTest {

    @Mock
    private GenreRepository genreRepository;

    @InjectMocks
    private GenreServiceImpl genreService;

    @BeforeEach
    void setUp() {
        Mockito.when(genreRepository.findAll())
                .thenReturn(List.of(new Genre("Fiction")));
    }

    @Test
    void findAll_shouldReturnGenres() {
        Iterable<Genre> result = genreService.findAll();
        assertThat(result).isNotEmpty();
    }
}