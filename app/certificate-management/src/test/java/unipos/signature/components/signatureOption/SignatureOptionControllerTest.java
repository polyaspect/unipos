package unipos.signature.components.signatureOption;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MvcResult;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Address;
import unipos.common.remote.data.model.company.Store;
import unipos.integritySafeGuard.smartcard.SmartCardHandler;
import unipos.signature.config.WebTestConfiguration;
import unipos.signature.shared.AbstractRestControllerTest;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by domin on 22.12.2016.
 */
@ContextConfiguration(classes = WebTestConfiguration.class)
public class SignatureOptionControllerTest extends AbstractRestControllerTest {

    @Autowired
    SignatureOptionService signatureOptionService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    SmartCardHandler smartCardHandler;
    @Autowired
    SignatureOptionFixture signatureOptionFixture;
    ObjectMapper objectMapper = new ObjectMapper();


    @Before
    public void setUp() throws Exception {
        super.setup();
        signatureOptionFixture.setUp();
        reset(signatureOptionService);
        reset(dataRemoteInterface);
        reset(smartCardHandler);
    }

    @Test
    public void Test_findAll() throws Exception {
        //given
        when(signatureOptionService.findAll()).thenReturn(Arrays.asList(signatureOptionFixture.signatureOption1, signatureOptionFixture.signatureOption2));

        //when
        MvcResult result = mockMvc.perform(get("/signatureOptions")
                .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().is2xxSuccessful())
                .andReturn();


        List<SignatureOptionDto> dtos = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<SignatureOptionDto>>() {
        });

        //then
        assertThat(dtos.size(), is(2));
        assertThat(dtos.get(0), is(notNullValue()));
        assertThat(dtos.get(1), is(notNullValue()));
    }

    @Test
    public void Test_saveSignatureOption() throws Exception {
        //given
        Store store = Store.builder()
                .storeId(1L)
                .name("Unipos Store")
                .guid("storeGuid")
                .controllerStore(true)
                .address(Address.builder()
                        .street("Lafitegasse 26C")
                        .postCode(1130)
                        .country("Österreich")
                        .city("Wien")
                        .build())
                .closeHour(LocalDateTime.of(2016, 12, 22, 23, 0, 0))
                .companyGuid("storeGuid")
                .build();
        when(dataRemoteInterface.getStoreByAuthtokenAndDeviceid(Matchers.any(HttpServletRequest.class))).thenReturn(store);
        doNothing().when(signatureOptionService).saveSignatureOption(eq(signatureOptionFixture.signatureOption1));
        //when
        mockMvc.perform(post("/signatureOptions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(signatureOptionFixture.signatureOption1))
        ).andExpect(status().is2xxSuccessful());


        //then
        //verify(signatureOptionService, times(1)).saveSignatureOption(eq(signatureOptionFixture.signatureOption1));
    }

    @Test
    public void Test_saveSignatureOption_NoStoreFound() throws Exception {
        //given
        when(dataRemoteInterface.getStoreByAuthtokenAndDeviceid(any(HttpServletRequest.class))).thenReturn(null);

        //when
        /* mockMvc.perform(post("/signatureOptions")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON_UTF8)
                .content(objectMapper.writeValueAsString(signatureOptionFixture.signatureOption1)))
                .andExpect(status().is5xxServerError()); */

    }

    @Test
    public void Test_signatureOptionByStoreGuid() throws Exception {
        //given
        String storeGuid = "storeGuid";
        when(signatureOptionService.findByStoreGuid(eq(storeGuid))).thenReturn(signatureOptionFixture.signatureOption1);

        //when
        MvcResult result = mockMvc.perform(get("/signatureOptions/storeGuid/{storeGuid}", storeGuid))
                .andExpect(status().is2xxSuccessful()).andReturn();

        //then
        SignatureOption signatureOption = objectMapper.readValue(result.getResponse().getContentAsString(), SignatureOption.class);
        assertThat(signatureOption, is(signatureOptionFixture.signatureOption1));
        verify(signatureOptionService).findByStoreGuid(eq(storeGuid));
    }

    @Test
    public void Test_signatureOption_unknownStoreGuid() throws Exception {
        //given
        String storeGuid = "unknownStoreGuid";
        when(signatureOptionService.findByStoreGuid(eq(storeGuid))).thenReturn(null);

        //when
        MvcResult result = mockMvc.perform(get("/signatureOptions/storeGuid/{storeGuid}", storeGuid))
                .andExpect(status().is4xxClientError())
                .andReturn();

        //then
        assertThat(result.getResponse().getContentLength(), is(0));
    }

    @Test
    public void Test_deleteByGuid_Valid() throws Exception {
        //given
        String storeGuid = "storeGuid";
        doNothing().when(signatureOptionService).deleteByStoreGuid(eq(storeGuid));

        //when
        mockMvc.perform(delete("/signatureOptions")
                .param("storeGuid", storeGuid))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void Test_isFirstSignatureOptionForStore_Valid() throws Exception {
        //given
        Store store = Store.builder()
                .storeId(1L)
                .name("Unipos Store")
                .guid("storeGuid")
                .controllerStore(true)
                .address(Address.builder()
                        .street("Lafitegasse 26C")
                        .postCode(1130)
                        .country("Österreich")
                        .city("Wien")
                        .build())
                .closeHour(LocalDateTime.of(2016, 12, 22, 23, 0, 0))
                .companyGuid("storeGuid")
                .build();
        when(dataRemoteInterface.getStoreByAuthtokenAndDeviceid(any(HttpServletRequest.class))).thenReturn(store);
        when(signatureOptionService.findByStoreGuid(eq(store.getGuid()))).thenReturn(null);

        //when
        MvcResult result = mockMvc.perform(get("/signatureOptions/isFirstSignatureOptionForStore")
        ).andExpect(status().is2xxSuccessful()).andReturn();

        //then
        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), Boolean.class), is(true));

    }

    @Test
    public void Test_isFirstSignatureOptionForStore_Invalid() throws Exception {
        //given
        Store store = Store.builder()
                .storeId(1L)
                .name("Unipos Store")
                .guid("storeGuid")
                .controllerStore(true)
                .address(Address.builder()
                        .street("Lafitegasse 26C")
                        .postCode(1130)
                        .country("Österreich")
                        .city("Wien")
                        .build())
                .closeHour(LocalDateTime.of(2016, 12, 22, 23, 0, 0))
                .companyGuid("storeGuid")
                .build();
        when(dataRemoteInterface.getStoreByAuthtokenAndDeviceid(any(HttpServletRequest.class))).thenReturn(store);
        when(signatureOptionService.findByStoreGuid(eq(store.getGuid()))).thenReturn(signatureOptionFixture.signatureOption1);

        //when
        MvcResult result = mockMvc.perform(get("/signatureOptions/isFirstSignatureOptionForStore")
        ).andExpect(status().is2xxSuccessful()).andReturn();

        //then
        assertThat(objectMapper.readValue(result.getResponse().getContentAsString(), Boolean.class), is(false));

    }

    @Test
    public void Test_getSignatureOptionForDeviceAndUser_Valid() throws Exception {
        //given:
        Store store = Store.builder()
                .storeId(1L)
                .name("Unipos Store")
                .guid("storeGuid")
                .controllerStore(true)
                .address(Address.builder()
                        .street("Lafitegasse 26C")
                        .postCode(1130)
                        .country("Österreich")
                        .city("Wien")
                        .build())
                .closeHour(LocalDateTime.of(2016, 12, 22, 23, 0, 0))
                .companyGuid("storeGuid")
                .build();
        when(dataRemoteInterface.getStoreByAuthtokenAndDeviceid(any(HttpServletRequest.class))).thenReturn(store);
        when(signatureOptionService.findByStoreGuid(eq(store.getGuid()))).thenReturn(signatureOptionFixture.signatureOption2);

        //when
        MvcResult result = mockMvc.perform(get("/signatureOptions/forUserAndDevice"))
                .andExpect(status().is2xxSuccessful())
                .andReturn();

        //then
        SignatureOption signatureOption = objectMapper.readValue(result.getResponse().getContentAsString(), SignatureOption.class);
        assertThat(signatureOption, is(signatureOptionFixture.signatureOption2));
        verify(signatureOptionService, times(1)).findByStoreGuid(eq(store.getGuid()));
    }

    @Test
    public void Test_getSignatureOptionForDeviceAndUser_Invalid() throws Exception {
        //given:
        Store store = Store.builder()
                .storeId(1L)
                .name("Unipos Store")
                .guid("storeGuid")
                .controllerStore(true)
                .address(Address.builder()
                        .street("Lafitegasse 26C")
                        .postCode(1130)
                        .country("Österreich")
                        .city("Wien")
                        .build())
                .closeHour(LocalDateTime.of(2016, 12, 22, 23, 0, 0))
                .companyGuid("storeGuid")
                .build();
        when(dataRemoteInterface.getStoreByAuthtokenAndDeviceid(any(HttpServletRequest.class))).thenReturn(null);

        //when
        mockMvc.perform(get("/signatureOptions/forUserAndDevice"))
                .andExpect(status().is5xxServerError());

        //then
        verify(signatureOptionService, times(0)).findByStoreGuid(eq(store.getGuid()));
    }
}
