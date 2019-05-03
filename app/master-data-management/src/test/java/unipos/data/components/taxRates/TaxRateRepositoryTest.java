package unipos.data.components.taxRates;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.data.test.config.MongoTestConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Created by dominik on 28.07.15.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
public class TaxRateRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    TaxRateFixture taxRateFixture;

    @Autowired
    TaxRateRepository taxRateRepository;

    @Before
    public void setUp() throws Exception {
        taxRateFixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        taxRateFixture.tearDown();
    }

    @Test
    public void testFindAll() {
        List<TaxRate> taxRates = taxRateRepository.findAll();

        assertThat(taxRates.size(), is(2));
        assertThat(taxRates.get(0).getPercentage(), is(20));
        assertThat(taxRates.get(0).getDescription(), is("This is the USt. for Bevarages"));
        assertThat(taxRates.get(1).getPercentage(), is(10));
        assertThat(taxRates.get(1).getDescription(), is("This is the USt. for Entertainment"));
    }

    @Test
    public void testDeleteAll() throws Exception {
        taxRateRepository.deleteAll();

        assertThat(taxRateRepository.findAll().size(), is(0));
    }

    @Test
    public void testSaveTaxRate() throws Exception {
        TaxRate taxRate = new TaxRate("Random Stuff",12, TaxRateCategory.DISCOUNT, "Random Stuff");
        taxRateRepository.save(taxRate);

        assertThat(taxRateRepository.findAll().size(), is(3));
        assertThat(taxRateRepository.findAll().get(2).getPercentage(), is(12));
        assertThat(taxRateRepository.findAll().get(2).getDescription(), is("Random Stuff"));
    }

    @Test
    public void testDeleteTaxRate() throws Exception {
        assertThat(taxRateRepository.findAll().size(), is(2));

        taxRateRepository.delete(taxRateRepository.findAll().get(0).getId());

        assertThat(taxRateRepository.findAll().size(), is(1));
    }

    @Test
    public void testDeleteByGuid() throws Exception {
        assertThat(taxRateRepository.findAll().size(), is(2));

        taxRateRepository.deleteByGuid(taxRateRepository.findAll().get(0).getGuid());

        assertThat(taxRateRepository.findAll().size(), is(1));
    }
}