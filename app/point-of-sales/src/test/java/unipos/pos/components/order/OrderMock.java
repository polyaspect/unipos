package unipos.pos.components.order;

import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterface;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
public class OrderMock {
    @Bean
    public OrderService orderService(){
        return mock(OrderService.class);
    }
    @Bean
    public AuthRemoteInterface tokenRemoteInterface() {
        return mock(AuthRemoteInterface.class);
    }
    @Bean
    public PosRemoteInterface posRemoteInterface() {
        return mock(PosRemoteInterface.class);
    }
    @Bean
    public LogRemoteInterface logRemoteInterface(){
        return mock(LogRemoteInterface.class);
    }
    @Bean
    public SocketRemoteInterface socketRemoteInterface(){
        return mock(SocketRemoteInterface.class);
    }
    @Bean
    public DataRemoteInterface dateRemoteInterface() {
        return mock(DataRemoteInterface.class);
    }
    @Bean
    public Scheduler scheduler() {
        return mock(Scheduler.class);
    }
}
