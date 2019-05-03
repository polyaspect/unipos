package unipos.design.components.screenCollection;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.design.components.screenCollection.model.ScreenCollection;

import java.util.List;

/**
 * Created by jolly on 10.12.2016.
 */
public interface ScreenCollectionRepository extends MongoRepository<ScreenCollection, String> {
    ScreenCollection findByNameAndPublished(String name, boolean published);
    List<ScreenCollection> findAllByPublished(boolean published);
    ScreenCollection findFirstByPublished(boolean published);
}
