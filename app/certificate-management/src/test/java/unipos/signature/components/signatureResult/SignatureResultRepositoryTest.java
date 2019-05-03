package unipos.signature.components.signatureResult;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.integritySafeGuard.domain.SignatureResult;
import unipos.signature.config.MongoTestConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by domin on 21.12.2016.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
public class SignatureResultRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    SignatureResultRepository signatureResultRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    SignatureResultFixture fixture;


    @Before
    public void setUp() throws Exception {
        fixture.setUp();

        mongoTemplate.save(fixture.base1);
        mongoTemplate.save(fixture.base2);
        mongoTemplate.save(fixture.base3);
        mongoTemplate.save(fixture.base4);
    }

    @After
    public void tearDown() throws Exception {
        mongoTemplate.findAllAndRemove(Query.query(where("_id").ne("-1")), SignatureResult.class);
    }

    @Test
    public void Test_Save() throws Exception {
        assertThat(signatureResultRepository.count(), is(4L));

        signatureResultRepository.save(fixture.toInsert1);

        assertThat(signatureResultRepository.count(), is(5L));
    }

    @Test
    public void Test_findFirstByStoreGuidOrderByCreationDateDesc() throws Exception {
        SignatureResult result = signatureResultRepository.findFirstByStoreGuidOrderByCreationDateDesc("storeGuid");

        assertThat(result.getId(), is(fixture.base3.getId()));
    }
}
