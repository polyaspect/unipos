package unipos.data.components.entity;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ggradnig
 * A simple MVC controller, that provides a REST interface for the class unipos.data.components.entity.Entity
 */

/**
 * The annotation @RestController tells Spring that this class is a controller, whose actions contain response bodies
 */
@RestController

/**
 * With @RequestMapping, the actions of these classes are mapped to the url "/entities".
 * The result will be HTTP with application-type JSON.
 */
@RequestMapping(value="/entities", produces=MediaType.APPLICATION_JSON_VALUE)
@Api(value="/entities")
public class EntityController
{
	/**
	 * The service that is used by the controller is injected by Springs IoC.
	 */
	@Autowired
	private EntityService entityService;

	/**
	 * This simple action will return a list of all entities in JSON-format.
	 */
	@RequestMapping(value = "", method = RequestMethod.GET)
	@ApiOperation(value="Find all entitites",
		response = Entity.class,
		responseContainer = "List")
	public List<Entity> findAll()
	{
		return entityService.findAllEntities();
	}
}
