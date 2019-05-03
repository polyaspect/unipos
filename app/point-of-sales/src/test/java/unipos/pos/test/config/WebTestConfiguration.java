package unipos.pos.test.config;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import unipos.common.mapping.LocalDateTimeConverter;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.report.ReportRemoteInterface;
import unipos.pos.components.sequence.SequenceServiceMock;

import java.util.Arrays;

import static org.mockito.Mockito.mock;

/**
 * @author ggradnig
 */

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"unipos.pos.components"}, excludeFilters = {
        @ComponentScan.Filter(type = FilterType.ANNOTATION, value = Service.class)})
@Import({SequenceServiceMock.class})
public class WebTestConfiguration extends WebMvcConfigurationSupport {

    @Bean
    public DozerBeanMapper dozerBeanMapper() {
        DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList("dozerMapping.xml"));
        mapper.setCustomConverters(Arrays.asList(new LocalDateTimeConverter()));
        return mapper;
    }

    @Bean
    public ReportRemoteInterface reportRemoteInterface() {
        return mock(ReportRemoteInterface.class);
    }

    @Bean
    public DataRemoteInterface dataRemoteInterface() {
        return mock(DataRemoteInterface.class);
    }
}
