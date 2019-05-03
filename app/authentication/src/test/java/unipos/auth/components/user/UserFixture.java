package unipos.auth.components.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Query;
import unipos.auth.test.config.Fixture;

import java.util.Arrays;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * @author Dominik
 */
public class UserFixture implements Fixture {

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void setUp() {
        User user1 = new User("dominik", "1234", false);
        User user2 = new User("gradnigger", "4321", false);

        mongoOperations.insertAll(Arrays.asList(user1,user2));
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(Query.query(where("name").ne("")), User.class);
    }
}
