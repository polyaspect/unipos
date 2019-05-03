package unipos.pos.components.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import unipos.pos.test.config.Fixture;
import unipos.pos.test.config.MongoTestConfiguration;

import java.util.Arrays;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by dominik on 08.09.15.
 */

@ContextConfiguration(classes = MongoTestConfiguration.class)
public class SequenceFixture implements Fixture {

    @Autowired
    MongoTemplate mongoTemplate;

    SequenceId sequenceId1, sequenceId2;

    public void initEntities() {
        sequenceId1 = SequenceId.builder()
                .seq(0L)
                .id("INVOICE")
                .build();
        sequenceId2 = SequenceId.builder()
                .seq(0L)
                .id("ORDER")
                .build();
    }

    @Override
    public void setUp() {
//        initEntities();
//        Arrays.asList(sequenceId1,sequenceId2).forEach((x) -> mongoTemplate.save(x));
    }

    @Override
    public void tearDown() {
        mongoTemplate.findAllAndRemove(Query.query(where("_id").ne("-1")), SequenceId.class);
    }
}
