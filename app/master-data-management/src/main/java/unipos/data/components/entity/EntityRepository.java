package unipos.data.components.entity;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author ggradnig
 *
 * Spring repository for CRUD operations on Entity
 *
 */

public interface EntityRepository extends MongoRepository<Entity, String>
{
    Entity findByAttribute(String attribute);
}
