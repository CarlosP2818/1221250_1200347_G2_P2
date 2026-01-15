package pt.psoft.g1.psoftg1.genremanagement.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.genremanagement.services.GetAverageLendingsQuery;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = GenreController.class, properties = "feature.maintenance.killswitch=false")
@AutoConfigureMockMvc
class GenreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GenreService genreService;

    @MockBean
    private GenreViewMapper genreViewMapper;

    @MockBean
    private JwtDecoder jwtDecoder;

    @BeforeEach
    void setUp() {
        Mockito.when(genreService.findTopGenreByBooks())
                .thenReturn(List.of());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getTop_shouldReturnNotFound_whenNoGenres() throws Exception {
        Mockito.when(genreService.findTopGenreByBooks())
                .thenReturn(List.of());

        mockMvc.perform(get("/api/genres/top5"))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getTop_shouldReturnOk_whenGenresExist() throws Exception {
        GenreBookCountDTO mockDto = Mockito.mock(GenreBookCountDTO.class);

        Mockito.when(genreService.findTopGenreByBooks())
                .thenReturn(List.of(mockDto));

        mockMvc.perform(get("/api/genres/top5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").exists());
    }

    @Test
    @WithMockUser(roles = "LIBRARIAN")
    void getAverageLendings_shouldReturnOk() throws Exception {
        GetAverageLendingsQuery query = new GetAverageLendingsQuery(2025, 12);

        SearchRequest<GetAverageLendingsQuery> request = new SearchRequest<>();
        request.setQuery(query);

        pt.psoft.g1.psoftg1.shared.services.Page page = new pt.psoft.g1.psoftg1.shared.services.Page();
        page.setNumber(1);
        page.setLimit(10);
        request.setPage(page);

        Mockito.when(genreService.getAverageLendings(any(GetAverageLendingsQuery.class), any(pt.psoft.g1.psoftg1.shared.services.Page.class)))
                .thenReturn(List.of());

        mockMvc.perform(post("/api/genres/avgLendingsPerGenre")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}