package unipos.core.components.modules;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author ggradnig
 *
 * Spring repository for CRUD operations on Entity
 *
 */

public interface ModuleRepository extends MongoRepository<Module, String>
{
}
