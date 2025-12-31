package pt.psoft.g1.psoftg1.messassing;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.genremanagement.api.GenreTempCreatedEvent;
import pt.psoft.g1.psoftg1.genremanagement.api.TempGenre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.TempGenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;

@Component
@RequiredArgsConstructor
public class AuthorRabbitListener {

  //  private final GenreService genreService;

   /* @RabbitListener(queues = "author-temp-created-queue", autoStartup = "false")
    public void handleTempAuthorCreation(GenreTempCreatedEvent payload) throws Exception {
        genreService.createTempGenre(
                payload.getGenreName(),
                payload.getSagaId()
        );
    }*/
}
