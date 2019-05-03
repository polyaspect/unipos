package unipos.auth.components.user.mitarbeiteridPin;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 04.06.2015.
 */
@Configuration
public class MitarbeiterPinServiceMock {

    @Bean
    public MitarbeiteridPinService mitarbeiteridPinService() {
        return mock(MitarbeiteridPinService.class);
    }
}
