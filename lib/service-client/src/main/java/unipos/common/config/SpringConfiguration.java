package unipos.common.config;

import com.sun.mail.iap.Protocol;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.client.AsyncClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.AsyncRestTemplate;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.KeyStore;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by Dominik on 03.12.2015.
 */

@Configuration
public class SpringConfiguration {

    @Bean
    public ClientHttpRequestFactory httpRequestFactory() throws Exception {
        HttpComponentsClientHttpRequestFactory rf = new HttpComponentsClientHttpRequestFactory(httpClient());
        rf.setReadTimeout(20000);
        rf.setConnectTimeout(20000);
        return rf;
    }

    @Bean
    public HttpClient httpClient() throws Exception {
        Path temp = Files.createTempFile("keystore-temp", ".jks");
        Files.copy(getClass().getClassLoader().getResourceAsStream("keystore.jks"), temp, StandardCopyOption.REPLACE_EXISTING);

//        SSLContext sslContext = new SSLContextBuilder()
//                .loadTrustMaterial(temp.toFile(), "unipos".toCharArray())
//                .build();

        SSLContext sslContext = SSLContext.getDefault();

        return HttpClients.custom().setSSLContext(sslContext).build();
    }


    @Bean
    public RestTemplate restTemplate() throws Exception {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setErrorHandler(new LogErrorHandler());
        restTemplate.setRequestFactory(httpRequestFactory());
        restTemplate.setInterceptors(Collections.singletonList(logRequestResponseInterceptor()));

        return restTemplate;
    }

    @Bean
    public AsyncRestTemplate asyncRestTemplate() throws Exception {
        AsyncRestTemplate restTemplate = new AsyncRestTemplate();

        restTemplate.setErrorHandler(new LogErrorHandler());

        return restTemplate;
    }

    @Bean
    public LogRequestResponseInterceptor logRequestResponseInterceptor() {
        return new LogRequestResponseInterceptor();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }


    @Bean
    public GlobalErrorHandler globalErrorHandler() {
        return new GlobalErrorHandler();
    }
}
