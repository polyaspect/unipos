package unipos.data.components.entity;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * @author ggradnig
 *
 * A placeholder POJO that gets persisted to MongoDB
 *
 */

/**
 * Via the annotation @Data, all members are provided with getters/setters (lombok)
 */
@Data

/**
 * The @Document annotation declares that this class is a no-sql document in the collection "entities"
 */
@Document(collection = "entities")

@ApiModel(value = "entities")
public class Entity implements Serializable
{
	private static final long serialVersionUID = 1L;

    @Id
	private String id;

	@ApiModelProperty(value="attribute", required = false)
	private String attribute;
	
	public Entity()
	{
	}

	public Entity(String attribute)
	{
		this.attribute = attribute;
	}
}
