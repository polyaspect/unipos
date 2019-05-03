package unipos.signature.components.umsatzZaehler;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import unipos.signature.components.sequence.SequenceId;
import unipos.signature.components.sequence.SequenceRepository;
import unipos.signature.config.FixtureConfiguration;
import unipos.signature.shared.AbstractServiceTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by domin on 22.12.2016.
 */
@ContextConfiguration(classes = FixtureConfiguration.class)
public class UmsatzZaehlerServiceTest extends AbstractServiceTest {

    @Mock
    UmsatzZaehlerRepository umsatzZaehlerRepository;
    @Mock
    SequenceRepository sequenceRepository;
    @InjectMocks
    UmsatzZaehlerServiceImpl umsatzZaehlerService = new UmsatzZaehlerServiceImpl();

    @Autowired
    UmsatzZaehlerFixture umsatzZaehlerFixture;

    @Before
    public void setUp() {
        super.setUp();
        umsatzZaehlerFixture.setUp();
    }

    @Test
    public void Test_getLatestUmsatzZaehlerForCompany_ValueExists() throws Exception {
        //given
        String companyId = "storeGuid";

        when(umsatzZaehlerRepository.findFirstByStoreGuidOrderByCreationDateTimeDesc(companyId)).thenReturn(umsatzZaehlerFixture.umsatzZaehler1);

        //when
        UmsatzZaehler result = umsatzZaehlerService.getLatestUmsatzZaehlerForStoreGuid(companyId);

        //then
        assertThat(result.getAutoIncrement(), is(umsatzZaehlerFixture.umsatzZaehler1.getAutoIncrement()));
        assertThat(result.getStoreGuid(), is(umsatzZaehlerFixture.umsatzZaehler1.getStoreGuid()));
        assertThat(result.getCreationDateTime(), is(umsatzZaehlerFixture.umsatzZaehler1.getCreationDateTime()));
        assertThat(result.getUmsatzZaehler(), is(umsatzZaehlerFixture.umsatzZaehler1.getUmsatzZaehler()));
        assertThat(result.getUmsatz(), is(umsatzZaehlerFixture.umsatzZaehler1.getUmsatz()));
    }

    @Test
    public void Test_getLatestUmsatzZaehlerForCompany_FirstForCompany() throws Exception {
        //given
        String companyId = "storeGuid";

        when(umsatzZaehlerRepository.findFirstByStoreGuidOrderByCreationDateTimeDesc(companyId)).thenReturn(null);

        //when
        UmsatzZaehler result = umsatzZaehlerService.getLatestUmsatzZaehlerForStoreGuid(companyId);

        //then
        assertThat(result.getAutoIncrement(), is(0L));
        assertThat(result.getStoreGuid(), is(companyId));
        assertThat(result.getCreationDateTime(), is(nullValue()));
        assertThat(result.getUmsatzZaehler(), is(new BigDecimal("0")));
        assertThat(result.getUmsatz(), is(new BigDecimal("0")));
    }

    @Test
    public void Test_saveUmsatzZaehler() throws Exception {
        //given
        String storeGuid = "storeGuid";
        UmsatzZaehler umsatzZaehler = new UmsatzZaehler();
        umsatzZaehler.setStoreGuid(storeGuid);
        umsatzZaehler.setUmsatz(new BigDecimal("70"));
        umsatzZaehler.setCreationDateTime(LocalDateTime.now());

        when(umsatzZaehlerRepository.findFirstByStoreGuidOrderByCreationDateTimeDesc(storeGuid)).thenReturn(umsatzZaehlerFixture.umsatzZaehler2);
        when(sequenceRepository.getNextSequenceId(eq(SequenceId.Name.UMSATZ_ZAEHLER.name() + "_" + umsatzZaehler.getStoreGuid()))).thenReturn(5L);
        when(umsatzZaehlerRepository.save(any(UmsatzZaehler.class))).thenReturn(null);

        //when
        umsatzZaehlerService.saveUmsatzZaehler(umsatzZaehler);

        //then
        verify(umsatzZaehlerRepository, times(1)).saveSynchronised(argThat(new ArgumentMatcher<UmsatzZaehler>() {
            @Override
            public boolean matches(Object o) {
                UmsatzZaehler u = (UmsatzZaehler)o;

                return u.getStoreGuid().equals(storeGuid) &&
                        u.getAutoIncrement() == 5L &&
                        u.getCreationDateTime() != null &&
                        u.getUmsatz().equals(new BigDecimal("70")) &&
                        u.getUmsatzZaehler().equals(new BigDecimal("220"));
            }
        }));
    }
}
