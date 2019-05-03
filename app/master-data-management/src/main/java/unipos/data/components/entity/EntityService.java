package unipos.data.components.entity;

import java.util.List;

/**
 * @author ggradnig
 */
public interface EntityService {
    void createEntity(Entity entity);
    void deleteAllEntities();
    List<Entity> findAllEntities();
}
