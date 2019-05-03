package unipos.signature.components.umsatzZaehler;

import unipos.signature.config.Fixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Created by domin on 22.12.2016.
 */
public class UmsatzZaehlerFixture implements Fixture {

    public UmsatzZaehler umsatzZaehler1;
    public UmsatzZaehler umsatzZaehler2;
    public UmsatzZaehler umsatzZaehler3;

    @Override
    public void setUp() {
        umsatzZaehler1 = new UmsatzZaehler();
        umsatzZaehler1.setUmsatzZaehler(new BigDecimal("100"));
        umsatzZaehler1.setUmsatz(new BigDecimal("10"));
        umsatzZaehler1.setAutoIncrement(3L);
        umsatzZaehler1.setCreationDateTime(LocalDateTime.of(2016,12,22,5,15,24));
        umsatzZaehler1.setStoreGuid("storeGuid");

        umsatzZaehler2 = new UmsatzZaehler();
        umsatzZaehler2.setUmsatzZaehler(new BigDecimal("150"));
        umsatzZaehler2.setUmsatz(new BigDecimal("50"));
        umsatzZaehler2.setAutoIncrement(4L);
        umsatzZaehler2.setCreationDateTime(LocalDateTime.of(2016,12,23,5,15,24));
        umsatzZaehler2.setStoreGuid("storeGuid");

        umsatzZaehler3 = new UmsatzZaehler();
        umsatzZaehler3.setUmsatzZaehler(new BigDecimal("20"));
        umsatzZaehler3.setUmsatz(new BigDecimal("20"));
        umsatzZaehler3.setAutoIncrement(1L);
        umsatzZaehler3.setCreationDateTime(LocalDateTime.of(2016,12,24,5,15,24));
        umsatzZaehler3.setStoreGuid("companyGuid2");
    }

    @Override
    public void tearDown() {

    }
}
