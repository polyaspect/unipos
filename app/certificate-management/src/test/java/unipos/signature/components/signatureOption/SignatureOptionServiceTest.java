package unipos.signature.components.signatureOption;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import unipos.common.remote.data.model.company.Address;
import unipos.common.remote.data.model.company.Store;
import unipos.integritySafeGuard.domain.RksSuite;
import unipos.signature.shared.AbstractServiceTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by domin on 22.12.2016.
 */
public class SignatureOptionServiceTest extends AbstractServiceTest {

    @Mock
    SignatureOptionRepository signatureOptionRepository;

    @InjectMocks
    SignatureOptionServiceImpl signatureOptionService;

    @Autowired
    SignatureOptionFixture signatureOptionFixture;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        signatureOptionFixture.setUp();
    }

    @Test
    public void Test_saveSignatureOption() throws Exception {
        //given
        Store store = Store.builder()
                .address(Address.builder()
                        .city("Wien")
                        .country("Österreich")
                        .postCode(1130)
                        .street("Lafitegasse 26C")
                        .build())
                .closeHour(LocalDateTime.of(2016,12,22,23,0,0))
                .controllerStore(true)
                .guid("storeGuid")
                .name("MeinStore")
                .storeId(1L)
                .build();
        when(signatureOptionRepository.deleteByStoreGuid(store.getGuid())).thenReturn(1L);
        when(signatureOptionRepository.save(any(SignatureOption.class))).thenReturn(null);


        //when
        signatureOptionService.saveSignatureOption(signatureOptionFixture.signatureOption1);

        //then
        verify(signatureOptionRepository, times(1)).save(argThat(new ArgumentMatcher<SignatureOption>() {
            @Override
            public boolean matches(Object o) {
                SignatureOption s = (SignatureOption)o;
                return s.getKassaId().equals("1") &&
                        s.getCrtSerialNo().equals("12-34-56-78-90-12") &&
                        s.getRksSuite() == RksSuite.R1_AT2 &&
                        s.getSecretKeyPlainText().equals("test") &&
                        s.getStoreGuid().equals("storeGuid") &&
                        s.getTurnOverCounterLengthInBytes() == 5 &&
                        s.getSecretKey() != null;
            }
        }));
    }

    @Test
    public void Test_findAll() throws Exception {
        //given
        when(signatureOptionRepository.findAll()).thenReturn(Arrays.asList(signatureOptionFixture.signatureOption1, signatureOptionFixture.signatureOption2));

        //when
        List<SignatureOption> signatureOptionList = signatureOptionService.findAll();

        //then
        assertThat(signatureOptionList.size(), is(2));
        assertThat(signatureOptionList.get(0), is(signatureOptionFixture.signatureOption1));
        assertThat(signatureOptionList.get(1), is(signatureOptionFixture.signatureOption2));

    }

    @Test
    public void Test_findByStoreGuid() throws Exception {
        //given
        String storeId = "storeGuid";
        when(signatureOptionRepository.findFirstByStoreGuid(eq(storeId))).thenReturn(signatureOptionFixture.signatureOption1);

        //when
        SignatureOption result = signatureOptionRepository.findFirstByStoreGuid(storeId);

        //then
        assertThat(result, is(signatureOptionFixture.signatureOption1));
    }

    @Test
    public void Test_deleteByStoreGuid() throws Exception {
        //given
        String storeId = "storeGuid2";
        when(signatureOptionRepository.deleteByStoreGuid(storeId)).thenReturn(1L);

        //when
        signatureOptionService.deleteByStoreGuid(storeId);

        //then
        verify(signatureOptionRepository, times(1)).deleteByStoreGuid(eq(storeId));


    }
}
