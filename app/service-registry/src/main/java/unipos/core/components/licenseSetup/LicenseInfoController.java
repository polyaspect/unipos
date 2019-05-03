package unipos.core.components.licenseSetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Company;
import unipos.core.components.update.UpdateService;
import unipos.licenseChecker.component.FileContainer;
import unipos.licenseChecker.component.LicenseChecker;
import unipos.licenseChecker.component.LicenseVerification;
import unipos.licenseChecker.component.exception.LicenseErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by domin on 13.02.2016.
 */

@RestController
@RequestMapping("/licenseManagement")
public class LicenseInfoController {

    @Autowired
    LicenseInfoService licenseInfoService;
    @Autowired
    UpdateService updateService;
    @Autowired
    LicenseChecker licenseChecker;

    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    AuthRemoteInterface authRemoteInterface;

    @RequestMapping(value = "/doesUserHaveValidLicense", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean doesUserHaveValidLicense(HttpServletRequest request) throws Exception {
        return true;

        /* LICENSE MANAGEMENT IS DEACTIVATED IN OPEN SOURCE VARIANT:
        String authToken = RequestHandler.getAuthToken(request);

        Assert.notNull(authToken, "The AuthToken must not be null!");

        User user = authRemoteInterface.findUserByAuthToken(authToken);
        if(user.getGuid().equals("00000000-580a-46e2-9d2a-590fd403910e"))
            return true;


        Company company = dataRemoteInterface.getCompanyByGuid(user.getCompanyGuid());
        Assert.notNull(company, "No company found for the given companyGuid");

        LicenseVerification verification = licenseChecker.getLicenseInfos(company.getName());

        return verification != null;
        */

    }

    @RequestMapping(value = "/doesLicenseFileExist", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public boolean licenseFileExists() {
        // return licenseInfoService.isLicenseFileExisting();

        // LICENSE MANAGEMENT IS DEACTIVATED IN OPEN SOURCE VARIANT:
        return true;
    }

    @RequestMapping(value = "/activate", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void activateProductByActivationCode(@RequestBody String activationCode, HttpServletResponse response) throws IOException, GeneralSecurityException, URISyntaxException, LicenseErrorException {
        try {

            licenseInfoService.activateProduct(activationCode);
        } catch (LicenseErrorException e) {
            response.sendError(500, e.getMessage());
        }
    }

    @RequestMapping(value = "/install", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void install(@RequestBody String activationCode, HttpServletResponse response) throws IOException, GeneralSecurityException, URISyntaxException, LicenseErrorException {
        try {
            List<String> modules = updateService.getModuleNamesForActicationCode(activationCode);
            for (String module : modules.stream().filter(item -> !item.equalsIgnoreCase("CORE") && !item.equalsIgnoreCase("ROOT") && !item.equalsIgnoreCase("UPDATER")).collect(Collectors.toList())) {
                module = module.replace("unipos-", "");
                if (FileContainer.isWarDeployed(module)) {
                    updateService.undeployModuleByContextPath(module);
                }
                updateService.downloadModuleByName(module);
            }
            if (modules.stream().filter(x -> x.equalsIgnoreCase("UPDATER")).count() > 0) {
                updateService.updateUpdater();
            }
            updateService.deployWarFiles();
        } catch (LicenseErrorException e) {
            response.sendError(500, e.getMessage());
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<LicenseInfo> findAll() {
        return licenseInfoService.findAll();
    }
}
