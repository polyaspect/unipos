package unipos.auth.components.role;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by Dominik on 28.05.2015.
 */
public interface RoleRepository extends MongoRepository<Role, String> {

}
