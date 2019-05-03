package unipos.core.components.licenseSetup;

import unipos.licenseChecker.component.exception.LicenseErrorException;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.List;

/**
 * Created by domin on 13.02.2016.
 */
public interface LicenseInfoService {

    void saveLicenseInfo(LicenseInfo licenseInfo);
    LicenseInfo getLicenseInfoByActivationCode(String activationCode);

    boolean isLicenseFileExisting();

    void activateProduct(String activationCode) throws IOException, GeneralSecurityException, URISyntaxException, LicenseErrorException;

    List<LicenseInfo> findAll();
}
