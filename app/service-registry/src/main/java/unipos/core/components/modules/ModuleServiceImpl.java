package unipos.core.components.modules;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.KeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.User;
import unipos.core.components.update.UpdateService;
import unipos.licenseChecker.component.exception.LicenseErrorException;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

/**
 * @author Dominik
 * 
 */
@Service
public class ModuleServiceImpl implements ModuleService
{
	@Autowired
	private ModuleRepository moduleRepository;

    @Autowired
    private UpdateService updateService;

    @Autowired
    AuthRemoteInterface authRemoteInterface;

    @Override
    public List<Module> getAllModules(String authToken) throws Exception {
        List<Module> runningModules = getRunningModules();

        User user = authRemoteInterface.findUserByAuthToken(authToken);

        List<Module> installedModules = new ArrayList<Module>();
        for(Map.Entry<String, String> module : updateService.getCurrentModuleVersionsInstalledForCompany(user.getCompanyGuid()).entrySet())
        {
            String moduleName, version;

            moduleName = module.getKey();
            version = module.getValue();

            installedModules.add(Module.builder().name(moduleName).status(ModuleStatus.INSTALLED).installedVersion(version).build());
        }

        List<Module> allModules = new ArrayList<Module>();
        for(Module i : installedModules){
            boolean runningModuleFound = false;
            for(Module r : runningModules){
                if(i.getName().contentEquals(r.getName())){
                    runningModuleFound = true;
                    String moduleName, version, context, status;
                    allModules.add(Module.builder().name(i.getName()).installedVersion(i.getInstalledVersion()).context(r.getContext()).status(r.getStatus()).build());
                }
            }
            if(!runningModuleFound){
                allModules.add(Module.builder().name(i.getName()).installedVersion(i.getInstalledVersion()).status(i.getStatus()).build());
            }
        }

        return allModules;
    }

    @Override
    public List<Module> getRunningModules() throws Exception{
        String managerUrl = "http://localhost:8080/manager/text/list";

        String plainCreds = "unipos:unipos";
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        HttpEntity<String> tomcatRequest = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = null;
        try {
            response = restTemplate.exchange(managerUrl, HttpMethod.GET, tomcatRequest, String.class);
        } catch (Exception ex) {
            throw new RestClientException("Error with trying to access URL '/manager/text/list'. Make sure user unipos:unipos has role 'manager-script'.\n" +
                    "You can change this setting by inserting the lines '<role rolename=\"manager-script\"/>\n" +
                    "  <user username=\"unipos\" password=\"unipos\" roles=\"manager-script\"/>' \ninto the file '[TOMCAT]/conf/tomcat-users.xml'.");
        }

        return Arrays.stream(response.getBody().split("\n")).filter(x -> x.startsWith("/")).map(line -> {
            String context, status, module;
            context = line.split(":")[0];

            status = line.split(":")[1];
            module = line.split(":")[0].equals("/") ? "core" : line.split(":")[0].replace("/", "");
            return Module.builder().name(module).status(ModuleStatus.valueOf(status.toUpperCase())).context(context).build();
        }).collect(toList());
    }

    @Override
    public ModuleStatus getModuleStatusOfRunningModule(String moduleName) throws Exception {
        return getRunningModules().stream().filter(x -> x.getName().equalsIgnoreCase(moduleName)).findFirst().orElseGet(() -> Module.builder().status(ModuleStatus.UNKNOWN).build()).getStatus();
    }

    @Override
    public ModuleStatus getModuleStatus(String authToken, String moduleName) throws Exception {
        return getAllModules(authToken).stream().filter(x -> x.getName().equalsIgnoreCase(moduleName)).findFirst().orElseThrow(() -> new DataRetrievalFailureException("No Module with the name '" + moduleName + "' found in the servlet!")).getStatus();
    }
}
