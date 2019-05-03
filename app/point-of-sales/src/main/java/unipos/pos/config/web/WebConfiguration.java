package unipos.pos.config.web;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import unipos.licenseChecker.component.ModuleName;
import unipos.common.mapping.LocalDateTimeConverter;
import unipos.pos.config.DBInitializer;

import java.util.Arrays;

/**
 * @author Siva
 */
@Configuration
@ComponentScan(basePackages = {"unipos.common", "unipos.pos.components", "unipos.licenseChecker", "unipos.authChecker"})
@EnableWebMvc
@PropertySource("classpath:application.properties")
public class WebConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        super.addViewControllers(registry);
        registry.addViewController("/").setViewName("index");
    }

    @Bean
    public ViewResolver resolver() {
        InternalResourceViewResolver url = new InternalResourceViewResolver();
        url.setPrefix("/");
        url.setSuffix(".html");
        return url;
    }

    @Bean
    public WebContentInterceptor webContentInterceptor() {
        WebContentInterceptor webContentInterceptor = new WebContentInterceptor();
        webContentInterceptor.setCacheSeconds(0);
        webContentInterceptor.setUseExpiresHeader(true);
        webContentInterceptor.setUseCacheControlHeader(true);
        webContentInterceptor.setUseCacheControlNoStore(true);
        return webContentInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(webContentInterceptor());
    }

    @Bean
    public DozerBeanMapper dozerBeanMapper() {
        DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList("dozerMapping.xml"));
        mapper.setCustomConverters(Arrays.asList(new LocalDateTimeConverter()));
        return mapper;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("/").setCachePeriod(0);
    }

    @Bean
    @Scope(value = "singleton")
    public SchedulerFactoryBean schedulerFactoryBean() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setSchedulerName("RAMScheduler");
        return schedulerFactoryBean;
    }

    @Override
    public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
        configurer.enable();
    }

    @Bean
    public DBInitializer dbInitializer() {
        return new DBInitializer();
    }

    @Bean
    public TimeTaskInitializer timeTaskInitializer() {
        return new TimeTaskInitializer();
    }

    @Bean
    public ModuleName moduleName() {
        ModuleName moduleName = ModuleName.builder().name("unipos-pos").build();

        return moduleName;
    }

}
