package unipos.pos.components.timeTask;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import unipos.pos.components.dailySettlement.timer.TimeTaskService;
import unipos.pos.components.orderItem.OrderItemService;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
public class TimeTaskServiceMock {
    @Bean
    public TimeTaskService timeTaskService(){
        return mock(TimeTaskService.class);
    }
}
