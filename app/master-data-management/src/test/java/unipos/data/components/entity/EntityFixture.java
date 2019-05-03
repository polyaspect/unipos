package unipos.data.components.entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import unipos.data.test.config.Fixture;
import unipos.data.test.config.MongoTestConfiguration;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author ggradnig
 */

@ContextConfiguration(classes={MongoTestConfiguration.class})
public class EntityFixture implements Fixture{

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void setUp() {
        mongoOperations.insert(new Entity("entity"));
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(query(where("attribute").is("entity")), Entity.class);

    }
}
