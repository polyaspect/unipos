package unipos.pos.components.dailySettlement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.pos.test.config.MongoTestConfiguration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Created by Dominik on 22.01.2016.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
public class DailySettlementRepositoryTest extends AbstractJUnit4SpringContextTests {

    DailySettlement dailySettlement1, dailySettlement2, dailySettlement3, dailySettlement4;

    @Autowired
    DailySettlementRepository dailySettlementRepository;

    @Before
    public void setUp() throws Exception {
        dailySettlement1 = DailySettlement.builder()
                .guid(UUID.randomUUID().toString())
                .closed(true)
                .storeGuid("storeGuid1")
                .dateTime(LocalDateTime.of(1994, 11, 5, 0, 35))
                .build();

        dailySettlement2 = DailySettlement.builder()
                .guid(UUID.randomUUID().toString())
                .closed(true)
                .storeGuid("storeGuid1")
                .dateTime(LocalDateTime.of(1994, 11, 6, 0, 35))
                .build();

        dailySettlement3 = DailySettlement.builder()
                .guid(UUID.randomUUID().toString())
                .closed(false)
                .storeGuid("storeGuid1")
                .dateTime(LocalDateTime.of(1994, 11, 7, 0, 35))
                .build();

        dailySettlement4 = DailySettlement.builder()
                .guid(UUID.randomUUID().toString())
                .closed(true)
                .storeGuid("storeGuid2")
                .dateTime(LocalDateTime.of(1994, 11, 5, 0, 35))
                .build();

        Arrays.asList(dailySettlement1, dailySettlement2, dailySettlement3,dailySettlement4).stream().forEach(x -> dailySettlementRepository.save(x));
    }

    @Test
    public void testFindLastByStoreGuid() throws Exception {
        DailySettlement dailySettlement = dailySettlementRepository.findLastByStoreGuid("storeGuid1", new Sort(new Sort.Order(Sort.Direction.DESC, "dateTime")));

        assertThat(dailySettlement, is(dailySettlement3));
    }

    @Test
    public void testFindLastByStoreGuidOrderByDateTime() throws Exception {
        DailySettlement dailySettlement = dailySettlementRepository.findLastByStoreGuidOrderByDateTimeDesc("storeGuid1");

        assertThat(dailySettlement, is(dailySettlement3));
    }

    @Test
    public void testFindByStoreGuid() throws Exception {
        assertThat(dailySettlementRepository.findByStoreGuid("storeGuid1").size(), is(3));
        assertThat(dailySettlementRepository.findByStoreGuid("storeGuid2").size(), is(1));
    }

    @After
    public void tearDown() throws Exception {
        dailySettlementRepository.deleteAll();

    }
}
