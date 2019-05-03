package unipos.data.components.category;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.internal.util.reflection.Whitebox;
import org.springframework.test.web.client.MockRestServiceServer;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Syncable;
import unipos.common.remote.sync.model.Target;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateCategory;
import unipos.data.components.taxRates.TaxRateRepository;
import unipos.data.shared.AbstractServiceTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;

/**
 * Created by dominik on 29.07.15.
 */
public class CategoryServiceTest extends AbstractServiceTest {
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    TaxRateRepository taxRateRepository;
    @Mock
    SyncRemoteInterface syncRemoteInterface;


    @InjectMocks
    CategoryService categoryService = new CategoryServiceImpl();

    @Override
    @Before
    public void setUp() {
        super.setUp();
        reset(categoryRepository);
    }

    @Test
    public void testFindAllCategories() {
        when(categoryRepository.findAll()).thenReturn(
                Arrays.asList(new Category("Schnitzel"), new Category("Cordon"), new Category("Trinken"))
        );

        List<Category> categoryList = categoryService.findAllCategories();

        assertThat(categoryList.size(), is(3));
        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    public void testFindCategoryWithExistingMongoDbId() {
        when(categoryRepository.exists(anyString())).thenReturn(true);
        when(categoryRepository.findOne(anyString())).thenReturn(new Category("Schnitzel"));

        Category category = categoryService.findCategoryByMongoDbId("SchnitzelId");

        assertThat(category, is(notNullValue()));
        assertThat(category.getName(), is("Schnitzel"));
        verify(categoryRepository, times(1)).findOne("SchnitzelId");
        verify(categoryRepository, times(1)).exists("SchnitzelId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCategoryWithNotExistingMongoDbId() {
        when(categoryRepository.exists(anyString())).thenReturn(false);

        categoryService.findCategoryByMongoDbId("SchnitzelId");

        verify(categoryRepository, times(1)).exists("SchnitzelId");
    }

    @Test
    public void testFindCategoryWithExistingName() {
        when(categoryRepository.findByNameLikeIgnoreCase(anyString())).thenReturn(Arrays.asList(new Category("Schnitzel")));

        List<Category> categories = categoryService.findCategoryByName("Schnitzel");
        assertThat(categories.size(), is(1));
        assertThat(categories.get(0).getName(), is("Schnitzel"));
        verify(categoryRepository, times(1)).findByNameLikeIgnoreCase("Schnitzel");

        categories = categoryService.findCategoryByName("ni");
        assertThat(categories.size(), is(1));
        assertThat(categories.get(0).getName(), is("Schnitzel"));
        verify(categoryRepository, times(1)).findByNameLikeIgnoreCase("ni");


    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindCategoryWithNotExistingName() {
        when(categoryRepository.findByNameLikeIgnoreCase(anyString())).thenReturn(Collections.<Category>emptyList());

        categoryService.findCategoryByName("NotExistingName");
        verify(categoryRepository, times(1)).findByNameLikeIgnoreCase("NotExistingName");
    }

    @Test
    public void testDeleteCategoryByExistingMongoId() {
        when(categoryRepository.exists(anyString())).thenReturn(true);
        doNothing().when(categoryRepository).delete(anyString());

        categoryService.deleteCategoryByMongoId("CategoryId");

        verify(categoryRepository, times(1)).exists("CategoryId");
        verify(categoryRepository, times(1)).delete("CategoryId");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeleteCategoryByNotExistingMongoId() {
        when(categoryRepository.exists(anyString())).thenReturn(false);

        categoryService.deleteCategoryByMongoId("CategoryId");

        verify(categoryRepository.exists("CategoryId"));
    }

    @Test
    public void testSaveCategorySuccessful() {
        Category category = new Category("Schnitzel");
        category.setId("MongoId");
        category.setTaxRate(new TaxRate("", 20, TaxRateCategory.NORMAL));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(taxRateRepository.findOne(anyString())).thenReturn(new TaxRate("", 30 , TaxRateCategory.NORMAL));

        categoryService.saveCategory(category);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveCategoryUnsuccessful() {
        Category category = new Category("Schnitzel");
        when(categoryRepository.save(any(Category.class))).thenReturn(category);
        when(taxRateRepository.findOne(anyString())).thenReturn(new TaxRate("", 30 , TaxRateCategory.NORMAL));

        categoryService.saveCategory(category);

        verify(categoryRepository, times(1)).save(any(Category.class));
    }

    @Test
    public void testDeleteAllRepositories() {
        doNothing().when(categoryRepository).deleteAll();

        categoryService.deleteAllCategories();

        verify(categoryRepository, times(1)).deleteAll();
    }

    @Test
    public void testDeleteByGuid() throws Exception {
        Category category = Category.builder()
                .taxRate(TaxRate.builder()
                        .description("Description")
                        .guid(UUID.randomUUID().toString())
                        .name("Food")
                        .percentage(20)
                        .taxRateCategory(TaxRateCategory.NORMAL)
                        .build())
                .build();

        when(categoryRepository.findFirstByGuid(anyString())).thenReturn(category);
       when(categoryRepository.deleteByGuid(anyString())).thenReturn(1L);
        doNothing().when(syncRemoteInterface).syncChanges(any(Syncable.class), any(Target.class), any(Action.class));

        categoryService.deleteByGUID("guid");

        verify(categoryRepository, times(1)).deleteByGuid(eq("guid"));
        verify(syncRemoteInterface, times(1)).syncChanges(eq(category), eq(Target.CATEGORY), eq(Action.DELETE));
    }
}