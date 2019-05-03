package unipos.auth.components.user.usernamePassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import unipos.auth.components.user.User;
import unipos.auth.test.config.Fixture;

import java.util.Arrays;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Dominik on 01.06.2015.
 */
public class UsernamePasswordFixture implements Fixture {

    @Autowired
    MongoOperations mongoOperations;
    User dominik, joyce;
    UsernamePassword usernamePassword, usernamePassword1;

    @Override
    public void setUp() {
        dominik = new User("Dominik", "Schiener", true);
        usernamePassword = new UsernamePassword("Dominik", "asdf", dominik);

        joyce = new User("Joyce", "Kudum", false);
        usernamePassword1 = new UsernamePassword("Joyce", "asdf", joyce);

        mongoOperations.insertAll(Arrays.asList(dominik,joyce));
        mongoOperations.insertAll(Arrays.asList(usernamePassword,usernamePassword1));
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(query(where("name").ne("")), User.class);
        mongoOperations.findAllAndRemove(query(where("username").ne("")), UsernamePassword.class);
    }
}
