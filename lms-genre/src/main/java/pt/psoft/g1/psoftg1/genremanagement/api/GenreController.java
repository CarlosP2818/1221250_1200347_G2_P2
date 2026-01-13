package pt.psoft.g1.psoftg1.genremanagement.api;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.genremanagement.services.GetAverageLendingsQuery;
import pt.psoft.g1.psoftg1.shared.api.ListResponse;
import pt.psoft.g1.psoftg1.shared.services.SearchRequest;

@Tag(name = "Genres", description = "Endpoints for managing Genres")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genres")
public class GenreController {
    private final GenreService genreService;
    private final GenreViewMapper genreViewMapper;

    @Value("${feature.maintenance.killswitch}")
    private boolean isKilled;

    @PostMapping(value="/avgLendingsPerGenre")
    public ListResponse<GenreLendingsView> getAverageLendings(
            @Valid @RequestBody final SearchRequest<GetAverageLendingsQuery> query){

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        final var list = genreService.getAverageLendings(query.getQuery(), query.getPage());
        return new ListResponse<>(genreViewMapper.toGenreAvgLendingsView(list));
    }

    @GetMapping("/top5")
    public ListResponse<GenreBookCountView> getTop() {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        System.out.println("ENTROU NO CONTROLLER");
        final var list = genreService.findTopGenreByBooks();

        if(list.isEmpty())
            throw new NotFoundException("No genres to show");

        return new ListResponse<>(genreViewMapper.toGenreBookCountView(list));
    }

    @GetMapping("/lendingsPerMonthLastTwelveMonths")
    public ListResponse<GenreLendingsCountPerMonthView> getLendingsPerMonthLastYearByGenre() {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        final var list = genreService.getLendingsPerMonthLastYearByGenre();

        if(list.isEmpty())
            throw new NotFoundException("No genres to show");

        final var viewList = genreViewMapper.toGenreLendingsCountPerMonthView(list);

        return new ListResponse<>(viewList);
    }

    @GetMapping("/lendingsAverageDurationPerMonth")
    public ListResponse<GenreLendingsAvgPerMonthView> getLendingsAverageDurationPerMonth(
            @RequestParam("startDate") final String start,
            @RequestParam("endDate") final String end) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        final var list = genreService.getLendingsAverageDurationPerMonth(start, end);

        if(list.isEmpty())
            throw new NotFoundException("No genres to show");

        final var viewList = genreViewMapper.toGenreLendingsAveragePerMonthView(list);

        return new ListResponse<>(viewList);
    }
}
