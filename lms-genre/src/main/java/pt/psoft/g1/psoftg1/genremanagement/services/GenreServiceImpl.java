package pt.psoft.g1.psoftg1.genremanagement.services;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    private final GenreRepository genreRepository;
    private final PhotoRepository photoRepository;
    private final RabbitMQPublisher rabbitMQPublisher;

    @Autowired
    private RabbitTemplate template;

    @Autowired
    private DirectExchange direct;

    @Autowired
    private TempGenreRepository tempGenreRepository;

    public Optional<Genre> findByString(String name) {
        return genreRepository.findByString(name);
    }

    @Override
    public Iterable<Genre> findAll() {
        return genreRepository.findAll();
    }

//    @Override
//    public List<GenreBookCountDTO> findTopGenreByBooks() {
//        Pageable pageableRules = PageRequest.of(0, 5);
//        return this.genreRepository.findTop5GenreByBookCount(pageableRules).getContent();
//    }

    @Override
    public Optional<Genre> findByName(String name) {
        return genreRepository.findByString(name);
    }

    @Override
    public OutboxEventMongo createTempGenre(String genre, UUID sagaId) {

        Optional<OutboxEventMongo> existing = tempGenreRepository.findBySagaId(sagaId);
        if (existing.isPresent()) {
            return existing.get();
        }

        OutboxEventMongo temp = new OutboxEventMongo();
        temp.setGenre(genre);
        temp.setSagaId(sagaId);

        OutboxEventMongo saved = tempGenreRepository.save(temp);
        rabbitMQPublisher.publishTempGenreCreated(saved);

        return saved;
    }

    @Override
    public Genre createGenre(Genre genre) {

        Optional<Genre> existing = genreRepository.findByString(genre.getGenre());
        if (existing.isPresent()) {
            return existing.get();
        }

        Genre savedGenre = genreRepository.save(genre);
        rabbitMQPublisher.publishGenreCreated(savedGenre);
        return savedGenre;
    }

    @Override
    public Genre save(Genre genre) {
        return this.genreRepository.save(genre);
    }

//    @Override
//    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
//        return genreRepository.getLendingsPerMonthLastYearByGenre();
//    }

//    @Override
//    public List<GenreLendingsDTO> getAverageLendings(GetAverageLendingsQuery query, Page page) {
//        if (page == null)
//            page = new Page(1, 10);
//
//        final var month = LocalDate.of(query.getYear(), query.getMonth(), 1);
//
//        return genreRepository.getAverageLendingsInMonth(month, page);
//    }

//    @Override
//    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(String start, String end) {
//        LocalDate startDate;
//        LocalDate endDate;
//
//        try {
//            startDate = LocalDate.parse(start);
//            endDate = LocalDate.parse(end);
//        } catch (DateTimeParseException e) {
//            throw new IllegalArgumentException("Expected format is YYYY-MM-DD");
//        }
//
//        if (startDate.isAfter(endDate))
//            throw new IllegalArgumentException("Start date cannot be after end date");
//
//        final var list = genreRepository.getLendingsAverageDurationPerMonth(startDate, endDate);
//
//        if (list.isEmpty())
//            throw new NotFoundException("No objects match the provided criteria");
//
//        return list;
//    }
}
