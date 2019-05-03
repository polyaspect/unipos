package unipos.signature.shared;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import unipos.signature.config.FixtureConfiguration;

/**
 * @author ggradnig
 */
@ContextConfiguration(classes = FixtureConfiguration.class)
abstract public class AbstractServiceTest extends AbstractJUnit4SpringContextTests {
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}
