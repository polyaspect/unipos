package unipos.data.components.product;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unipos.data.test.config.MongoTestConfiguration;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Created by Dominik on 23.07.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MongoTestConfiguration.class})
public class ProductRepositoryTest {
    @Autowired
    ProductFixture productFixture;

    @Autowired
    ProductRepository productRepository;

    @Before
    public void setUp() throws Exception {
        productFixture.setUp();
    }

    @Test
    public void testSaveProduct() throws Exception {
        Product product = new Product("asdf", "asdf", new BigDecimal("12.34"));
        productRepository.save(product);
        assertThat(product.getId(), is(notNullValue()));
    }

    @Test
    public void testSaveProductWithCategoryIsNull() throws Exception {
        Product product = Product.builder()
                .id("")
                .name("Joyce")
                .description("Joyce Kutumthanamkuzhy")
                .number(1L)
                .price(new BigDecimal("1234.99"))
                .category(null)
                .build();

        productRepository.save(product);

        product = productRepository.findOne(product.getId());
        assertThat(product.getName(), is("Joyce"));
    }

    @Test
    public void testListProducts() throws Exception {
        List<Product> products = productRepository.findAll();
        assertThat(products.size(), is(1));
        assertThat(products.get(0).getName(), is("Red Bull"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        Product product = productRepository.findAll().get(0);
        productRepository.delete(product);

        assertThat(productRepository.findAll().size(), is(0));
    }

    @Test
    public void testDeleteProductWithId() throws Exception {
        Product product = new Product("Red Bull", "Red Bull Racing", new BigDecimal("100000.00"));
        productRepository.save(product);
        assertThat(productRepository.findAll().size(), is(2));

        productRepository.delete(product.getId());
        assertThat(productRepository.findAll().size(), is(1));

    }

    @Test
    public void testDeleteByNumber() throws Exception {
        assertThat(productRepository.findAll().size(), is(1));
        productRepository.deleteByNumber(1L);
        assertThat(productRepository.findAll().size(), is(0));
    }

    @Test
    public void testFindByNumber() throws Exception {
        assertThat(productRepository.findByNumber(1L), is(notNullValue()));
        assertThat(productRepository.findByNumber(1L).getName(), is("Red Bull"));
        assertThat(productRepository.findByNumber(1L).getNumber(), is(1L));
    }

    @Test
    public void testFindOneById() throws Exception {
        Product product = productRepository.findOne(productRepository.findAll().get(0).getId());
        assertThat(product.getName(), is("Red Bull"));
        assertThat(product.getNumber(), is(1L));
    }

    @Test
    public void testFindByName() throws Exception {
        Product product = productRepository.findByName("Red Bull").get(0);

        assertThat(product.getName(), is("Red Bull"));
        assertThat(product.getDescription(), is("Red Bull Racing Energy Drink 355ml"));
        assertThat(product.getNumber(), is(1L));
        assertThat(product.getPrice(), is(new BigDecimal("1.59")));
    }

    @Test
    public void testFindByExistingNumberOrNotExistingName() throws Exception {
        Product product = productRepository.findByNumberOrNameLikeIgnoreCase(1, "asdf").get(0);

        assertThat(product.getName(), is("Red Bull"));
        assertThat(product.getDescription(), is("Red Bull Racing Energy Drink 355ml"));
        assertThat(product.getNumber(), is(1L));
        assertThat(product.getPrice(), is(new BigDecimal("1.59")));
    }

    @Test
    public void testFindByNotExistingNumberOrExistingName() throws Exception {
        Product product = productRepository.findByNumberOrNameLikeIgnoreCase(-1, "Red Bull").get(0);

        assertThat(product.getName(), is("Red Bull"));
        assertThat(product.getDescription(), is("Red Bull Racing Energy Drink 355ml"));
        assertThat(product.getNumber(), is(1L));
        assertThat(product.getPrice(), is(new BigDecimal("1.59")));
    }

    @Test
    public void testFindByExistingNumberOrExistingName() throws Exception {
        Product product = productRepository.findByNumberOrNameLikeIgnoreCase(1, "Red Bull").get(0);

        assertThat(product.getName(), is("Red Bull"));
        assertThat(product.getDescription(), is("Red Bull Racing Energy Drink 355ml"));
        assertThat(product.getNumber(), is(1L));
        assertThat(product.getPrice(), is(new BigDecimal("1.59")));
    }

    @Test
    public void testFindByNotExistingNumberOrExistingNameLike() throws Exception {
        Product product = productRepository.findByNumberOrNameLikeIgnoreCase(-1, "red").get(0);

        assertThat(product.getName(), is("Red Bull"));
        assertThat(product.getDescription(), is("Red Bull Racing Energy Drink 355ml"));
        assertThat(product.getNumber(), is(1L));
        assertThat(product.getPrice(), is(new BigDecimal("1.59")));

        product = productRepository.findByNumberOrNameLikeIgnoreCase(-1, "bull").get(0);

        assertThat(product.getName(), is("Red Bull"));
        assertThat(product.getDescription(), is("Red Bull Racing Energy Drink 355ml"));
        assertThat(product.getNumber(), is(1L));
        assertThat(product.getPrice(), is(new BigDecimal("1.59")));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindByNotExistingNumberOrNotExistingName() throws Exception {
        Product product = productRepository.findByNumberOrNameLikeIgnoreCase(-1, "lololol").get(0);

        assertThat(productRepository.findByNumberOrNameLikeIgnoreCase(-1, "asdfasdf").size(), is(0));
    }

    @Test
    public void testFindByNameLikeIgnoreCase() throws Exception {
        Product product = productRepository.findByNameLikeIgnoreCase("red").get(0);

        assertThat(product.getName(), is("Red Bull"));
        assertThat(product.getDescription(), is("Red Bull Racing Energy Drink 355ml"));
        assertThat(product.getNumber(), is(1L));
        assertThat(product.getPrice(), is(new BigDecimal("1.59")));
    }

    @Test
    public void testFindFirstByOrderByLastnameAsc() throws Exception {
        Product product = new Product(null, 666L, "HighestEntity","", new BigDecimal("666.66"), null);
        Product product2 = new Product(null, 333L, "HighestEntity","", new BigDecimal("666.66"), null);
        productRepository.save(product);
        productRepository.save(product2);

        product = null;
        product = productRepository.findFirstByOrderByNumberDesc();
        assertThat(product.getNumber(), is(666L));

    }

    @Test
    public void testFindFirstByGuid() throws Exception {
        Product product = productRepository.findFirstByGuid("guid");
        assertThat(product, is(notNullValue()));
        assertThat(product.getGuid(), is("guid"));
    }

    @Test
    public void testFindByStores() throws Exception {
        Product product = Product.builder()
                .name("asdf")
                .description("asdf")
                .number(2L)
                .price(new BigDecimal("2.99"))
                .guid("guid2")
                .stores(Arrays.asList("storeGuid3"))
                .build();
        productRepository.save(product);

        assertThat(productRepository.findByStoresContains("storeGuid1").size(), is(1));
        assertThat(productRepository.findByStoresContains("storeGuid3").size(), is(2));
        assertThat(productRepository.findByStoresContains("storeGuidasdfasdfasdf").size(), is(0));
    }

    @Test
    public void testFindByStoresArray() throws Exception {
        Product product = Product.builder()
                .name("asdf")
                .description("asdf")
                .number(2L)
                .price(new BigDecimal("2.99"))
                .guid("guid2")
                .stores(Arrays.asList("storeGuid3"))
                .build();
        productRepository.save(product);

        assertThat(productRepository.findAll().size(), is(2));

        assertThat(productRepository.findByStoresIn(Arrays.asList("storeGuid3", "storeGuid1")).size(), is(2));

        Product product2 = Product.builder()
                .name("lol")
                .description("lol")
                .number(3L)
                .price(new BigDecimal("2.99"))
                .guid("guid3")
                .stores(Arrays.asList("storeGuid2"))
                .build();
        productRepository.save(product2);

        Product product3 = Product.builder()
                .name("lol2")
                .description("lol2")
                .number(3L)
                .price(new BigDecimal("2.99"))
                .guid("guid4")
                .stores(Arrays.asList("storeGuid1", "storeGuid3"))
                .build();
        productRepository.save(product3);

        Product product4 = Product.builder()
                .name("lol")
                .description("lol")
                .number(3L)
                .price(new BigDecimal("2.99"))
                .guid("guid5")
                .stores(Arrays.asList("storeGuid2", "storeGuid3"))
                .build();
        productRepository.save(product4);

        assertThat(productRepository.findAll().size(), is(5));

        assertThat(productRepository.findByStoresIn(Collections.emptyList()).size(), is(0));
        assertThat(productRepository.findByStoresIn(Arrays.asList("storeGuid1")).size(), is(2));
        assertThat(productRepository.findByStoresIn(Arrays.asList("storeGuid2")).size(), is(3));
        assertThat(productRepository.findByStoresIn(Arrays.asList("storeGuid3")).size(), is(4));
        assertThat(productRepository.findByStoresIn(Arrays.asList("storeGuid1","storeGuid2")).size(), is(4));
        assertThat(productRepository.findByStoresIn(Arrays.asList("storeGuid1","storeGuid3")).size(), is(4));
        assertThat(productRepository.findByStoresIn(Arrays.asList("storeGuid3","storeGuid2")).size(), is(5));
        assertThat(productRepository.findByStoresIn(Arrays.asList("storeGuid1","storeGuid2", "storeGuid3")).size(), is(5));
    }

    @After
    public void tearDown() throws Exception {
        productFixture.tearDown();
    }
}