package unipos.socket.components.socket.socket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.socket.components.workstation.WorkstationService;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 16.01.2016.
 */
@Configuration
public class SocketServiceMock {

    @Bean
    public WorkstationService workstationService() {
        return mock(WorkstationService.class);
    }
}
