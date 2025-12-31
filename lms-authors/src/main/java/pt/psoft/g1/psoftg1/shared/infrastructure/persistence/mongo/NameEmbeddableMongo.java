package pt.psoft.g1.psoftg1.shared.infrastructure.persistence.mongo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
public class NameEmbeddableMongo {

    @Field("name")
    String name;

    public NameEmbeddableMongo(String name) {
        this.name = name;
    }
}
