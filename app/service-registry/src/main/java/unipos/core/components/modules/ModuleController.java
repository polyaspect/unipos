package unipos.core.components.modules;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.*;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.Right;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.licenseChecker.component.exception.LicenseErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * @author Dominik
 **/

@RestController
@RequestMapping(value = "/modules")
@Api(value = "/modules")
@PropertySource("classpath:application.properties")
public class ModuleController {

    @Autowired
    ModuleService moduleService;
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    private LogRemoteInterface logRemoteInterface;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    @ApiOperation(value = "All started modules",
            response = Module.class,
            responseContainer = "List")
    public List<Module> all(HttpServletRequest request) throws Exception {
        return moduleService.getAllModules(RequestHandler.getAuthToken(request));
    }

    @RequestMapping(value = "/started", method = RequestMethod.GET)
    @ApiOperation(value = "All started modules",
            response = Module.class,
            responseContainer = "List")
    public List<Module> started(HttpServletRequest request) throws Exception {
        return moduleService.getRunningModules();
    }

    @RequestMapping(value = "/status/{moduleName}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ModuleStatus getModuleStatus(HttpServletRequest request, HttpServletResponse response, @PathVariable("moduleName") String moduleName) throws Exception {
        Assert.notNull(moduleName, "The param 'moduleName' must not be null");

        String authToken = "";
        try{
            authToken = RequestHandler.getAuthToken(request);
        }
        catch(Exception ex){
            return moduleService.getModuleStatusOfRunningModule(moduleName);
        }
        return moduleService.getModuleStatus(authToken, moduleName);
    }

    @RequestMapping(value = "/permissions", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void propagatePermissions(HttpServletRequest request, @RequestBody List<Right> rights) throws Exception {
        Assert.notNull(rights, "The param 'permissions' must not be null!");

        while (!moduleService.getModuleStatusOfRunningModule("auth").equals(ModuleStatus.RUNNING)) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        authRemoteInterface.addRights(rights);
    }
}
