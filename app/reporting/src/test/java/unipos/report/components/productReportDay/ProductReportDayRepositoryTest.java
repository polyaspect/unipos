package unipos.report.components.productReportDay;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
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
 * Created by Thomas on 06.02.2016.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
public class ProductReportDayRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    ProductReportDayRepository productReportDayRepository;
    @Autowired
    MongoTemplate mongoTemplate;

    @After
    public void tearDown() {
        mongoTemplate.findAllAndRemove(Query.query(Criteria.where("_id").ne("-1")), ProductReportDay.class);
    }

    @Test
    public void testSave() {
        ProductReportDay productReportDay = new ProductReportDay();
        productReportDayRepository.save(productReportDay);

        List<ProductReportDay> productReportDays = productReportDayRepository.findAll();
        assertThat(productReportDays, is(notNullValue()));
        assertThat(productReportDays.size(), is(1));

    }

    @Test
    public void testFindByDateBetween() {
        LocalDate date = LocalDate.now();
        ProductReportDay productReportDay1 = new ProductReportDay(date, new ArrayList<>(), new BigDecimal("0.0"), "storeGuid", "userId");
        ProductReportDay productReportDay2 = new ProductReportDay(date.minusMonths(2), new ArrayList<>(), new BigDecimal("0.0"), "storeGuid", "userId");
        ProductReportDay productReportDay3 = new ProductReportDay(date.plusMonths(1), new ArrayList<>(), new BigDecimal("0.0"), "storeGuid", "userId");

        productReportDayRepository.save(Arrays.asList(productReportDay1, productReportDay2, productReportDay3));

        List<ProductReportDay> productReportDays = productReportDayRepository.findByDateBetween(date.minusMonths(2).minusDays(1L), date.plusMonths(1).plusDays(1L));
        assertThat(productReportDays, is(notNullValue()));
        assertThat(productReportDays.size(), is(3));
    }

}