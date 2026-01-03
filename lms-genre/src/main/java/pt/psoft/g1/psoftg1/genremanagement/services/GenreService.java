package pt.psoft.g1.psoftg1.genremanagement.services;

import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.persistence.mongo.OutboxEventMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;

import java.util.Optional;
import java.util.UUID;

public interface GenreService {
    Iterable<Genre> findAll();

    Genre save(Genre genre);

    Optional<Genre> findByString(String name);

    Optional<Genre> findByName(String name);

    OutboxEventMongo createTempGenre(String genre, UUID sagaId);

    Genre createGenre(Genre genre);


//    List<GenreBookCountDTO> findTopGenreByBooks();

//    List<GenreLendingsDTO> getAverageLendings(GetAverageLendingsQuery query, Page page);

//    List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre();

//    List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(String startDate, String endDate);
}
