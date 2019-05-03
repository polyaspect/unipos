package unipos.auth.components.user.usernamePassword;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 01.06.2015.
 */

@Configuration
public class UsernamePasswordServiceMock {

    @Bean
    public UsernamePasswordService usernamePasswordService() {
        return mock(UsernamePasswordService.class);
    }

}
