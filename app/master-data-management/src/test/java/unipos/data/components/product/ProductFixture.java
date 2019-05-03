package unipos.data.components.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import unipos.data.components.category.Category;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateCategory;
import unipos.data.test.config.Fixture;
import unipos.data.test.config.MongoTestConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Dominik on 23.07.2015.
 */

@ContextConfiguration(classes={MongoTestConfiguration.class})
public class ProductFixture implements Fixture {

    @Autowired
    MongoOperations mongoOperations;

    Product product;

    @Override
    public void setUp() {
        product = Product.builder()
                .name("Red Bull")
                .description("Red Bull Racing Energy Drink 355ml")
                .number(1L)
                .price(new BigDecimal("1.59"))
                .guid("guid")
                .stores(Arrays.asList("storeGuid1", "storeGuid2", "storeGuid3"))
                .build();

        initDatabase();
    }

    private void initDatabase() {
        mongoOperations.insert(product);
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(query(where("id").ne("0")), Product.class);
    }
}
