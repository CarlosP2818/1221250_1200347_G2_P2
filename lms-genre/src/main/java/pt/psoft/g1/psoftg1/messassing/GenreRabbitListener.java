package pt.psoft.g1.psoftg1.messassing;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreTempCreatedEvent;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

@Component
@RequiredArgsConstructor
public class GenreRabbitListener {

   private final GenreService genreService;

    @RabbitListener(queues = "genre-temp-created-queue", autoStartup = "false")
    public void handleTempGenreCreation(GenreTempCreatedEvent payload) throws Exception {
        genreService.createTempGenre(
                payload.getGenreName(),
                payload.getSagaId()
        );
    }
}
