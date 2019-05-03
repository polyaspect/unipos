package unipos.data.components.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateCategory;
import unipos.data.test.config.Fixture;

import java.util.Arrays;
import java.util.UUID;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by dominik on 29.07.15.
 */
public class CategoryFixture implements Fixture {

    @Autowired
    MongoOperations mongoOperations;
    Category category, category1;

    @Override
    public void setUp() {
        TaxRate taxRate10 = new TaxRate("Entertainment", 10, TaxRateCategory.NORMAL);
        TaxRate taxRate20 = new TaxRate("Essen", 20, TaxRateCategory.NORMAL);
        mongoOperations.insertAll(Arrays.asList(taxRate10, taxRate20));


        category = Category.builder()
                .taxRate(taxRate20)
                .name("Schnitzel")
                .guid(UUID.randomUUID().toString())
                .build();
        category1 = Category.builder()
                .guid(UUID.randomUUID().toString())
                .taxRate(taxRate10)
                .build();
        mongoOperations.insertAll(Arrays.asList(category, category1));
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(query(where("id").ne("0")), Category.class);
        mongoOperations.findAllAndRemove(query(where("id").ne("0")), TaxRate.class);
    }
}
