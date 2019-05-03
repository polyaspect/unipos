package unipos.data.components.entity;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import unipos.data.shared.AbstractServiceTest;

import java.util.Arrays;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author ggradnig
 */

public class EntityServiceTest extends AbstractServiceTest{

    @InjectMocks
    private EntityService entityService = new EntityServiceImpl();

    @Mock
    private EntityRepository entityRepository;

    @Test
    public void testFindAllEntities(){
        when(entityRepository.findAll()).thenReturn(Arrays.asList(new Entity("entity 1"), new Entity("entity 2")));
        assertThat(entityService.findAllEntities().size(), is(2));
    }
}
