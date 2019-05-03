package unipos.auth.components.user;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

/**
 * Created by Dominik on 27.05.2015.
 */

//ToDo: REVIEW STATE: NOT REVIEWED

public interface UserRepository extends MongoRepository<User, String> {
    User findByNameAndSurname(String firstname, String lastname);
    Long deleteByGuid(String guid);
    User findByUserId(Long userId);

    User findByGuid(String guid);
}
