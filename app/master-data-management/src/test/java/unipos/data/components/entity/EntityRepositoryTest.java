package unipos.data.components.entity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unipos.data.test.config.MongoTestConfiguration;
import static org.junit.Assert.*;

/**
 * Created by ggradnig on 2015-02-05
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={MongoTestConfiguration.class})
public class EntityRepositoryTest {

    @Autowired
    private EntityFixture entityFixture;

    @Autowired
    private EntityRepository entityRepository;

    @Before
    public void setUp(){
        entityFixture.setUp();
    }

    @Test
    public void testGetEntityByAttribute(){
        assertNotNull(entityRepository.findByAttribute("entity"));
    }

    @After
    public void tearDown(){
        entityFixture.tearDown();
    }
}
