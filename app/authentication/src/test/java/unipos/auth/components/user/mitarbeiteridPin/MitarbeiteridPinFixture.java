package unipos.auth.components.user.mitarbeiteridPin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import unipos.auth.components.user.User;
import unipos.auth.components.user.usernamePassword.UsernamePassword;
import unipos.auth.test.config.Fixture;

import java.util.Arrays;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Dominik on 01.06.2015.
 */
public class MitarbeiteridPinFixture implements Fixture {

    public User dominik, joyce;
    public MitarbeiteridPin mitarbeiteridPin, mitarbeiteridPin1;

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public void setUp() {
        dominik = new User("Dominik", "Schiener", true);
        mitarbeiteridPin = new MitarbeiteridPin(1234, 4321, dominik);

        joyce = new User("Joyce", "Kudum", false);
        mitarbeiteridPin1 = new MitarbeiteridPin(4321, 1234, joyce);

        mongoOperations.insertAll(Arrays.asList(dominik,joyce));
        mongoOperations.insertAll(Arrays.asList(mitarbeiteridPin, mitarbeiteridPin1));
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(query(where("name").ne("")), User.class);
        mongoOperations.findAllAndRemove(query(where("mitarbeiterid").ne("")), MitarbeiteridPin.class);
    }
}
