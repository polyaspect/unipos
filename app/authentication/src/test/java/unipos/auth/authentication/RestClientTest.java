package unipos.auth.authentication;

import com.google.gson.Gson;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestTemplate;
import unipos.auth.components.authentication.Module;
import unipos.auth.components.authentication.RestClient;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

/**
 * Created by Dominik on 26.07.2015.
 */

@ContextConfiguration(classes = RestClientConfiguration.class)
public class RestClientTest extends AbstractJUnit4SpringContextTests {


    @Autowired
    private RestClient restClient;

    Gson gson = new Gson();

    @Test
    public void testModuleList() throws Exception {

        List<Module> modules = Arrays.asList(new Module("auth", "/auth"), new Module("pos", "/pos"), new Module("data", "/data"));

        MockRestServiceServer mockServer = MockRestServiceServer.createServer(restClient.getRestTemplate());
        mockServer.expect(requestTo("/modules"))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(gson.toJson(modules), MediaType.APPLICATION_JSON));


        modules = restClient.getModules();

        assertThat(modules.size(), is(3));

        assertThat(modules.get(0).getName(), is("auth"));
        assertThat(modules.get(0).getAdresse(), is("/auth"));

        assertThat(modules.get(1).getName(), is("pos"));
        assertThat(modules.get(1).getAdresse(), is("/pos"));

        assertThat(modules.get(2).getName(), is("data"));
        assertThat(modules.get(2).getAdresse(), is("/data"));

        mockServer.verify();
    }



}
