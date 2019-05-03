package unipos.data.components.entity;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MvcResult;
import unipos.data.shared.AbstractRestControllerTest;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author ggradnig
 */
@RunWith(SpringJUnit4ClassRunner.class)
@Import(EntityServiceMock.class)
public class EntityControllerTest extends AbstractRestControllerTest{
    @Autowired
    private EntityService entityService;

    @Test
    public void testFindAll() throws Exception{
        /**
         * We mock the service using the mockito-framework.
         * How awesome is that? :DD Love it.
         */
        when(entityService.findAllEntities()).thenReturn(Arrays.asList(new Entity("entity 1"), new Entity("entity 2")));

        ObjectMapper mapper = new ObjectMapper();

        MvcResult mvcResult = mockMvc.perform(get("/entities")).andExpect(status().is2xxSuccessful()).andReturn();

        List<Entity> entities = mapper.readValue(
                mvcResult.getResponse().getContentAsString(),
                new TypeReference<List<Entity>>() {}
        );

        assertThat(entities.size(), is(2));
    }
}
