package unipos.data.components.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import unipos.data.components.company.model.Address;
import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.Store;
import unipos.data.test.config.Fixture;

import java.util.Arrays;

/**
 * Created by dominik on 04.09.15.
 */
public class CompanyFixture implements Fixture {

    public Company company;

    @Autowired
    MongoTemplate mongoTemplate;

    private void init() {
        Store uniposStore = Store.builder()
                .name("Store1")
                .address(Address.builder()
                        .city("Vienna")
                        .country("Austria")
                        .postCode(1100)
                        .street("Lafitegasse 26C")
                        .build())
                .controllerStore(true)
                .build();

        mongoTemplate.save(uniposStore);

        company = Company.builder()
                .commercialRegisterNumber("43043G")
                .name("Red Bull")
                .uid("ATU0000000001")
                .stores(Arrays.asList(
                        uniposStore
                ))
                .build();
    }

    @Override
    public void setUp() {
        init();
        mongoTemplate.save(company);
    }

    @Override
    public void tearDown() {
        mongoTemplate.findAllAndRemove(Query.query(Criteria.where("_id").ne("-1")), Company.class);
        mongoTemplate.findAllAndRemove(Query.query(Criteria.where("_id").ne("-1")), Store.class);
    }
}
