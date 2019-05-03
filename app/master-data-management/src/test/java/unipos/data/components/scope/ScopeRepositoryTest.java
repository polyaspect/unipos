package unipos.data.components.scope;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unipos.data.components.company.StoreRepository;
import unipos.data.components.company.model.Address;
import unipos.data.components.company.model.Store;
import unipos.data.components.product.Product;
import unipos.data.components.product.ProductRepository;
import unipos.data.test.config.MongoTestConfiguration;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Created by Dominik on 23.07.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MongoTestConfiguration.class})
public class ScopeRepositoryTest {
    @Autowired
    ScopeFixture scopeFixture;

    @Autowired
    ScopeRepository scopeRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    StoreRepository storeRepository;

    @Before
    public void setUp() throws Exception {
        scopeFixture.setUp();

        scopeRepository.save(Scope.builder()
                .product(scopeFixture.product)
                .store(scopeFixture.store)
                .build());

        scopeRepository.save(Scope.builder()
                .product(scopeFixture.product1)
                .store(scopeFixture.store)
                .build());

        scopeRepository.save(Scope.builder()
                .product(scopeFixture.product2)
                .store(scopeFixture.store1)
                .build());
    }

    @Test
    public void testFindByStore() throws Exception {
        assertThat(scopeRepository.findAll().size(), is(3));
        assertThat(scopeRepository.findByStore(scopeFixture.store).size(), is(2));
        assertThat(scopeRepository.findByStore(scopeFixture.store1).size(), is(1));
    }

    @Test
    public void testDeleteByStore() throws Exception {
        assertThat(scopeRepository.findAll().size(), is(3));
        assertThat(scopeRepository.deleteByStore(scopeFixture.store), is(2L));
        assertThat(scopeRepository.findAll().size(), is(1));
        assertThat(scopeRepository.deleteByStore(scopeFixture.store1), is(1L));
        assertThat(scopeRepository.findAll().size(), is(0));
    }

    @After
    public void tearDown() throws Exception {
        scopeFixture.tearDown();
    }
}