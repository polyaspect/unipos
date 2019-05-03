package unipos.auth.components.authentication;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Dominik on 26.07.2015.
 */
@Component
public class RestClient {

    private RestTemplate restTemplate = new RestTemplate();

    public List<Module> getModules() {
        Module[] modulesArrays = restTemplate.getForObject("/modules", Module[].class);
        List<Module> moduleList = Arrays.asList(modulesArrays);

        return moduleList;
    }

    public RestTemplate getRestTemplate() {
        return restTemplate;
    }
}
