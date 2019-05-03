package unipos.auth.components.right;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Dominik on 28.05.2015.
 */
public interface RightRepository extends MongoRepository<Right, String> {
    Right findByName(String name);

    List<Right> findByGuidIn(List<String> rightGuids);
}
