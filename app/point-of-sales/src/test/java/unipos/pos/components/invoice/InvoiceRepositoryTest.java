package unipos.pos.components.invoice;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.common.remote.data.model.company.Store;
import unipos.pos.components.invoice.model.Invoice;
import unipos.pos.components.invoice.InvoiceRepository;
import unipos.pos.test.config.MongoTestConfiguration;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;

/**
 * Created by dominik on 03.09.15.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
public class InvoiceRepositoryTest extends AbstractJUnit4SpringContextTests{

    @Autowired
    InvoiceFixture invoiceFixture;
    @Autowired
    InvoiceRepository invoiceRepository;

    @Before
    public void setUp() throws Exception {
        invoiceFixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        invoiceFixture.tearDown();
    }

    @Test
    public void testSaveInvoice() throws Exception {
        Invoice invoice = Invoice.builder()
                .build();

        invoiceRepository.save(invoice);

        List<Invoice> invoiceList = invoiceRepository.findAll();
        assertThat(invoiceList, is(notNullValue()));
        assertThat(invoiceList.size(), is(1));
    }

    @Test
    public void testFindByCreationDateBetween() throws Exception {
        Invoice invoice1 = Invoice.builder().creationDate(LocalDateTime.of(2015,10,31,0,0,0)).store(Store.builder().guid(LocalDateTime.now().hashCode() + "").build()).build();
        Invoice invoice2 = Invoice.builder().creationDate(LocalDateTime.of(2015,10,10,0,0,0)).store(Store.builder().guid(LocalDateTime.now().hashCode() + "").build()).build();
        Invoice invoice3 = Invoice.builder().creationDate(LocalDateTime.of(2015,11,1,0,0,0)).store(Store.builder().guid(LocalDateTime.now().hashCode() + "").build()).build();
        Invoice invoice4 = Invoice.builder().creationDate(LocalDateTime.of(2015,9,25,0,0,0)).store(Store.builder().guid(LocalDateTime.now().hashCode() + "").build()).build();

        Arrays.asList(invoice1,invoice2,invoice3,invoice4).forEach(x -> invoiceRepository.save(x));

        List<Invoice> invoiceList = invoiceRepository.findByCreationDateBetween(LocalDateTime.of(2015,8,25,0,0,0), LocalDateTime.of(2015,10,11,0,0,0));
        assertThat(invoiceList, is(notNullValue()));
        assertThat(invoiceList.size(), is(2));
        assertTrue(invoiceList.contains(invoice4));

        List<Invoice> invoiceList2 = invoiceRepository.findByCreationDateBetween(LocalDateTime.of(2015, 10, 30, 0, 0, 0), LocalDateTime.now());
        assertThat(invoiceList2, is(notNullValue()));
        assertThat(invoiceList2.size(), is(2));
        assertTrue(invoiceList2.contains(invoice1));

        List<Invoice> invoiceList3 = invoiceRepository.findByCreationDateBetween(LocalDateTime.of(2015,9,1,0,0,0), LocalDateTime.now());
        assertThat(invoiceList3, is(notNullValue()));
        assertThat(invoiceList3.size(), is(4));
        assertTrue(invoiceList3.contains(invoice1));
        assertTrue(invoiceList3.contains(invoice2));
        assertTrue(invoiceList3.contains(invoice3));
        assertTrue(invoiceList3.contains(invoice4));

    }

}