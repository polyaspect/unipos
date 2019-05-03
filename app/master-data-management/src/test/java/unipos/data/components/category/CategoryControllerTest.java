package unipos.data.components.category;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import unipos.data.shared.AbstractRestControllerTest;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by dominik on 29.07.15.
 */
public class CategoryControllerTest extends AbstractRestControllerTest {

    @Autowired
    CategoryService categoryService;

    Gson gson = new Gson();

    @Before
    public void setUp() throws Exception {
        super.setup();
        reset(categoryService);
    }

    @Test
    public void testFindAllCategories() throws Exception{
        when(categoryService.findAllCategories()).thenReturn(Arrays.asList(new Category("Schnitzel"), new Category("Cordon"), new Category("Trinken")));

        MvcResult result = mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        List<Category> categories = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Category>>(){}.getType());

        assertThat(categories.size(), is(3));
        assertThat(categories.get(0).getName(), is("Schnitzel"));
        verify(categoryService, times(1)).findAllCategories();
    }

    @Test
    public void findCategoryByExistingMongoDbId() throws Exception {
        when(categoryService.findCategoryByMongoDbId(anyString())).thenReturn(new Category("Nachspeisen"));

        MvcResult result = mockMvc.perform(get("/categories/dbId/{dbId}", "MongoDb"))
                .andExpect(status().isOk())
                .andReturn();

        Category category = gson.fromJson(result.getResponse().getContentAsString(), Category.class);

        assertThat(category, is(notNullValue()));
        assertThat(category.getName(), is("Nachspeisen"));
        verify(categoryService, times(1)).findCategoryByMongoDbId("MongoDb");
    }

    @Test
    public void findCategoryByNotExistingMongoDbId() throws Exception {
        doThrow(IllegalArgumentException.class).when(categoryService).findCategoryByMongoDbId(anyString());

        mockMvc.perform(get("/categories/dbId/{dbId}", "NotExistingDbId"))
                .andExpect(status().is(404));

        verify(categoryService, times(1)).findCategoryByMongoDbId("NotExistingDbId");
    }

    @Test
    public void findCategoryByExistingName() throws Exception {
        when(categoryService.findCategoryByName(anyString())).thenReturn(Arrays.asList(new Category("Schnitzel"), new Category("Cordon")));

        MvcResult result =  mockMvc.perform(get("/categories/name/{name}", "Sch"))
                .andExpect(status().isOk())
                .andReturn();

        List<Category> categories = gson.fromJson(result.getResponse().getContentAsString(), new TypeToken<List<Category>>(){}.getType());

        assertThat(categories.size(), is(2));
        assertThat(categories.get(0).getName(), is("Schnitzel"));
        assertThat(categories.get(1).getName(), is("Cordon"));
    }

    @Test
    public void findCategoryByNotExistingName() throws Exception {
        doThrow(IllegalArgumentException.class).when(categoryService).findCategoryByName(anyString());

        mockMvc.perform(get("/categories/name/{name}", "Sch"))
                .andExpect(status().is(404));
    }

    @Test
    public void deleteCategoryByExistingMongoDbId() throws Exception {
        doNothing().when(categoryService).deleteCategoryByMongoId(anyString());

        mockMvc.perform(delete("/categories").param("id", "MongoId"))
                .andExpect(status().isOk());
    }

    @Test
    public void DeleteCategoryByNotExistingMongoDbId() throws Exception {
        doThrow(IllegalArgumentException.class).when(categoryService).deleteCategoryByMongoId(anyString());

        mockMvc.perform(delete("/categories").param("id", "MongoId"))
                .andExpect(status().is(404));
    }

    @Test
    public void saveCategorySuccessful() throws Exception{
        when(categoryService.saveCategory(any(Category.class))).thenReturn(Category.builder().name("MyCategory").taxRate(null).build());

        Category category = new Category("Schnitzel");

        mockMvc.perform(post("/categories").content(gson.toJson(category)).header("Content-Type", "application/json"))
                .andExpect(status().isOk());

        verify(categoryService, times(1)).saveCategory(any(Category.class));
    }

    @Test
    public void saveCategoryNotSuccessful() throws Exception{
        doThrow(IllegalArgumentException.class).when(categoryService).saveCategory(any(Category.class));

        Category category = new Category("Schnitzel");

        mockMvc.perform(post("/categories").content(gson.toJson(category)).header("Content-Type", "application/json"))
                .andExpect(status().is(400));

        verify(categoryService, times(1)).saveCategory(any(Category.class));
    }

    @Test
    public void testDeleteByGuid() throws Exception {
        doNothing().when(categoryService).deleteByGUID(anyString());

        mockMvc.perform(delete("/categories/guid").param("guid", "myGuid"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteByGuidWithNullGuid() throws Exception {
        mockMvc.perform(delete("/categories/guid").param("guid", ""))
                .andExpect(status().is(400));
    }
}