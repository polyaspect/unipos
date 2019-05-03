package unipos.data.components.taxRates;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import unipos.data.shared.AbstractRestControllerTest;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dominik on 28.07.15.
 */
public class TaxRateControllerTest extends AbstractRestControllerTest {

    @Autowired
    TaxRateService taxRateService;

    Gson gson = new Gson();

    @Test
    public void testFindAllTaxRates() throws Exception {
        when(taxRateService.findAll()).thenReturn(Arrays.asList(new TaxRate("20%",10, TaxRateCategory.NORMAL, "ASDF")));

        MvcResult result = mockMvc.perform(get("/taxRates"))
                .andExpect(status().isOk())
                .andReturn();

        List<TaxRate> taxRates = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<TaxRate>>(){}.getType());

        assertThat(taxRates.size(), is(1));
        assertThat(taxRates.get(0).getPercentage(), is(10));
        assertThat(taxRates.get(0).getDescription(), is("ASDF"));
    }

    @Test
    public void testSaveTaxRate() throws Exception {
        reset(taxRateService);

        doNothing().when(taxRateService).saveTaxRate(any(TaxRate.class));

        TaxRate taxRate = new TaxRate("LOL",10, TaxRateCategory.NORMAL, "LOL");

        mockMvc.perform(post("/taxRates").content(gson.toJson(taxRate)).header("Content-Type", "application/json"))
                .andExpect(status().isOk());

        verify(taxRateService, times(1)).saveTaxRate(any(TaxRate.class));
    }

    @Test
    public void testDeleteTaxRateWithExistingMongoDbId() throws Exception {
        reset(taxRateService);

        doNothing().when(taxRateService).deleteTaxRateByMongoId(anyString());

        mockMvc.perform(delete("/taxRates").param("id", "LOL"))
                .andExpect(status().isOk());

        verify(taxRateService, times(1)).deleteTaxRateByMongoId("LOL");
    }

    @Test
    public void testDeleteTaxRateWithNotExistingMongoDbId() throws Exception {
        reset(taxRateService);

        doThrow(IllegalArgumentException.class).when(taxRateService).deleteTaxRateByMongoId(anyString());

        mockMvc.perform(delete("/taxRates").param("id", "ASDF"))
                .andExpect(status().is(404));

        verify(taxRateService, times(1)).deleteTaxRateByMongoId("ASDF");
    }
}