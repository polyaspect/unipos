package unipos.integritySafeGuard.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import unipos.integritySafeGuard.SmartCardFixture
import unipos.integritySafeGuard.event.ScDetachedCommand
import unipos.integritySafeGuard.event.ScInsertedCommand
import unipos.integritySafeGuard.event.ScRemovedCommand
import unipos.integritySafeGuard.smartcard.SmartCardHandler

import static org.mockito.Mockito.mock

/**
 * Created by domin on 05.09.2016.
 */
@Configuration
class TestConfiguration {

    @Bean
    public Setup setup() {
        new Setup()
    }

    @Bean
    public SmartCardFixture smartCardFixture() {
        return new SmartCardFixture();
    }

    @Bean
    public ScInsertedCommand scInsertedCommand() {
        mock(ScInsertedCommand.class)
    }

    @Bean
    public ScRemovedCommand scRemovedCommand() {
        mock(ScRemovedCommand.class)
    }

    @Bean
    public ScDetachedCommand scDetachedCommandommand() {
        mock(ScDetachedCommand.class)
    }

    @Bean
    public SmartCardHandler smartCardHandler() {
        return new SmartCardHandler()
    }

}