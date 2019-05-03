package unipos.signature.components.signature;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import unipos.common.remote.pos.model.Invoice;
import unipos.signature.components.signatureOption.SignatureOptionFixture;
import unipos.signature.components.signatureOption.SignatureOptionService;
import unipos.signature.components.umsatzZaehler.UmsatzZaehler;
import unipos.signature.components.umsatzZaehler.UmsatzZaehlerService;
import unipos.signature.config.MongoTestConfiguration;
import unipos.signature.config.WebTestConfiguration;
import unipos.signature.shared.AbstractRestControllerTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by domin on 22.12.2016.
 */
@ContextConfiguration(classes = WebTestConfiguration.class)
public class SignatureControllerTest extends AbstractRestControllerTest {

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    SignatureFixture signatureFixture;
    @Autowired
    SignatureOptionFixture signatureOptionFixture;
    @Autowired
    UmsatzZaehlerService umsatzZaehlerService;
    @Autowired
    SignatureOptionService signatureOptionService;
    @Autowired
    SignatureService signatureService;


    @Before
    public void setUp() throws Exception {
        super.setup();
        signatureFixture.setUp();
        signatureOptionFixture.setUp();

        reset(umsatzZaehlerService);
        reset(signatureOptionService);
        reset(signatureService);
    }

    @Test
    public void Test_signInvoice() throws Exception {
        //given
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                UmsatzZaehler umsatzZaehler = invocationOnMock.getArgumentAt(0, UmsatzZaehler.class);
                umsatzZaehler.setUmsatzZaehler(new BigDecimal("165"));
                umsatzZaehler.setCreationDateTime(LocalDateTime.now());
                umsatzZaehler.setAutoIncrement(5L);
                return umsatzZaehler;
            }
        }).when(umsatzZaehlerService).saveUmsatzZaehler(any());
        when(signatureOptionService.findByStoreGuid(anyString())).thenReturn(signatureOptionFixture.signatureOption1);
        when(signatureService.signInvoice(any(Invoice.class))).thenReturn(signatureFixture.getInvoice1());

        //when
        MvcResult result = mockMvc.perform(post("/signatures/signInvoice")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(signatureFixture.getInvoice1()))
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        Invoice mappedResult = objectMapper.readValue(result.getResponse().getContentAsString(), Invoice.class);
    }

//    @Test
//    public void Test_createStartInvoice_Valid() throws Exception {
//        //given
//        Invoice invoice = signatureFixture.getInvoice1();
//        when(signatureService.createStartInvoice(any(Invoice.class))).thenReturn(invoice);
//
//        //when
//        MvcResult result = mockMvc.perform(post("/signatures/createStartInvoice")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaType.APPLICATION_JSON_UTF8)
//                .content(objectMapper.writeValueAsString(invoice)))
//                .andExpect(status().is2xxSuccessful())
//                .andReturn();
//
//        //then
//        verify(signatureService, times(1)).createStartInvoice(any());
//    }
//
//    @Test
//    public void Test_createStartInvoice_Invalid() throws Exception {
//        //given
//        Invoice invoice = signatureFixture.getInvoice1();
//
//        //when
//        MvcResult result = mockMvc.perform(post("/signatures/createStartInvoice")
//                .contentType(MediaType.APPLICATION_JSON_UTF8)
//                .accept(MediaType.APPLICATION_JSON_UTF8))
//                .andExpect(status().is5xxServerError())
//                .andReturn();
//    }
}
