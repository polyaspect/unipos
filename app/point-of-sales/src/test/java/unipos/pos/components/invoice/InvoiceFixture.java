package unipos.pos.components.invoice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import unipos.pos.components.invoice.model.Invoice;
import unipos.pos.test.config.Fixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created by dominik on 03.09.15.
 */
public class InvoiceFixture implements Fixture {

    @Autowired
    MongoTemplate mongoTemplate;

    Invoice invoice1;

    private void init() {

    }

    @Override
    public void setUp() {
    }

    @Override
    public void tearDown() {
        mongoTemplate.findAllAndRemove(Query.query(Criteria.where("_id").ne("-1")), Invoice.class);
    }
}
