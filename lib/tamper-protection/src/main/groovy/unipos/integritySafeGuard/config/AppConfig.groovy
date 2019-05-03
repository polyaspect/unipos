package unipos.integritySafeGuard.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Created by domin on 05.09.2016.
 */
@Configuration
class AppConfig {

    @Bean
    public Setup setup() {
        new Setup()
    }
}
