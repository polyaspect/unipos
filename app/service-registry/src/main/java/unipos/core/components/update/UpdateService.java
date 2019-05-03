package unipos.core.components.update;

import unipos.licenseChecker.component.exception.LicenseErrorException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

/**
 * Created by domin on 03.04.2016.
 */
public interface UpdateService {

    void downloadModuleByName(String moduleName) throws IOException, GeneralSecurityException, LicenseErrorException;

    void downloadModuleByNameAndVersion(String moduleName, String versionNumber) throws IOException, GeneralSecurityException, LicenseErrorException;

    List<String> getModuleNamesForActicationCode(String activationCode) throws GeneralSecurityException, IOException;

    void deployWarFiles();

    void undeployModuleByName(String moduleName);

    void deployWarFileByName(String moduleName) throws IOException;

    void undeployModuleByContextPath(String contextPath) throws IllegalArgumentException;

    void updateUpdater() throws IOException, GeneralSecurityException;

    void triggerRootUpdate() throws IOException;

    Map<String,String> getCurrentModuleVersionsInstalledForCompany(String companyGuid) throws URISyntaxException, GeneralSecurityException, LicenseErrorException, IOException;

    Map<String,String> getCurrentModuleVersionsFromLicense(String companyGuid) throws URISyntaxException, GeneralSecurityException, LicenseErrorException, IOException;

    /**
     * Checks the content of the license file against the real currently installed module versions.
     * If the license version is higher than the current version, the name of the module is returned
     * @return A list of module-name, where the license version is higher than the current installed version
     */
    List<String> checkLicenseVersionsAgainstModuleVersions();

    boolean isModuleInVersionAvailable(String moduleName, String moduleVersion);
}
