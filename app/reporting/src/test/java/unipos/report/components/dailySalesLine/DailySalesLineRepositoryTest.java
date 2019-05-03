package unipos.report.components.dailySalesLine;

import org.junit.After;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.report.test.config.MongoTestConfiguration;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by Thomas on 11.02.2016.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
public class DailySalesLineRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    DailySalesLineRepository dailySalesLineRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @After
    public void tearDown() {
        mongoTemplate.findAllAndRemove(Query.query(Criteria.where("_id").ne("-1")), DailySalesLine.class);
    }

    @Test
    public void testSave() {
        dailySalesLineRepository.save(new DailySalesLine());

        List<DailySalesLine> dailySalesLines = dailySalesLineRepository.findAll();
        assertThat(dailySalesLines, is(notNullValue()));
        assertThat(dailySalesLines.size(), is(1));
    }

    @Test
    public void testFindByDateBetween() {
        LocalDate date = LocalDate.now();
        DailySalesLine dailySalesLine1 = new DailySalesLine(date, new ArrayList<>(), new BigDecimal("0.0"), "storeGuid", "userId");
        DailySalesLine dailySalesLine2 = new DailySalesLine(date.minusMonths(1), new ArrayList<>(), new BigDecimal("0.0"), "storeGuid", "userId");
        DailySalesLine dailySalesLine3 = new DailySalesLine(date.plusMonths(2), new ArrayList<>(), new BigDecimal("0.0"), "storeGuid", "userId");

        dailySalesLineRepository.save(Arrays.asList(dailySalesLine1, dailySalesLine2, dailySalesLine3));

        List<DailySalesLine> dailySalesLines = dailySalesLineRepository.findByDateBetween(date.minusMonths(1).minusDays(1L), date.plusMonths(2).plusDays(1L));
        assertThat(dailySalesLines, is(notNullValue()));
        assertThat(dailySalesLines.size(), is(3));
    }

}