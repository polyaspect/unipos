package unipos.core.components.modules;

import org.springframework.web.client.RestClientException;
import unipos.licenseChecker.component.exception.LicenseErrorException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author ggradnig
 */
public interface ModuleService {

    List<Module> getAllModules(String authToken) throws Exception;

    List<Module> getRunningModules() throws Exception;

    ModuleStatus getModuleStatusOfRunningModule(String moduleName) throws Exception;

    ModuleStatus getModuleStatus(String authToken, String moduleName) throws Exception;
}
