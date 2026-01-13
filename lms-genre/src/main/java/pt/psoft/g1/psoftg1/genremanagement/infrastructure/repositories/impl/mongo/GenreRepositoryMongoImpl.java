package pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.impl.mongo;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import pt.psoft.g1.psoftg1.genremanagement.infrastructure.repositories.persistence.mongo.GenreMongo;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsPerMonthDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
@Profile("mongo")
public class GenreRepositoryMongoImpl implements GenreRepository {

    private final MongoTemplate mongoTemplate;

    private final GenreMongoMapper genreMongoMapper;

    @Override
    public Iterable<Genre> findAll() {
        List<GenreMongo> genres = mongoTemplate.findAll(GenreMongo.class);
        return genres.stream().map(genreMongoMapper::toDomain).collect(Collectors.toList());
    }

    @Override
    public Optional<Genre> findByString(String genreName) {
        Query query = new Query(Criteria.where("genre").is(genreName));
        GenreMongo genre = mongoTemplate.findOne(query, GenreMongo.class);
        return Optional.ofNullable(genre).map(genreMongoMapper::toDomain);    }

    @Override
    public Genre save(Genre genre) {
        GenreMongo genreMongo = genreMongoMapper.toMongo(genre);
        GenreMongo saved = mongoTemplate.save(genreMongo);
        return genreMongoMapper.toDomain(saved);
    }

    @Override
    public Page<GenreBookCountDTO> findTop5GenreByBookCount(Pageable pageable) {
        Aggregation agg = Aggregation.newAggregation(
                Aggregation.project("genre")
                        .and(ctx -> {
                            return new org.bson.Document("$size",
                                    new org.bson.Document("$ifNull", List.of("$books", List.of())));
                        }).as("bookCount"),

                Aggregation.sort(Sort.by(Sort.Direction.DESC, "bookCount")),

                Aggregation.limit(5)
        );

        AggregationResults<GenreBookCountDTO> results =
                mongoTemplate.aggregate(agg, "genre", GenreBookCountDTO.class);

        List<GenreBookCountDTO> dtos = results.getMappedResults();
        return new PageImpl<>(dtos, pageable, dtos.size());
    }

    @Override
    public List<GenreLendingsDTO> getAverageLendingsInMonth(LocalDate month, pt.psoft.g1.psoftg1.shared.services.Page page)
    {
        return List.of();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsPerMonthLastYearByGenre() {
        return List.of();
    }

    @Override
    public List<GenreLendingsPerMonthDTO> getLendingsAverageDurationPerMonth(LocalDate startDate, LocalDate endDate) {
        return List.of();
    }

    @Override
    public void delete(Genre genre) {
        Query query = new Query(Criteria.where("genre").is(genre.getGenre()));
        mongoTemplate.remove(query, GenreMongo.class);
    }
}
