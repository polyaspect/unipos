package unipos.data.components.taxRates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import unipos.data.test.config.Fixture;
import unipos.data.test.config.MongoTestConfiguration;

import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Dominik on 23.07.2015.
 */

@ContextConfiguration(classes={MongoTestConfiguration.class})
public class TaxRateFixture implements Fixture {

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public void setUp() {
        TaxRate taxRate = TaxRate.builder()
                .name("Bevarages")
                .guid(UUID.randomUUID().toString())
                .description("This is the USt. for Bevarages")
                .percentage(20)
                .taxRateCategory(TaxRateCategory.NORMAL)
                .build();
        TaxRate taxRate1 = TaxRate.builder()
                .description("This is the USt. for Entertainment")
                .name("Entertainment")
                .taxRateCategory(TaxRateCategory.NORMAL)
                .percentage(10)
                .guid(UUID.randomUUID().toString())
                .build();
        mongoOperations.insert(taxRate);
        mongoOperations.insert(taxRate1);
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(query(where("id").ne("0")), TaxRate.class);
    }
}
