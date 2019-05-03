package unipos.data.components.category;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.data.test.config.MongoTestConfiguration;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

/**
 * Created by dominik on 29.07.15.
 */

@ContextConfiguration(classes = MongoTestConfiguration.class)
public class CategoryRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryFixture categoryFixture;

    @Before
    public void setUp() throws Exception {
        categoryFixture.setUp();
    }

    @Test
    public void testFindAll() throws Exception {
        List<Category> categories = categoryRepository.findAll();

        assertThat(categories.size(), is(2));
    }

    @Test
    public void testSave() {
        Category category = new Category("Trinken");
        assertThat(category.getId(), is(nullValue()));

        categoryRepository.save(category);
        assertThat(category.getId(), is(notNullValue()));
    }

    @Test
    public void testFindOne() {
        Category category = categoryFixture.category;

        assertThat(category.getId(), is(notNullValue()));
        category = categoryRepository.findOne(category.getId());
        assertThat(category, is(notNullValue()));
        assertThat(category.getId(), is(notNullValue()));
        assertThat(category.getName(), is("Schnitzel"));
        assertThat(category.getTaxRate().getPercentage(), is(20));
    }

    @Test
    public void testDeleteOneByMongoId() {
        Category category = new Category("Trinken");
        categoryRepository.save(category);

        assertThat(category.getId(), is(notNullValue()));
        assertThat(categoryRepository.findAll().size(), is(3));

        categoryRepository.delete(category.getId());
        assertThat(categoryRepository.findAll().size(), is(2));
    }

    @Test
    public void testDeleteAllCategories() {
        assertThat(categoryRepository.findAll().size(), is(2));
        categoryRepository.deleteAll();
        assertThat(categoryRepository.findAll().size(), is(0));
    }

    @Test
    public void testFindByName() {
        List<Category> category = categoryRepository.findByNameLikeIgnoreCase("Schnitzel");
        assertThat(category.size(), is(1));
        assertThat(category.get(0).getName(), is("Schnitzel"));

        category = categoryRepository.findByNameLikeIgnoreCase("hn");
        assertThat(category.size(), is(1));
        assertThat(category.get(0).getName(), is("Schnitzel"));
    }

    @Test
    public void testDeleteByGuid() throws Exception {
        assertThat(categoryRepository.findAll().size(), is(2));
        categoryRepository.deleteByGuid(categoryFixture.category.getGuid());
        assertThat(categoryRepository.findAll().size(), is(1));
        assertThat(categoryRepository.findFirstByGuid(categoryFixture.category.getGuid()), is(nullValue()));
    }

    @Test
    public void testDeleteByGuidWithInvalidGuid() throws Exception {
        assertThat(categoryRepository.findAll().size(), is(2));
        categoryRepository.deleteByGuid("asdf");
        assertThat(categoryRepository.findAll().size(), is(2));
    }

    @After
    public void tearDown() throws Exception {
        categoryFixture.tearDown();
    }
}