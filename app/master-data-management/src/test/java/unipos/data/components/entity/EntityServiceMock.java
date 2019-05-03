package unipos.data.components.entity;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
public class EntityServiceMock {
    @Bean
    public EntityService entityService(){
        return mock(EntityService.class);
    }
}
