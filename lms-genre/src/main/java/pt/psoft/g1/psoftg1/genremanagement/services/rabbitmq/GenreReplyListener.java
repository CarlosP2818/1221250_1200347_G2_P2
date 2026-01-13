package pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.mongo.MongoDataOutboxEventRepo;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.CreateGenreRequest;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreService;
import pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq.events.GenreCreatedEvent;
import pt.psoft.g1.psoftg1.genremanagement.services.rabbitmq.events.GenreFoundReply;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreReplyListener {

    private final RabbitTemplate rabbitTemplate;
    private final MongoDataOutboxEventRepo mongoDataOutboxEventRepo;
    private final GenreRepository genreRepository;

    @RabbitListener(queues = "genre.created.queue")
    public void receive(GenreCreatedEvent event) {

        List<OutboxEventMongo> savedTempGenres = new ArrayList<>();

        System.out.println("Genre:" + event);
        System.out.println("GENRE: Recebi CreateBookEvent, correlationId=" + event.getCorrelationId());

        if (event.getGenreName() == null || event.getGenreName().isBlank()) {
            System.err.println("GENRE: Nenhum nome de gênero no evento!");
            return;
        }

        OutboxEventMongo tempGenre = new OutboxEventMongo();
        tempGenre.setName(event.getGenreName());
        tempGenre.setCorrelationId(event.getCorrelationId());
        tempGenre.setProcessed(false);

        mongoDataOutboxEventRepo.save(tempGenre);
        savedTempGenres.add(tempGenre);

        genreRepository.save(new Genre(event.getGenreName()));

        // Enviar reply para o Book Service
        GenreFoundReply reply = new GenreFoundReply(event.getCorrelationId(), tempGenre.getName());
        rabbitTemplate.convertAndSend(
                "book.genre.replies.exchange",
                "genre.reply",
                reply
        );

        System.out.println("Genre: Reply enviado com sucesso!");

        finalizeTempAuthors(tempGenre);

    }

    private void finalizeTempAuthors(OutboxEventMongo genre) {
        genre.setProcessed(true);
        mongoDataOutboxEventRepo.save(genre);
        System.out.println("GENRE ENVIADO:" + genre);
        System.out.println("GENRE: Gênero temporário finalizado: " + genre.getName());
    }
}
