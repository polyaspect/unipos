package unipos.pos.components.sequence;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import unipos.pos.test.config.MongoTestConfiguration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by dominik on 08.09.15.
 */

@ContextConfiguration(classes = MongoTestConfiguration.class)
public class SequenceRepositoryTest extends AbstractJUnit4SpringContextTests {

    @Autowired
    SequenceRepository sequenceRepository;
    @Autowired
    SequenceFixture sequenceFixture;

    @Before
    public void setUp() throws Exception {
        sequenceFixture.setUp();
    }

    @After
    public void tearDown() throws Exception {
        sequenceFixture.tearDown();
    }

    @Test
    public void testSquenceGeneration() throws Exception {
        Long sequenceNumberOrder = sequenceRepository.getNextSequenceId("ORDER");
        Long sequenceNumberInvoice = sequenceRepository.getNextSequenceId("INVOICE");
        assertThat(sequenceNumberInvoice, is(1L));
        assertThat(sequenceNumberOrder, is(1L));
    }



}
