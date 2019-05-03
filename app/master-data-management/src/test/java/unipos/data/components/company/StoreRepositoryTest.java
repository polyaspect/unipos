package unipos.data.components.company;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.CompanyRepository;
import unipos.data.components.company.model.Store;
import unipos.data.test.config.MongoTestConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by dominik on 04.09.15.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
public class StoreRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    CompanyFixture companyFixture;
    @Autowired
    StoreRepository storeRepository;

    @Before
    public void setUp() throws Exception {
        companyFixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        companyFixture.tearDown();
    }

    @Test
    public void testFindAllStores() throws Exception {
        List<Store> stores = storeRepository.findAll();
        assertThat(stores.size(), is(1));
        assertThat(stores.get(0), is(notNullValue()));
    }

    @Test
    public void testFindStoreByGuid() throws Exception {
        Store guidStore = storeRepository.findFirstByGuid(companyFixture.company.getStores().get(0).getGuid());
        assertThat(guidStore, is(notNullValue()));
        assertThat(guidStore, is(companyFixture.company.getStores().get(0)));
    }

    @Test
    public void testFindByControllerPlaced() throws Exception {
        List<Store> stores = storeRepository.findByControllerStore(true);

        assertThat(stores.size(), is(1));
        assertThat(storeRepository.findByControllerStore(false).size(), is(0));
    }

    @Test
    public void testDeleteByGuid() throws Exception {
        assertThat(storeRepository.findAll().size(), is(1));

        storeRepository.deleteByGuid(companyFixture.company.getStores().get(0).getGuid());
        assertThat(storeRepository.findAll().size(), is(0));
    }
}