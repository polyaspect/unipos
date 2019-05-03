package unipos.printer.components.printer;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.printer.components.printer.model.Printer;
import unipos.printer.config.ExceptionDatabaseLogger;
import unipos.printer.test.config.MongoTestConfiguration;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dominik on 09.09.15.
*/


@ContextConfiguration(classes = MongoTestConfiguration.class)
public class PrinterRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    PrinterFixture printerFixture;
    @Autowired
    PrinterRepository printerRepository;

    @Before
    public void setUp() throws Exception {
        printerFixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        printerFixture.tearDown();
    }

    @Test
    public void testPrintQRCode() {
        Printer printer = printerFixture.printer1;
        try {
            printer.openConnection();

            String jws = "eyJ0eXAiOiJKV1QiLA0KICJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqb2UiLA0KICJleHAiOjEzMDA4MTkzODAsDQogImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ.dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk";
            printer.printQRCode(jws, 3);
            printer.cutPaper();

        } catch (Exception e) {

        } finally {
            try {
                printer.closeConnection();
            } catch (IOException ioe) {

            }
        }
    }

    @Test
    public void testPrintQRCodeWithZeroScale() {
        Printer printer = printerFixture.printer1;
        try {
            printer.openConnection();

            String jws = "eyJ0eXAiOiJKV1QiLA0KICJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJqb2UiLA0KICJleHAiOjEzMDA4MTkzODAsDQogImh0dHA6Ly9leGFtcGxlLmNvbS9pc19yb290Ijp0cnVlfQ.dBjftJeZ4CVP-mB92K27uhbUJU1p1r_wW1gFWFOEjXk";
            printer.printQRCode(jws, 0);
            printer.cutPaper();

        } catch (Exception e) {

        } finally {
            try {
                printer.closeConnection();
            } catch (IOException ioe) {

            }
        }
    }

    @Test
    public void testPrintQRCodeWithNullMessage() {
        Printer printer = printerFixture.printer1;
        try {
            printer.openConnection();

            printer.printQRCode(null, 3);
            printer.cutPaper();

        } catch (Exception e) {

        } finally {
            try {
                printer.closeConnection();
            } catch (IOException ioe) {

            }
        }
    }

    /*@Test
    public void testFindByIpAddress() throws Exception {
        Printer printer = printerRepository.findByPrinterId("192.168.0.10");
        assertThat(printer, is(notNullValue()));
        assertThat(printer.getIpAddress(), is("192.168.0.10"));
        assertThat(printer.getName(), is("Thekendrucker"));
        assertThat(printer.getTypeName(), is("Epson Drucker1"));
        assertThat(printer.getId(), is(notNullValue()));
    }

    @Test
    public void testUnknownPrinterReturnsNull() throws Exception {
        Printer printer = printerRepository.findFirstByIpAddress("");

        assertThat(printer, is(nullValue()));
    }*/
}
