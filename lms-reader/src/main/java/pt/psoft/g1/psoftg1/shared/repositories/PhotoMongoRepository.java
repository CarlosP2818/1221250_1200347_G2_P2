package pt.psoft.g1.psoftg1.shared.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("mongo")
public class PhotoMongoRepository implements PhotoRepository {

    private final MongoTemplate mongoTemplate;

    public PhotoMongoRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void deleteByPhotoFile(String photoFile) {
        mongoTemplate.remove(mongoTemplate.findById(photoFile, pt.psoft.g1.psoftg1.shared.model.Photo.class));
    }
}
