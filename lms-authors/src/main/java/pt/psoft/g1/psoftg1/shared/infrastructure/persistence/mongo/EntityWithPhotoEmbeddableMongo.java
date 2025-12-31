package pt.psoft.g1.psoftg1.shared.infrastructure.persistence.mongo;

import com.mongodb.lang.Nullable;
import lombok.Getter;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Field;

import java.nio.file.InvalidPathException;

@Getter
public class EntityWithPhotoEmbeddableMongo {

    @Nullable
    @DBRef
    @Field("photo")
    protected String photo;

    // This method is used by the mapper in order to set the photo.
    public void setPhoto(String photoUri) {
        this.setPhotoInternal(photoUri);
    }

    protected void setPhotoInternal(String photoURI) {
        if (photoURI == null) {
            this.photo = null;
        } else {
            try {
                // If Path.of succeeds, we assume it's a valid path
                this.photo = photoURI;
            } catch (InvalidPathException e) {
                // Avoid invalid references to photos
                this.photo = null;
            }
        }
    }

    @Nullable
    public String getPhoto() {
        return photo;
    }
}
