package unipos.design.shared;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

/**
 * @author ggradnig
 */
abstract public class AbstractServiceTest {
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }
}
