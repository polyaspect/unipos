package unipos.pos.components.invoice;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.pos.shared.AbstractRestControllerTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

/**
 * Created by dominik on 08.09.15.
 */
public class InvoiceControllerTest extends AbstractRestControllerTest {

    @Autowired
    InvoiceService invoiceService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    PosRemoteInterface posRemoteInterface;

    @Before
    public void setUp() throws Exception {
        super.setup();
        reset(invoiceService);
    }

    @Test
    public void testCreateInvoiceFromOrder() throws Exception {

 /*       when(invoiceService.createInvoiceFromOrder(anyString(), any(Company.class), any(Store.class), anyString(), any())).thenReturn(null);
        when(dataRemoteInterface.getCompanyByGuid(anyString())).thenReturn(Company.builder().commercialRegisterNumber("123").name("ASDF").guid("asdf").build());
        when(dataRemoteInterface.getStoreByAuthtokenAndDeviceid(anyString(),anyString())).thenReturn(Store.builder().build());
        when(posRemoteInterface.isCreationAllowed(any(HttpServletRequest.class))).thenReturn(true);
        //params("clientOrderId","myOrderDocumentId");
        mockMvc.perform(post("/invoices/createFromOrder")
                .cookie(new Cookie("DeviceToken", "LOL"), new Cookie("AuthToken", "LOL"))
                .content("myOrderDocumentId")).andExpect(status().isOk());

        verify(invoiceService).createInvoiceFromOrder(eq("myOrderDocumentId"), any(Company.class), any(Store.class), anyString(), any());*/
        //TODO: Fix Texts
    }
}
