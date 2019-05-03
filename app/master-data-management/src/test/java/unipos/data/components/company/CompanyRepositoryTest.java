package unipos.data.components.company;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.CompanyRepository;
import unipos.data.test.config.MongoTestConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by dominik on 04.09.15.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
public class CompanyRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    CompanyFixture companyFixture;
    @Autowired
    CompanyRepository companyRepository;

    @Before
    public void setUp() throws Exception {
        companyFixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        companyFixture.tearDown();
    }

    @Test
    public void testfindCompany() throws Exception {
        Company company = companyRepository.findOne(companyFixture.company.getId());
        assertThat(company.getStores().get(0), is(notNullValue()));
        assertThat(company.getStores().get(0).getAddress(), is(notNullValue()));
        assertThat(company, is(companyFixture.company));
    }
}