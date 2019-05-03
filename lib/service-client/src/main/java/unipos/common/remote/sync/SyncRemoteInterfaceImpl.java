package unipos.common.remote.sync;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.GSonHolder;
import unipos.common.container.UrlContainer;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Syncable;
import unipos.common.remote.sync.model.Target;

import javax.servlet.ServletContext;
import java.nio.charset.Charset;

/**
 * Created by dominik on 01.11.15.
 */

@Service
public class SyncRemoteInterfaceImpl implements SyncRemoteInterface {

    @Autowired
    ServletContext servletContext;

    @Autowired
    RestTemplate restTemplate;

    Gson gson = GSonHolder.serializeDateGson();

    @Override
    public void syncChanges(Syncable data, Target target) throws Exception {


        if (target != null) {

            MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
            mvm.add("target", target.name());
            mvm.add("data", new String(gson.toJson(data).getBytes("UTF-8"), Charset.forName("UTF-8")));

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            HttpEntity httpEntity = new HttpEntity(mvm, headers);

            restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.SYNC_SYNCCHANGES, httpEntity, Void.class);
        }
    }

    @Override
    public void syncChanges(Syncable data, Target target, Action action) throws Exception {
        if (target != null && action != null && data != null) {

            MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
            mvm.add("target", target.name());
            mvm.add("data", new String(gson.toJson(data).getBytes("UTF-8"), Charset.forName("UTF-8")));
            mvm.add("action", action.name());

            MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
            headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            HttpEntity httpEntity = new HttpEntity(mvm, headers);
            try {
                restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.SYNC_SYNCCHANGESACTION, httpEntity, Void.class);
            } catch (Exception ex) {
                //ToDo: Handle sync errors only if sync module is enabled
            }
        } else {
            System.err.println("Could not sync the action: Target --> " + target + ", Action --> " + action + ", data --> " + data);
        }
    }

    @Override
    public String getCurrentVersion() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.SYNC_CURRENT_VERSION, String.class);
    }
}
