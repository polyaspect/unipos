package unipos.signature.components.umsatzZaehler;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.signature.config.MongoTestConfiguration;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by domin on 22.12.2016.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
public class UmsatzZaehlerRepositoryTest extends AbstractJUnit4SpringContextTests {


    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    UmsatzZaehlerRepository umsatzZaehlerRepository;

    @Autowired
    UmsatzZaehlerFixture umsatzZaehlerFixture;

    @Before
    public void setUp() throws Exception {
        umsatzZaehlerFixture.setUp();

        mongoTemplate.save(umsatzZaehlerFixture.umsatzZaehler1);
        mongoTemplate.save(umsatzZaehlerFixture.umsatzZaehler2);
        mongoTemplate.save(umsatzZaehlerFixture.umsatzZaehler3);

        assertThat(umsatzZaehlerRepository.count(), is(3L));
    }

    @After
    public void tearDown() throws Exception {
        mongoTemplate.findAllAndRemove(Query.query(where("_id").ne("-1")), UmsatzZaehler.class);
    }

    @Test
    public void Test_findFirstByCompanyIdOrderByCreationDateTimeDesc() throws Exception {
        //given
        String companyGuid = "storeGuid";

        //when
        UmsatzZaehler result = umsatzZaehlerRepository.findFirstByStoreGuidOrderByCreationDateTimeDesc(companyGuid);

        //then
        assertThat(result.getStoreGuid(), is(companyGuid));
        assertThat(result.getUmsatzZaehler(), is(new BigDecimal("150")));
        assertThat(result.getUmsatz(), is(new BigDecimal("50")));
        assertThat(result.getId(), is(umsatzZaehlerFixture.umsatzZaehler2.getId()));
        assertThat(result.getAutoIncrement(), is(4L));
        assertThat(result.getCreationDateTime(), is(notNullValue()));
    }

    @Test
    public void Test_saveSynchronised() throws Exception {
        //given
        UmsatzZaehler umsatzZaehler = new UmsatzZaehler();
        umsatzZaehler.setStoreGuid("meineTestCompany");
        umsatzZaehler.setCreationDateTime(LocalDateTime.now());
        umsatzZaehler.setAutoIncrement(100L);
        umsatzZaehler.setUmsatz(new BigDecimal("100"));
        umsatzZaehler.setUmsatzZaehler(new BigDecimal("100"));

        //when
        umsatzZaehlerRepository.saveSynchronised(umsatzZaehler);

        //then
        assertThat(umsatzZaehlerRepository.count(), is(4L));
    }
}
