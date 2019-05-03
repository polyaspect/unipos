package unipos.data.components.product;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import unipos.data.components.category.Category;
import unipos.data.components.category.CategoryRepository;
import unipos.data.components.productLog.LogAction;
import unipos.data.components.productLog.ProductLog;
import unipos.data.components.productLog.ProductLogRepository;
import unipos.data.components.sequence.ProductLogSequenceRepository;
import unipos.data.shared.AbstractServiceTest;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Dominik on 23.07.2015.
 */
public class ProductServiceTest extends AbstractServiceTest {

    @InjectMocks
    ProductService productService = new ProductServiceImpl();

    @Mock
    ProductRepository productRepository;
    @Mock
    ProductLogRepository productLogRepository;
    @Mock
    ProductLogSequenceRepository productLogSequenceRepository;
    @Mock
    CategoryRepository categoryRepository;

    @Override
    @Before
    public void setUp() {
        super.setUp();
        reset(productLogRepository, productRepository);
    }

    @Test
    public void testSaveProduct() {

        Product product = Product.builder()
                .name("Joyce")
                .description("Kudumthanamkuzhy")
                .price(new BigDecimal("1.59"))
                .number(123L)
                .category(Category.builder().id("").name("asdf").taxRate(null).build())
                .build();

        when(productLogRepository.save(any(ProductLog.class))).thenReturn(ProductLog.newProductLogFromProduct(product));
        when(productLogSequenceRepository.getNextSequenceId(anyString())).thenReturn(123L);
        when(categoryRepository.findOne(anyString())).thenReturn(Category.builder().id("1").name("Essen").taxRate(null).build());

        productService.saveProduct(product);

        ProductLog productLog = ProductLog.newProductLogFromProduct(product);
        productLog.setAction(LogAction.CREATE);

        verify(productRepository, times(0)).save(any(Product.class));
        verify(productLogRepository, times(1)).save(any(ProductLog.class));
    }

    @Test
    public void testUpdateProduct() throws Exception {
        Product product = Product.builder()
                .id("ThisIsAnAlreadySetId")
                .name("Joyce")
                .description("Kudumthanamkuzhy")
                .price(new BigDecimal("1.59"))
                .number(123L)
                .category(Category.builder().id("").name("Trinken").taxRate(null).build())
                .productIdentifier(1L)
                .build();
        when(productLogRepository.save(any(ProductLog.class))).thenReturn(null);
        when(categoryRepository.findOne(anyString())).thenReturn(Category.builder().id("asdf").name("Trinken").taxRate(null).build());

        productService.updateProduct(product);

        ProductLog productLog = ProductLog.newProductLogFromProduct(product);
        productLog.setAction(LogAction.UPDATE);

        verify(productRepository, times(0)).save(any(Product.class));
        verify(productLogRepository, times(1)).save(any(ProductLog.class));
    }

    @Test
    public void testSaveProductWithInvalidCategory() throws Exception {
        when(productLogRepository.save(any(ProductLog.class))).thenReturn(null);


        Product product = Product.builder()
                .id("")
                .name("Joyce")
                .description("Joyce Kutumthanamkuzhy")
                .number(1L)
                .price(new BigDecimal("1234.99"))
                .category(null)
                .build();

        productService.saveProduct(product);

        verify(productRepository, times(0)).save(product);
        verify(productLogRepository, times(1)).save(any(ProductLog.class));
    }

    @Test
    public void testListProducts() throws Exception {
        when(productRepository.findAll()).thenReturn(Arrays.asList(new Product("Product1", "This is Product1", new BigDecimal("1.00")), new Product("Product2", "This is Product2", new BigDecimal("2.00"))));
        List<Product> products = productService.listProducts();

        assertThat(products.size(), is(2));
        assertThat(products.get(0).getName(), is("Product1"));
        assertThat(products.get(1).getName(), is("Product2"));
    }

    @Test
    public void testDeleteProduct() throws Exception {
        reset(productRepository);

        doNothing().when(productLogRepository).delete(any(ProductLog.class));
        when(categoryRepository.findOne(anyString())).thenReturn(Category.builder().id("").name("").taxRate(null).build());

        Product deletableProduct = Product.builder()
                .id("productId")
                .name("LG G3")
                .price(new BigDecimal("356.99"))
                .number(1L)
                .category(Category.builder()
                        .id("")
                        .name("Technik")
                        .taxRate(null)
                        .build())
                .productIdentifier(1L)
                .build();

        productService.deleteProduct(deletableProduct);

        verify(productLogRepository, times(1)).save(any(ProductLog.class));
    }

    @Test
    public void testDeleteProductById() throws Exception {
        reset(productRepository);

        doNothing().when(productLogRepository).delete(anyString());
        when(productLogRepository.findOne(anyString())).thenReturn(ProductLog.newProductLogFromProduct(new Product("", 321L, "Red Bull", "Red Bull Energy Drink 355ml", new BigDecimal("1.59"), null)));
        when(productRepository.exists(anyString())).thenReturn(true);
        when(productRepository.findOne(anyString())).thenReturn(new Product("", 321L, "Red Bull", "Red Bull Energy Drink 355ml", new BigDecimal("1.59"), null));

        productService.deleteProduct("asdfa7sdf6asd7f");

        verify(productLogRepository, times(1)).save(any(ProductLog.class));
    }

    @Test
    public void testFindByMongoId() throws Exception {
        reset(productRepository);

        when(productRepository.findOne(anyString())).thenReturn(new Product("Red Bull", "Red Bull Energy Drink", new BigDecimal("6.66")));

        Product product = productService.findProductById("asdfghjkl");

        verify(productRepository, times(2)).findOne("asdfghjkl");
        assertThat(product.getName(), is("Red Bull"));
        assertThat(product.getPrice(), is(new BigDecimal("6.66")));
    }

    @Test
    public void testFindByExistingNumberOrNotExistingName() throws Exception {
        when(productRepository.findByNumberOrNameLikeIgnoreCase(anyInt(), anyString())).thenReturn(Arrays.asList(new Product("", 1L, "Red Bull", "Red Bull Energy Drink 355ml", new BigDecimal("1.59"), null)));

        Product product = productService.findProductByNumberOrName("1").get(0);

        Assert.assertThat(product.getName(), is("Red Bull"));
        Assert.assertThat(product.getDescription(), is("Red Bull Energy Drink 355ml"));
        Assert.assertThat(product.getNumber(), is(1L));
        Assert.assertThat(product.getPrice(), is(new BigDecimal("1.59")));
    }

    @Test
    public void testFindByNotExistingNumberOrExistingName() throws Exception {
        when(productRepository.findByNameLikeIgnoreCase(anyString())).thenReturn(Arrays.asList(new Product("", 1L, "Red Bull", "Red Bull Energy Drink 355ml",  new BigDecimal("1.59"), null)));

        Product product = productService.findProductByNumberOrName("Red Bull").get(0);

        Assert.assertThat(product.getName(), is("Red Bull"));
        Assert.assertThat(product.getDescription(), is("Red Bull Energy Drink 355ml"));
        Assert.assertThat(product.getNumber(), is(1L));
        Assert.assertThat(product.getPrice(), is(new BigDecimal("1.59")));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testFindByNotExistingNumberOrNotExistingName() throws Exception {
        when(productRepository.findByNumberOrNameLikeIgnoreCase(anyInt(), anyString())).thenReturn(Arrays.asList(new Product("", 1L, "Red Bull", "Red Bull Energy Drink 355ml", new BigDecimal("1.59"), null)));

        Product product = productService.findProductByNumberOrName("asdadf").get(0);

        Assert.assertThat(productRepository.findByNumberOrNameLikeIgnoreCase(-1, "asdfasdf").size(), is(0));
    }

    @Test
    public void testSaveProducts() throws Exception {
        when(productLogRepository.save(any(ProductLog.class))).thenReturn(null);
        when(productLogSequenceRepository.getNextSequenceId(anyString())).thenReturn(123L);
        when(categoryRepository.findOne(anyString())).thenReturn(Category.builder().id("1").name("Essen").taxRate(null).build());


        Product product1 = new Product(null, 1L, "Red Bull", "Red Bull Energy Drink 355ml",  new BigDecimal("1.59"), null);
        Product product2 = new Product(null, 2L, "LG G3", "LG D855",  new BigDecimal("356.00"), null);
        Product product3 = new Product(null, 3L, "Joyce Laptop", "Medion Erazer DX-7611",  new BigDecimal("855.99"), null);
        Product product4 = new Product(null, 4L, "Joyce alter Laptop", "Acer Aspire 7745G",  new BigDecimal("999.99"), null);

        productService.saveProducts(product1,product2,product3,product4);

        verify(productLogRepository, times(4)).save(any(ProductLog.class));
    }

    @Test
    public void testDeleteProductByProductNumber() throws Exception {
        ProductLog recentlyCreatedProduct = ProductLog.builder()
                ._id("id")
                .product(Product.builder()
                        .id("productId")
                        .productIdentifier(1L)
                        .category(Category.builder().name("Technik").id("categoryId").build())
                        .description("desc")
                        .number(1L)
                        .price(new BigDecimal("1.99"))
                        .name("Red Bull").build())
                .action(LogAction.CREATE)
                .productIdentifier(1L)
                .date(LocalDateTime.now())
                .published(false)
                .deleted(false).build();
        when(productLogRepository.findFirstByProductIdentifierAndIgnoredOrderByDateDesc(anyLong(), anyBoolean())).thenReturn(recentlyCreatedProduct);

        productService.deleteProductByProductNumber(1L);

        verify(productLogRepository, times(1)).save(any(ProductLog.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteProductByProductNumberWithInvalidNumber() throws Exception {
        when(productLogRepository.findFirstByProductIdentifierAndIgnoredOrderByDateDesc(anyLong(), anyBoolean())).thenReturn(null);

        productService.deleteProductByProductNumber(1L);
    }

/*    @Test
    public void testGetNextValidProductNumber() throws Exception {
        when(productRepository.findFirstByIgnoredOrderByProduct_NumberDesc()).thenReturn(new Product(null, 666L, "Red Bull", "Energy Drink",  new BigDecimal("1.59"), null));

        Product lastProduct = productService.getProductWithHighestProductNumber();
        assertThat(lastProduct.getNumber(), is(666L));
    }*/
}
