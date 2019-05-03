package unipos.data.components.company;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * Created by Dominik on 23.07.2015.
 */

@Configuration
public class StoreServiceMock {

    @Bean
    public StoreService storeService() {
        return mock(StoreService.class);
    }

}
