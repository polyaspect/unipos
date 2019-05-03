package unipos.data.components.paymentMethods;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.components.paymentMethod.PaymentMethodRepository;
import unipos.data.test.config.MongoTestConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Created by dominik on 29.07.15.
 */

@ContextConfiguration(classes = MongoTestConfiguration.class)
public class PaymentMethodRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    @Autowired
    PaymentMethodFixture paymentMethodFixture;

    @Before
    public void setUp() throws Exception {
        paymentMethodFixture.setUp();
    }

    @Test
    public void testFindAll() throws Exception {
        List<PaymentMethod> categories = paymentMethodRepository.findAll();

        assertThat(categories.size(), is(2));
    }

    @Test
    public void testSave() {
        PaymentMethod paymentMethod = new PaymentMethod("VISA");
        assertThat(paymentMethod.getId(), is(nullValue()));

        paymentMethodRepository.save(paymentMethod);
        assertThat(paymentMethod.getId(), is(notNullValue()));
    }

    @Test
    public void testFindOne() {
        PaymentMethod paymentMethod = new PaymentMethod("MasterCard");
        paymentMethodRepository.save(paymentMethod);

        assertThat(paymentMethod.getId(), is(notNullValue()));
        paymentMethod = paymentMethodRepository.findOne(paymentMethod.getId());
        assertThat(paymentMethod, is(notNullValue()));
        assertThat(paymentMethod.getId(), is(notNullValue()));
        assertThat(paymentMethod.getName(), is("MasterCard"));
    }

    @Test
    public void testDeleteOneByMongoId() {
        PaymentMethod paymentMethod = new PaymentMethod("Karte");
        paymentMethodRepository.save(paymentMethod);

        assertThat(paymentMethod.getId(), is(notNullValue()));
        assertThat(paymentMethodRepository.findAll().size(), is(3));

        paymentMethodRepository.delete(paymentMethod.getId());
        assertThat(paymentMethodRepository.findAll().size(), is(2));
    }

    @Test
    public void testDeleteAllCategories() {
        assertThat(paymentMethodRepository.findAll().size(), is(2));
        paymentMethodRepository.deleteAll();
        assertThat(paymentMethodRepository.findAll().size(), is(0));
    }

    @Test
    public void testFindByName() {
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByNameLikeIgnoreCase("VISA");
        assertThat(paymentMethods.size(), is(1));
        assertThat(paymentMethods.get(0).getName(), is("VISA"));

        paymentMethods = paymentMethodRepository.findByNameLikeIgnoreCase("is");
        assertThat(paymentMethods.size(), is(1));
        assertThat(paymentMethods.get(0).getName(), is("VISA"));
    }

    @After
    public void tearDown() throws Exception {
        paymentMethodFixture.tearDown();
    }
}