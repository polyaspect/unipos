package unipos.common.remote.design;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.UrlContainer;

/**
 * Created by domin on 19.04.2016.
 */
@Service
public class DesignRemoteInterfaceImpl implements DesignRemoteInterface {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String getCurrentVersion() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.DESIGN_CURRENT_VERSION, String.class);
    }
}
