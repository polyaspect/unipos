package unipos.common.remote.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.UrlContainer;
import unipos.common.remote.auth.model.Right;
import unipos.common.remote.core.model.LicenseInfo;
import unipos.common.remote.core.model.Module;
import unipos.common.remote.core.model.ModuleStatus;
import unipos.common.remote.data.model.company.Store;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by Joyce on 10.10.2015.
 */
@Service
public class CoreRemoteInterfaceImpl implements CoreRemoteInterface {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<Module> startedModules() {
        Module[] startedModules =
                restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.CORE_STARTED_MODULES, Module[].class);
        return Arrays.asList(startedModules);
    }

    @Override
    public boolean licenseFileExists() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.CORE_LICENSE_FILE_EXISTS, Boolean.class);
    }

    @Override
    public List<LicenseInfo> getAllLicenceInfos() {
        return Arrays.asList(restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.CORE_FIND_ALL_LICENSEINFOS, LicenseInfo[].class));
    }

    @Override
    public void addNewAutoUpdaterSettlementDateTime(Store mapedStore) {
        restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.CORE_ADD_NEW_UPDATING_TIME, mapedStore, Void.class);
    }

    @Override
    public void deleteAutoUpdateSchedulerOfStore(Store mapedStore) {
        restTemplate.delete(UrlContainer.BASEURL + UrlContainer.CORE_AUTOUPDATE_DELETE_BY_STORE_GUID + "/" + mapedStore.getGuid(), mapedStore, Void.class);
    }

    @Override
    public String getCurrentVersion() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.CORE_CURRENT_VERSION, String.class);
    }

    public ModuleStatus getModuleStatus(String moduleName) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        ResponseEntity<ModuleStatus> responseEntity = null;

        try{
            responseEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.CORE_MODULE_STATUS, HttpMethod.GET, new HttpEntity<>(headers), ModuleStatus.class, moduleName);
        }
        catch(Exception ex){
            return ModuleStatus.UNKNOWN;
        }

        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return ModuleStatus.UNKNOWN;
        }
    }

    @Override
    public void addPermissions(List<Right> rights) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        HttpEntity<List<Right>> request = new HttpEntity<>(rights, headers);

        ResponseEntity responseEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.CORE_ADD_PERMISSIONS, HttpMethod.POST, request, Void.class);

        if(!responseEntity.getStatusCode().is2xxSuccessful()) {
            throw new RestClientException("Unable to propagate the Permissions to the auth module");
        }
    }
}
