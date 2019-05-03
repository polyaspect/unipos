package unipos.pos.components.sequence;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
public class SequenceServiceMock {
    @Bean
    public SequenceService sequenceService(){
        return mock(SequenceService.class);
    }
}
