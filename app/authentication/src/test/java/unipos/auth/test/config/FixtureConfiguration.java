package unipos.auth.test.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.auth.components.user.UserFixture;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinFixture;
import unipos.auth.components.user.usernamePassword.UsernamePasswordFixture;

/**
 * @author ggradnig
 */
@Configuration
public class FixtureConfiguration {

    @Bean
    public UserFixture userFixture() {
        return new UserFixture();
    }

    @Bean
    public UsernamePasswordFixture usernamePasswordFixture() {return new UsernamePasswordFixture();}

    @Bean
    public MitarbeiteridPinFixture mitarbeiteridPinFixture() {return new MitarbeiteridPinFixture();}
}
