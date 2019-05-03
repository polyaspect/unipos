package unipos.data.components.scope;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import unipos.data.components.company.model.Address;
import unipos.data.components.company.model.Store;
import unipos.data.components.product.Product;
import unipos.data.test.config.Fixture;
import unipos.data.test.config.MongoTestConfiguration;

import java.math.BigDecimal;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * Created by Dominik on 23.07.2015.
 */

@ContextConfiguration(classes={MongoTestConfiguration.class})
public class ScopeFixture implements Fixture {

    @Autowired
    MongoOperations mongoOperations;

    Product product, product1, product2;
    Store store, store1;

    @Override
    public void setUp() {
        product = Product.builder()
                .name("Red Bull")
                .description("Red Bull Racing Energy Drink 355ml")
                .number(1L)
                .price(new BigDecimal("1.59"))
                .guid("guid")
                .build();

        product1 = Product.builder()
                .name("Fallout 4")
                .description("Fallout 4 Video Game")
                .number(2L)
                .price(new BigDecimal("59.99"))
                .guid("guid2")
                .build();

        product2 = Product.builder()
                .name("Fallout 3")
                .description("Fallout 3 Video Game")
                .number(2L)
                .price(new BigDecimal("19.99"))
                .guid("guid3")
                .build();

        store = Store.builder()
                .name("Store1")
                .address(Address.builder()
                        .city("Vienna")
                        .country("Austria")
                        .postCode(1100)
                        .street("Lafitegasse 26C")
                        .build())
                .companyGuid("unipos")
                .guid("guid")
                .storeId(1L)
                .build();

        store1 = Store.builder()
                .name("Store2")
                .address(Address.builder()
                        .city("Vienna")
                        .country("Austria")
                        .postCode(1130)
                        .street("Ceralegasse 8/11/8")
                        .build())
                .companyGuid("unipos2")
                .guid("guid2")
                .storeId(2L)
                .build();

        initDatabase();
    }

    private void initDatabase() {
        mongoOperations.insert(product);
        mongoOperations.insert(product1);
        mongoOperations.insert(product2);
        mongoOperations.insert(store);
        mongoOperations.insert(store1);
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(query(where("id").ne("0")), Scope.class);
        mongoOperations.findAllAndRemove(query(where("id").ne("0")), Product.class);
        mongoOperations.findAllAndRemove(query(where("id").ne("0")), Store.class);
    }
}
