package unipos.auth.components.user;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.sync.SyncRemoteInterface;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 27.05.2015.
 */
@Configuration
public class UserServiceMock {

    @Bean
    public UserService userService() {
        return mock(UserService.class);
    }

    @Bean
    public SyncRemoteInterface syncRemoteInterface() {return mock(SyncRemoteInterface.class);}

    @Bean
    public LogRemoteInterface logRemoteInterface() {return mock(LogRemoteInterface.class);}

}
