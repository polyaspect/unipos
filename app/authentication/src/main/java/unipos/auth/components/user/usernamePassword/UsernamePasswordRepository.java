package unipos.auth.components.user.usernamePassword;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.auth.components.user.User;

import java.util.List;

/**
 * Created by Dominik on 01.06.2015.
 */
public interface UsernamePasswordRepository extends MongoRepository<UsernamePassword, String> {
    UsernamePassword findByUsernameAndPassword(String username, String password);
    UsernamePassword findByUsername(String username);

    UsernamePassword findByGuid(String guid);

    Long deleteByGuid(String guid);

    List<UsernamePassword>findByUser(User user);
}
