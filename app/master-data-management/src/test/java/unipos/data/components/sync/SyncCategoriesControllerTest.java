package unipos.data.components.sync;

import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import unipos.data.components.category.Category;
import unipos.data.components.category.CategoryRepository;
import unipos.data.components.product.Product;
import unipos.data.components.productLog.LogAction;
import unipos.data.components.productLog.ProductLog;
import unipos.data.components.shared.GSonHolder;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateCategory;
import unipos.data.components.taxRates.TaxRateRepository;
import unipos.data.shared.AbstractRestControllerTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



/**
 * Created by dominik on 07.11.15.
 */

public class SyncCategoriesControllerTest extends AbstractRestControllerTest {

    Gson gson = GSonHolder.serializeDateGson();

    @InjectMocks
    @Autowired
    CategorySyncController categorySyncController;
    @Mock
    TaxRateRepository taxRateRepository;
    @Mock
    CategoryRepository categoryRepository;

    @Before
    public void setUp() throws Exception {
        setup();
        //Whitebox.setInternalState();
    }

    @Test
    public void testCreate() throws Exception {
        Category category = Category.builder()
                .taxRate(TaxRate.builder()
                        .taxRateCategory(TaxRateCategory.NORMAL)
                        .percentage(20)
                        .name("Essen")
                        .guid("guidTaxRate")
                        .description("Dinge die man essen kann")
                        .build())
                .guid("guid")
                .name("Schnitzel")
                .build();

        when(taxRateRepository.findFirstByGuid(anyString())).thenReturn(TaxRate.builder()
                .taxRateCategory(TaxRateCategory.NORMAL)
                .percentage(20)
                .name("Essen")
                .guid("guid")
                .description("Dinge die man essen kann")
                .build());

        mockMvc.perform(post("/syncCategory/create").param("logCreation", gson.toJson(category)))
                .andExpect(status().isOk());

        verify(taxRateRepository, times(1)).findFirstByGuid(eq("guidTaxRate"));
        verify(categoryRepository, times(1)).save(argThat(new ArgumentMatcher<Category>() {
            @Override
            public boolean matches(Object o) {
                if(o instanceof Category) {
                    Category category1 = (Category)o;
                    return category1.getGuid().equals("guid")
                            && category1.getId() == null
                            && category1.getName().equals("Schnitzel")
                            && category1.getTaxRate() != null;
                } else
                    return false;
            }
        }));
    }

    @Test
    public void testDelete() throws Exception {
        Category category = Category.builder()
                .taxRate(TaxRate.builder()
                        .taxRateCategory(TaxRateCategory.NORMAL)
                        .percentage(20)
                        .name("Essen")
                        .guid("guidTaxRate")
                        .description("Dinge die man essen kann")
                        .build())
                .guid("guid")
                .name("Schnitzel")
                .build();

        when(categoryRepository.deleteByGuid(anyString())).thenReturn(1L);

        mockMvc.perform(post("/syncCategory/delete").param("logCreation", gson.toJson(category)))
                .andExpect(status().isOk());

        verify(categoryRepository, times(1)).deleteByGuid(eq("guid"));
    }

}