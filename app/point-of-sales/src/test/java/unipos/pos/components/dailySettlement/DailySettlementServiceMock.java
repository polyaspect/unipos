package unipos.pos.components.dailySettlement;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
public class DailySettlementServiceMock {
    @Bean
    public DailySettlementService dailySetllementService(){
        return mock(DailySettlementService.class);
    }
}
