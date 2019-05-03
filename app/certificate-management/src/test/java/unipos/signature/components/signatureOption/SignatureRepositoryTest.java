package unipos.signature.components.signatureOption;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.signature.config.MongoTestConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by domin on 22.12.2016.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
public class SignatureRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    SignatureOptionRepository signatureOptionRepository;
    @Autowired
    SignatureOptionFixture signatureOptionFixture;

    @Before
    public void setUp() throws Exception {
        signatureOptionFixture.setUp();

        mongoTemplate.save(signatureOptionFixture.signatureOption1);
        mongoTemplate.save(signatureOptionFixture.signatureOption2);
    }

    @After
    public void tearDown() throws Exception {
        mongoTemplate.findAllAndRemove(Query.query(where("_id").ne("-1")), SignatureOption.class);

    }

    @Test
    public void Test_findFirstByStoreGuid() throws Exception {
        //given
        String storeId = "storeGuid";

        //when
        SignatureOption result = signatureOptionRepository.findFirstByStoreGuid(storeId);

        //then
        assertThat(result, is(notNullValue()));
        assertThat(result, is(signatureOptionFixture.signatureOption1));
    }

    @Test
    public void Test_countByStoreGuid() throws Exception {
        //given
        String storeGuid = "storeGuid";

        //when
        Long result = signatureOptionRepository.countByStoreGuid(storeGuid);

        //then
        assertThat(result, is(1L));
        assertThat(signatureOptionRepository.countByStoreGuid("asdf"), is(0L));
    }

    @Test
    public void Test_deleteByStoreGuid() throws Exception {
        //given
        String storeGuid = "storeGuid";

        //when
        signatureOptionRepository.deleteByStoreGuid(storeGuid);
        Long remaining = signatureOptionRepository.count();

        //then
        assertThat(remaining, is(1L));
    }
}
