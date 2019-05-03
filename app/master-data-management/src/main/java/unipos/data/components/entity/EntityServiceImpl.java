package unipos.data.components.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author ggradnig
 * 
 */
@Service
public class EntityServiceImpl implements EntityService
{
	@Autowired
	private EntityRepository entityRepository;

	public void createEntity(Entity entity)
	{
		entityRepository.save(entity);
	}

	public void deleteAllEntities()
	{
		entityRepository.deleteAll();
	}

	public List<Entity> findAllEntities()
	{
		return entityRepository.findAll();
}

}
