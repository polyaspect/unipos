package unipos.pos.components.sequence;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import unipos.pos.shared.AbstractServiceTest;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

/**
 * Created by dominik on 08.09.15.
 */
public class SequenceServiceTest extends AbstractServiceTest {

    @InjectMocks
    SequenceService sequenceService = new SequenceServiceImpl();
    @Mock
    SequenceRepository sequenceRepository;

    @Test
    public void testSequenceGeneration() throws Exception {
        when(sequenceRepository.getNextSequenceId("ORDER")).thenReturn(1L);
        when(sequenceRepository.getNextSequenceId("INVOICE")).thenReturn(2L);

        Long sequenceNumberOrder = sequenceService.getNextSequenceId("ORDER");
        Long sequenceNumberInvoice = sequenceService.getNextSequenceId("INVOICE");

        assertThat(sequenceNumberOrder ,is(1L));
        assertThat(sequenceNumberInvoice ,is(2L));
    }
}
