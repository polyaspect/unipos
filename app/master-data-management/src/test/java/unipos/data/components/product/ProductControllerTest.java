package unipos.data.components.product;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.hamcrest.CustomMatcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import unipos.data.components.category.Category;
import unipos.data.components.company.StoreService;
import unipos.data.components.company.model.Store;
import unipos.data.components.productLog.ProductLogService;
import unipos.data.shared.AbstractRestControllerTest;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Dominik on 23.07.2015.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@Import(ProductServiceMock.class)
public class ProductControllerTest extends AbstractRestControllerTest {

    @Autowired
    ProductService productService;
    @Autowired
    ProductLogService productLogService;
    @Autowired
    StoreService storeService;

    Gson gson = new Gson();

    @Before
    public void setUp() throws Exception {
        super.setup();
        reset(productService);
    }

    @Test
    public void testSaveProduct() throws Exception {


        doNothing().when(productService).saveProduct(any(Product.class));
        when(productLogService.existsProductNumber(anyLong())).thenReturn(true);

        Product product = getProduct();
        product.setProductIdentifier(null);  //To save a Product, the productIdentifier has to be unset. If it's set, an update is triggered instead.

        Gson gson = new Gson();
        String json = gson.toJson(product);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk());

        verify(productService, times(1)).saveProduct(product);
    }

    @Test
    public void testSaveProductWithCategoryIsNull() throws Exception{
        reset(productService);
        Product product = Product.builder()
                .name("Joyce")
                .description("Joyce Kutumthanamkuzhy")
                .number(1L)
                .price(new BigDecimal("1234.99"))
                .category(null)
                .guid(UUID.randomUUID().toString())
                .attributes(new ArrayList<>())
                .build();

        doNothing().when(productService).saveProduct(any(Product.class));

        mockMvc.perform(post("/products")
                .content(gson.toJson(product))
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk());

        verify(productService, times(1)).saveProduct(product);
    }

    @Test
    public void testSaveProductWithInvalidCategory() throws Exception{
        Product product = Product.builder()
                .id("")
                .name("Joyce")
                .description("Joyce Kutumthanamkuzhy")
                .number(1L)
                .price(new BigDecimal("1234.99"))
                .category(new Category("asdf", null))
                .guid(UUID.randomUUID().toString())
                .attributes(new ArrayList<>())
                .build();

        doNothing().when(productService).saveProduct(any(Product.class));

        mockMvc.perform(post("/products")
                .content(gson.toJson(product))
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk());

        product.setCategory(null);
        verify(productService, times(1)).saveProduct(product);
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = getProduct();

        doNothing().when(productService).updateProduct(any(Product.class));

        mockMvc.perform(post("/products")
                .content(gson.toJson(product))
                .header("Content-Type", "application/json"))
                .andExpect(status().isOk());

        verify(productService, times(1)).updateProduct(product);
    }

    @Test
    public void testListProducts() throws Exception {
        when(productService.listProductsByStore(any(Store.class))).thenReturn(Arrays.asList(new Product("Product1", "This is Product1", new BigDecimal("1.00")), new Product("Product2", "This is Product2", new BigDecimal("2.00"))));
        when(storeService.getStoreByUserAndDevice(any(HttpServletRequest.class))).thenReturn(Store.builder().guid("storeGuid").build());

        MvcResult result = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

        List<Product> entities = mapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<Product>>() {
                }
        );

        verify(productService, times(1)).listProductsByStore(argThat(new ArgumentMatcher<Store>() {
            @Override
            public boolean matches(Object o) {
                Store store = (Store)o;
                return store.getGuid().equals("storeGuid");
            }
        }));
        assertThat(entities.size(), is(2));
    }

    @Test
    public void testDeleteProductById() throws Exception {
        doNothing().when(productService).deleteProduct(anyString());

        mockMvc.perform(delete("/products/dbId")
                .param("id", "asdfa7f7sadf88"))
                .andExpect(status()
                        .isOk());

        verify(productService, times(1)).deleteProduct(anyString());
    }

    @Test
    public void testDeleteProductByIdWithInvalidId() throws Exception {
        mockMvc.perform(delete("/products/dbId")
                .param("id", ""))
                .andExpect(status().is(400));
    }

    @Test
    public void testDeleteProductByIdWithUnableId() throws Exception {
        doThrow(IllegalArgumentException.class).when(productService).deleteProduct(anyString());

        mockMvc.perform(delete("/products/dbId")
        .param("id", "76578d89f7ggddf768"))
                .andExpect(status().is(404));
    }

    @Test
    public void testDeleteProductByNumber() throws Exception {
        doNothing().when(productService).deleteProductByProductNumber(anyLong());

        mockMvc.perform(delete("/products/productNumber")
        .param("productNumber", "12345"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteProductByNumberInvalidNumber() throws Exception {
        mockMvc.perform(delete("/products/productNumber")
        .param("productNumber", "-1234"))
                .andExpect(status().is(400));
    }

    @Test
    public void testDeleteProductByNumberUnavailableNumber() throws Exception {
        doThrow(IllegalArgumentException.class).when(productService).deleteProductByProductNumber(anyLong());

        mockMvc.perform(delete("/products/productNumber")
        .param("productNumber", "12345"))
                .andExpect(status().is(404));
    }

    @Test
    public void testFindProductByMongoId() throws Exception {
        when(productService.findProductById(anyString())).thenReturn(new Product("Red Bull", "Red Bull Energy Drink 250ml", new BigDecimal("1.49")));

        MvcResult result=  mockMvc.perform(get("/products/dbId/1"))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

       Product product = mapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<Product>() {}
        );

        assertThat(product.getName(), is("Red Bull"));
        assertThat(product.getPrice(), is(new BigDecimal("1.49")));
        assertThat(product.getDescription(), is("Red Bull Energy Drink 250ml"));
    }

    @Test
    public void testFindProductByProductNumberOrName() throws Exception {
        when(productService.findProductByNumberOrName(anyString())).thenReturn(
                Arrays.asList(
                        new Product("", 1L, "Red Bull", "Red Bull Energy Drink 355ml", new BigDecimal("1.59"), null),
                        new Product("", 2L, "LG G3", "LG D855 32 GB", new BigDecimal("355.00"), null),
                        new Product("", 3L, "IKEA Tisch 1234", "IKEA ASDF Tisch 1234", new BigDecimal("99.99"), null)
                )
        );

        MvcResult result = mockMvc.perform(get("/products/productNumberOrName/{searchString}", 1))
                .andExpect(status().isOk())
                .andReturn();

        ObjectMapper mapper = new ObjectMapper();

        List<Product> products = mapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<List<Product>>() {
                }
        );

        assertThat(products.size(), is(3));
    }

    @Test
    public void testFindProductByProductNumberOrNameWithoutResult() throws Exception {
        when(productService.findProductByNumberOrName(anyString())).thenReturn(Collections.<Product>emptyList());

        MvcResult result = mockMvc.perform(get("/products/productNumberOrName/{searchString}", Math.PI))
                .andExpect(status().is(404))
                .andReturn();

        Gson gson = new Gson();
        List<Product> products = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Product>>() {
        }.getType());
        assertThat(products, is(nullValue()));
    }

    private Product getProduct() {
        return Product.builder()
                .id("TestId-->ShouldCauseUpdate")
                .name("Joyce")
                .description("Joyce Kutumthanamkuzhy")
                .number(1L)
                .price(new BigDecimal("1234.99"))
                .category(null)
                .productIdentifier(1L)
                .guid(UUID.randomUUID().toString())
                .attributes(new ArrayList<>())
                .build();
    }

/*    @Test
    public void testGetNextProductNumber() throws Exception {
        when(productService.getProductWithHighestProductNumber()).thenReturn(new Product(null, 666L, "Red Bull", "Racing", new BigDecimal("1.59"),null));

        MvcResult result = mockMvc.perform(get("/products/highestProductNumberProduct"))
                .andExpect(status().isOk())
                .andReturn();


        ObjectMapper mapper = new ObjectMapper();

        Product highestProduct = mapper.readValue(
                result.getResponse().getContentAsString(),
                new TypeReference<Product>() {
                }
        );

        assertThat(highestProduct.getNumber(), is(666L));
        assertThat(highestProduct.getPrice(), is(new BigDecimal("1.59")));
    }*/
}
