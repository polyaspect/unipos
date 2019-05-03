package unipos.design.test.config;

import org.springframework.context.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"unipos.design.components"}, excludeFilters = {
        @ComponentScan.Filter(type= FilterType.ANNOTATION, value=Service.class)})
public class WebTestConfiguration extends WebMvcConfigurationSupport {
}
