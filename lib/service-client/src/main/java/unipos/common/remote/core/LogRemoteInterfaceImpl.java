package unipos.common.remote.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.UrlContainer;
import unipos.common.remote.core.model.LogDto;

/**
 * Created by Gradnig on 07.11.2015.
 */
@Service
public class LogRemoteInterfaceImpl implements LogRemoteInterface {


    private RestTemplate restTemplate = new RestTemplate();

    @Override
    public int log(LogDto log) {
        try
        {
            restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.CORE_ADD_LOG, log, Void.class);
            return 200;
        }
        catch(Exception ex)
        {
            return 0;
        }
    }
}
