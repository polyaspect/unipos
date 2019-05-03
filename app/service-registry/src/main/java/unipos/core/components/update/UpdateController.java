package unipos.core.components.update;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;
import unipos.licenseChecker.component.exception.LicenseErrorException;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by domin on 07.03.2016.
 */
@RestController
@RequestMapping("/update")
public class UpdateController {

    @Autowired
    UpdateRepository updateRepository;
    @Autowired
    UpdateService updateService;
    @Autowired
    LogRemoteInterface logRemoteInterface;
    @Autowired
    AuthRemoteInterface authRemoteInterface;

    @RequestMapping(value = "/updatableModules", method = RequestMethod.GET)
    public List<String> showToUpdateModules() {
        try {
            List<String> result = new ArrayList<>();

            logRemoteInterface.log(new LogDto(LogDto.Level.INFO, "Showing updatable modues", this.getClass().getSimpleName(), LocalDateTime.now(), null));
            Map<String,String> updatableModules = updateRepository.checkForNewLicenseFile(false);

            updatableModules.forEach((k,v) -> {
                result.add(k+":"+v);
            });

            return result;
        } catch (Exception ex) {
            LogDto logDto = new LogDto();
            logDto.addExceptionParameters(ex);
            logDto.setDateTime(LocalDateTime.now());
            logDto.setLevel(LogDto.Level.ERROR);
            logDto.setMessage(ex.getMessage());
            logRemoteInterface.log(logDto);
            return null;
        }
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public List<String> downloadUpdate() throws IOException, GeneralSecurityException, LicenseErrorException {
        List<String> result = new ArrayList<>();

        try {
            logRemoteInterface.log(new LogDto(LogDto.Level.INFO, "Checking for updates", this.getClass().getSimpleName(), LocalDateTime.now(), null));
            Map<String,String> toUpdateModules = updateRepository.checkForNewLicenseFile(true);
            logRemoteInterface.log(new LogDto(LogDto.Level.INFO, toUpdateModules.size() + " updates are available", this.getClass().getSimpleName(), LocalDateTime.now(), null));

            if(toUpdateModules.size() > 0) {
                toUpdateModules.forEach((moduleName,moduleVersion) -> {
                    moduleName = moduleName.replace("unipos-", "");
                    moduleVersion = moduleVersion.isEmpty() ? null : moduleVersion;
                    if(!moduleName.equalsIgnoreCase("CORE") && !moduleName.equalsIgnoreCase("ROOT") && !moduleName.equalsIgnoreCase("UPDATER")) {
                        if(!moduleName.equalsIgnoreCase("ROOT") && !moduleName.equalsIgnoreCase("CORE")) {
                            try {
                                logRemoteInterface.log(new LogDto(LogDto.Level.INFO, "Downloading " + moduleName, this.getClass().getSimpleName(), LocalDateTime.now(), null));
                                updateService.downloadModuleByNameAndVersion(moduleName, moduleVersion);
                                logRemoteInterface.log(new LogDto(LogDto.Level.INFO, "Undeploying " + moduleName, this.getClass().getSimpleName(), LocalDateTime.now(), null));
                                updateService.undeployModuleByContextPath(moduleName);
                                logRemoteInterface.log(new LogDto(LogDto.Level.INFO, "Deploying " + moduleName, this.getClass().getSimpleName(), LocalDateTime.now(), null));
                                updateService.deployWarFileByName(moduleName);
                            } catch (Exception ignored) {}
                        }
                    }
                });

                toUpdateModules.forEach((moduleName,moduleVersion) -> {
                    moduleName = moduleName.replace("unipos-", "");
                    if(moduleName.equalsIgnoreCase("UPDATER")) {
                        try {
                            logRemoteInterface.log(new LogDto(LogDto.Level.INFO, "Updating the Updater" , this.getClass().getSimpleName(), LocalDateTime.now(), null));
                            updateService.updateUpdater();
                        } catch (Exception ignored) {}
                    }
                });

                toUpdateModules.forEach((moduleName,moduleVersion) -> {
                    moduleName = moduleName.replace("unipos-", "");
                    if(moduleName.equalsIgnoreCase("ROOT") || moduleName.equalsIgnoreCase("CORE")) {
                        try {
                            logRemoteInterface.log(new LogDto(LogDto.Level.INFO, "Downloading CORE", this.getClass().getSimpleName(), LocalDateTime.now(), null));
                            updateService.downloadModuleByNameAndVersion("ROOT", moduleVersion);
                            logRemoteInterface.log(new LogDto(LogDto.Level.INFO, "Triggering CORE update", this.getClass().getSimpleName(), LocalDateTime.now(), null));
                            updateService.triggerRootUpdate();
                        } catch (Exception ignored) {}
                    }
                });

                logRemoteInterface.log(new LogDto(LogDto.Level.INFO, "Update successful", this.getClass().getSimpleName(), LocalDateTime.now(), null));
            }

            toUpdateModules.forEach((k,v) -> {
                result.add(k+":"+v);
            });

            return result;
        } catch (Exception ex) {
            LogDto logDto = new LogDto();
            logDto.addExceptionParameters(ex);
            logDto.setDateTime(LocalDateTime.now());
            logDto.setLevel(LogDto.Level.ERROR);
            logDto.setMessage(ex.getMessage());
            logRemoteInterface.log(logDto);
            return null;
        }
    }

    @RequestMapping(value = "/undeploy/{contextPath}", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void undeployWebApp(@PathVariable("contextPath") String contextPath) {
        Assert.notNull(contextPath, "The context path must not be 'null'");

        updateService.undeployModuleByContextPath(contextPath);
    }

    @RequestMapping(value = "/getCurrentModuleVersionsInstalled", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,String> getCurrentModuleVersions(HttpServletRequest request) throws GeneralSecurityException, LicenseErrorException, IOException, URISyntaxException {
        String authToken = RequestHandler.getAuthToken(request);

        Assert.notNull(authToken, "The AuthToken must not be null!");

        User user = authRemoteInterface.findUserByAuthToken(authToken);

        Assert.notNull(user, "No valid User found for the given AuthToken!");
        Assert.notNull(user.getGuid(), "No CompanyGuid found for the given User!");

        return updateService.getCurrentModuleVersionsInstalledForCompany(user.getCompanyGuid());
    }

    @RequestMapping(value = "/getCurrentModuleVersionsLicense", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public Map<String,String> getCurrentModuleVersionsFromLicense(HttpServletRequest request) throws GeneralSecurityException, LicenseErrorException, IOException, URISyntaxException {
        String authToken = RequestHandler.getAuthToken(request);

        Assert.notNull(authToken, "The AuthToken must not be null!");

        User user = authRemoteInterface.findUserByAuthToken(authToken);

        Assert.notNull(user, "No valid User found for the given AuthToken!");
        Assert.notNull(user.getGuid(), "No CompanyGuid found for the given User!");

        return updateService.getCurrentModuleVersionsFromLicense(user.getCompanyGuid());
    }
}
