package pt.psoft.g1.psoftg1.genremanagement.api;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/genre")
public class GenreController {

    @Value("${feature.maintenance.killswitch}")
    private boolean isKilled;

    private final GenreService genreService;

    @RequestMapping("{name}")
    public ResponseEntity<Genre> findByName(@PathVariable String name) {

        if (isKilled) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "Funcionalidade desativada");
        }

        return genreService.findByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
