package unipos.report.config.web;

import org.dozer.DozerBeanMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.mvc.WebContentInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import unipos.common.mapping.LocalDateTimeConverter;
import unipos.report.config.DBInitializer;

import java.util.Arrays;

/**
 * @author Siva
 * 
 */
@Configuration
@ComponentScan(basePackages = {"unipos.report.components", "unipos.common", "unipos.authChecker"})
@EnableWebMvc
public class WebConfiguration extends WebMvcConfigurerAdapter
{

	@Override
	public void addViewControllers(ViewControllerRegistry registry)
	{
		super.addViewControllers(registry);
		registry.addViewController("/").setViewName("index");
	}

	@Bean
	public ViewResolver resolver()
	{
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

	@Bean
	public DozerBeanMapper dozerBeanMapper() {
		DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList("dozerMapping.xml"));
		mapper.setCustomConverters(Arrays.asList(new LocalDateTimeConverter()));
		return mapper;
	}

	@Bean
	public DBInitializer dbInitializer() {
		return new DBInitializer();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(webContentInterceptor());
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/**").addResourceLocations("/").setCachePeriod(0);
	}

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer)
	{
		configurer.enable();
	}
	
}
