package unipos.core.components.licenseSetup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.GSonHolder;
import unipos.licenseChecker.component.*;
import unipos.licenseChecker.component.exception.LicenseErrorException;
import unipos.licenseChecker.computerId.ComputerIdCalculator;
import unipos.licenseChecker.computerId.Hardware4Nix;
import unipos.licenseChecker.computerId.Hardware4Win;
import unipos.licenseChecker.computerId.OsDetector;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSession;
import java.io.File;
import java.io.IOException;
import java.net.*;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.*;

/**
 * Created by domin on 13.02.2016.
 */

@SuppressWarnings("ResultOfMethodCallIgnored")
@Service
public class LicenseInfoServiceImpl implements LicenseInfoService {

    @Autowired
    LicenseInfoRepository licenseInfoRepository;
    @Autowired
    LicenseChecker licenseChecker;
    @Autowired
    Environment environment;
    @Autowired
    RestTemplate restTemplate;

    @Override
    public void saveLicenseInfo(LicenseInfo licenseInfo) {
        licenseInfoRepository.save(licenseInfo);
    }

    @Override
    public LicenseInfo getLicenseInfoByActivationCode(String activationCode) {
        return licenseInfoRepository.findByActivationCode(activationCode);
    }

    @Override
    public boolean isLicenseFileExisting() {
        List<File> licenses = FileContainer.getLicenseFiles();

        return licenses.stream().filter(File::isFile).count() > 0;
    }

    @Override
    public void activateProduct(String activationCode) throws IOException, GeneralSecurityException, URISyntaxException, LicenseErrorException {
        String updateServerUrl = environment.getProperty("update.url");

        if(licenseInfoRepository.findByActivationCode(activationCode) != null) {
            throw new LicenseErrorException("You already used the given ActicationCode on this machine.");
        }

        HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        //first of all get the license decryption key
        LicenseInfoEntity licenseInfoEntity = restTemplate.getForObject( updateServerUrl + "/licenses/getAuthenticationValueByActivationCode/" + activationCode, LicenseInfoEntity.class);
        String authPw = licenseInfoEntity.getEncryptionPw();

        String computerId = ComputerIdCalculator.calcComputerId();

        ActivationEntity activationEntity = ActivationEntity.builder()
                .activationCode(activationCode)
                .computerId(computerId)
                .build();

        //after that download the licenseFile
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.valueOf("application/zip")));
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(GSonHolder.serializeDateGson().toJson(activationEntity), headers);

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(updateServerUrl + "/licenses/licenseFileByActivationCode", HttpMethod.POST, entity, byte[].class);
//
//        //save the license file
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            //licensePackage Path
            File licensePackage = FileContainer.createNewLicenseFile();

            Files.write(licensePackage.toPath(), responseEntity.getBody());
            //publicKeyPath
            File publicKeyFile = FileContainer.getPublicKeyFile();

            LicenseVerification verification = licenseChecker.activateLicense(licensePackage, authPw, publicKeyFile);
            if (verification == null) {
                return;
            }

            licenseInfoRepository.save(LicenseInfo.builder().activationCode(activationCode).customerId(verification.getCustomerId()).companyName(licenseInfoEntity.getCompanyName()).build());
//        }
    }

    @Override
    public List<LicenseInfo> findAll() {
        return licenseInfoRepository.findAll();
    }
}
