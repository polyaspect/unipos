package unipos.data.components.paymentMethods;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import unipos.data.components.category.Category;
import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.test.config.Fixture;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by dominik on 29.07.15.
 */
public class PaymentMethodFixture implements Fixture {

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public void setUp() {
        PaymentMethod paymentMethod = new PaymentMethod("Maesto");
        PaymentMethod paymentMethod1 = new PaymentMethod("VISA");
        mongoOperations.insert(paymentMethod);
        mongoOperations.insert(paymentMethod1);
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(query(where("id").ne("0")), PaymentMethod.class);
    }
}
