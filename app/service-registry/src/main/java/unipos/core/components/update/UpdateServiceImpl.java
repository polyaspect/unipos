package unipos.core.components.update;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.GSonHolder;
import unipos.common.container.PomVersionExplorer;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.design.DesignRemoteInterface;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.printer.PrinterRemoteInterface;
import unipos.common.remote.report.ReportRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.core.components.licenseSetup.LicenseInfo;
import unipos.core.components.licenseSetup.LicenseInfoRepository;
import unipos.licenseChecker.component.*;
import unipos.licenseChecker.component.exception.LicenseErrorException;
import unipos.licenseChecker.computerId.ComputerIdCalculator;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by domin on 03.04.2016.
 */
@Service
public class UpdateServiceImpl implements UpdateService {

    @Autowired
    LicenseInfoRepository licenseInfoRepository;
    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LicenseChecker licenseChecker;
    @Autowired
    Environment environment;
    @Autowired
    LogRemoteInterface logRemoteInterface;

    //RemoteInterfaces for aquiring Versions
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    DesignRemoteInterface designRemoteInterface;
    @Autowired
    PosRemoteInterface posRemoteInterface;
    @Autowired
    PrinterRemoteInterface printerRemoteInterface;
    @Autowired
    ReportRemoteInterface reportRemoteInterface;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;

    public void downloadModuleByName(String moduleName) throws IOException, GeneralSecurityException, LicenseErrorException {
        downloadModuleByNameAndVersion(moduleName, null);
    }

    @Override
    public void downloadModuleByNameAndVersion(String moduleName, String versionNumber) throws IOException, GeneralSecurityException, LicenseErrorException {

        String updateServerUrl = environment.getProperty("update.url");

        if(versionNumber != null && !versionNumber.isEmpty()) {
            //First of all check, if the given Module and its version are valid
            Assert.isTrue(isModuleInVersionAvailable(moduleName, versionNumber), "The module '" + moduleName + "' is not available in version '" + versionNumber + "'");
        }

        List<LicenseInfo> licenseInfos = licenseInfoRepository.findAll();
        if(licenseInfos == null || licenseInfos.size() == 0) {
            throw new LicenseErrorException("No licenseFound in the DB");
        }

        LicenseInfo licenseInfo = licenseInfos.stream().findFirst().get();

        String computerId = ComputerIdCalculator.calcComputerId();
        String decryptPw = LicenseInfoEntity.builder().companyName(licenseInfo.getCompanyName()).customerNumber(licenseInfo.getCustomerId()).build().getEncryptionPw();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

        MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
        mvm.add("moduleName", moduleName);
        mvm.add("licenseInfoEntity", GSonHolder.serializeDateGson().toJson(LicenseInfoEntity.builder().companyName(licenseInfo.getCompanyName()).customerNumber(licenseInfo.getCustomerId()).build()));
        mvm.add("deviceIdentifier", computerId);
        if(versionNumber != null && !versionNumber.isEmpty()) {
            mvm.add("version", versionNumber);
        }

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(mvm, headers);

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange(updateServerUrl + "/update/downloadModule", HttpMethod.POST, request, byte[].class);
//
//        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            File encryptedWarFile = FileContainer.createNewTempWarFile(moduleName, "");

            Files.write(encryptedWarFile.toPath(), responseEntity.getBody());
            if (licenseChecker.verifySign(encryptedWarFile)) {
                licenseChecker.deployWarFile(encryptedWarFile, decryptPw, moduleName);
            }
//        } else {
//            LogDto logDto = new LogDto();
//            logDto.setDateTime(LocalDateTime.now());
//            logDto.setLevel(LogDto.Level.ERROR);
//            logDto.setMessage(moduleName + "-Download not successful");
//            logDto.addParameter("HTTP-Status", String.valueOf(responseEntity.getStatusCode()));
//            logDto.addParameter("HTTP-Body", new String(responseEntity.getBody()));
//            logRemoteInterface.log(logDto);
//        }
    }

    @Override
    public List<String> getModuleNamesForActicationCode(String activationCode) throws GeneralSecurityException, IOException {
        LicenseInfo licenseInfo = licenseInfoRepository.findByActivationCode(activationCode);

        Assert.notNull(licenseInfo, "No valid LicenseInfo found for the given activationKey");

        String computerId = ComputerIdCalculator.calcComputerId();
        String decryptPw = LicenseInfoEntity.builder().companyName(licenseInfo.getCompanyName()).customerNumber(licenseInfo.getCustomerId()).build().getEncryptionPw();

        List<File> licenseFiles = FileContainer.getLicenseFiles();

        //This is a list of the already installed modules.
        List<String> oldModules = new ArrayList<>();

        //First get a list of the already installed modules. Just look in the licenseFiles and just sum them up.
        for (File licenseFile : licenseFiles) {
            List<LicenseInfo> licenseInfos = licenseInfoRepository.findAll().stream().filter(x -> !x.getActivationCode().equals(activationCode)).collect(Collectors.toList());
            for (LicenseInfo info : licenseInfos) {
                try {
                    String oldDecryptPw = LicenseInfoEntity.builder().companyName(info.getCompanyName()).customerNumber(info.getCustomerId()).build().getEncryptionPw();

                    LicenseVerification licenseVerification = licenseChecker.decryptLicenseFile(oldDecryptPw, licenseFile);
                    if(!licenseVerification.getActivationCode().equals(activationCode)) {

                        oldModules.addAll(licenseVerification.getModules().stream()
                                .filter(module -> module.getDevices().stream().filter(device -> device.getComputerId().equals(computerId)).count() > 0)
                                .map(x -> x.getName().replace("unipos-", "")).collect(Collectors.toList()));
                    }
                } catch (Exception ignored) {
                    //We skip this exception, because it just means that the try was invalid. We brute force all our posibilities
                }
            }

        }

        //Distinct the list that we don't have double entries.
        oldModules = oldModules.stream().distinct().collect(Collectors.toList());

        //Now check the new licenseFile against the oldModules. So we only download the new modules ;)
        for (File licenseFile : licenseFiles) {
            try {
                LicenseVerification licenseVerification = licenseChecker.decryptLicenseFile(decryptPw, licenseFile);
                if (licenseVerification.getActivationCode().equals(activationCode)) {
                    List<String> newModulesForClient = licenseVerification.getModules().stream()
                            .filter(module -> module.getDevices().stream().filter(device -> device.getComputerId().equals(computerId)).count() > 0)
                            .map(x -> x.getName().replace("unipos-", ""))
                            .collect(toList());

                    return ListUtils.subtract(newModulesForClient, oldModules);
                }
            } catch (Exception ex) {
                //We skip this exception, because it just means that the try was invalid. We brute force all our posibilities
            }

        }
        return new ArrayList<>();
    }

    @Override
    public void deployWarFiles() {
        List<File> deployableWarFiles = Arrays.asList(FileContainer.getDeployableWarDirectory().listFiles());

        File webappsDirectory = FileContainer.getWebappsDirectory();
        List<File> webappWars = Arrays.asList(webappsDirectory.listFiles());

        List<File> toRemove = webappWars.stream().filter(x -> deployableWarFiles.stream().filter(y -> FilenameUtils.getBaseName(y.getAbsolutePath()).equals(FilenameUtils.getBaseName(x.getAbsolutePath()))).count() > 0).collect(toList());
        toRemove.forEach(File::delete);

        deployableWarFiles.forEach(x -> {
            try {
                Files.move(x.toPath(), new File(webappsDirectory.getAbsolutePath() + "/" + x.getName()).toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    @Override
    public void undeployModuleByName(String moduleName) {
        File webappsDirectory = FileContainer.getWebappsDirectory();

        List<File> moduleWars = Arrays.stream(webappsDirectory.listFiles()).filter(file ->
                (FilenameUtils.getBaseName(file.getAbsolutePath()).equals(moduleName.replace("unipos-", "")) &&
                        FilenameUtils.getExtension(file.getAbsolutePath()).equals("war")) &&
                        file.isFile()
                        ||
                        (FilenameUtils.getBaseName(file.getAbsolutePath()).replace("unipos-", "").equals(moduleName.replace("unipos-", "")) &&
                                file.isDirectory()))
                .collect(Collectors.toList());

        if (moduleWars == null) {
            return;
        }

        moduleWars.stream()
                .filter(File::exists)
                .forEach(File::delete);
    }

    @Override
    public void deployWarFileByName(String moduleName) throws IOException {
        File deployableWarsDirectory = FileContainer.getDeployableWarDirectory();

        File warToDeploy = Arrays.stream(deployableWarsDirectory.listFiles())
                .filter(file -> FilenameUtils.getBaseName(file.getAbsolutePath()).replace("unipos-", "").equals(moduleName.replace("unipos-", ""))
                        && FilenameUtils.getExtension(file.getAbsolutePath()).equals("war"))
                .findFirst()
                .orElse(null);

        if (warToDeploy != null) {
            File moveToPath = new File(FileContainer.getWebappsDirectory().getAbsolutePath() + "/" + moduleName.replace("unipos-", "") + ".war");
            if (moveToPath.exists()) {
                moveToPath.delete();
            }
            Files.move(warToDeploy.toPath(), moveToPath.toPath());
        }
    }

    @Override
    public void undeployModuleByContextPath(String contextPath) throws IllegalArgumentException {

        String managerUrl = "http://localhost:8080/manager/text/undeploy?path=/";

        String plainCreds = environment.getProperty("tomcat.user") + ":" + environment.getProperty("tomcat.password");
        byte[] plainCredsBytes = plainCreds.getBytes();
        byte[] base64CredsBytes = org.apache.commons.codec.binary.Base64.encodeBase64(plainCredsBytes);
        String base64Creds = new String(base64CredsBytes);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        HttpEntity<String> httpEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.exchange(managerUrl + contextPath, HttpMethod.GET, httpEntity, String.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            String responseMessage = response.getBody();

            if (!responseMessage.startsWith("OK")) {
                if (!responseMessage.startsWith("FAIL - No context exists named")) {
                    throw new IllegalArgumentException(responseMessage);
                }
            }
        }
    }

    @Override
    public void updateUpdater() throws IOException, GeneralSecurityException {
        List<LicenseInfo> lincenseInfos = licenseInfoRepository.findAll();

        String updateServerUrl = environment.getProperty("update.url");

        for (LicenseInfo licenseInfo : lincenseInfos) {
            String computerId = ComputerIdCalculator.calcComputerId();
            String decryptPw = LicenseInfoEntity.builder().companyName(licenseInfo.getCompanyName()).customerNumber(licenseInfo.getCustomerId()).build().getEncryptionPw();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            headers.setAccept(Collections.singletonList(MediaType.APPLICATION_OCTET_STREAM));

            MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
            mvm.add("moduleName", "updater");
            mvm.add("licenseInfoEntity", GSonHolder.serializeDateGson().toJson(LicenseInfoEntity.builder().companyName(licenseInfo.getCompanyName()).customerNumber(licenseInfo.getCustomerId()).build()));
            mvm.add("deviceIdentifier", computerId);

            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(mvm, headers);

            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(updateServerUrl+"/update/downloadModule", HttpMethod.POST, request, byte[].class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                File encryptedWarFile = FileContainer.createNewTempWarFile("UPDATER", "");

                Files.write(encryptedWarFile.toPath(), responseEntity.getBody());
                if (licenseChecker.verifySign(encryptedWarFile)) {
                    licenseChecker.deployUpdater(encryptedWarFile, decryptPw);
                }
            }
        }
    }

    @Override
    public void triggerRootUpdate() throws IOException {
        File updaterJar = FileContainer.getUpdaterJar();

        if ((updaterJar).exists() && updaterJar.length() > 0L) {
            String[] command = new String[5];
            command[0] = "java";
            command[1] = "-jar";
            command[2] = updaterJar.getAbsolutePath();
            command[3] = FileContainer.getDeployableWarDirectory().getAbsolutePath();
            command[4] = FileContainer.getWebappsDirectory().getAbsolutePath();
            Process p = Runtime.getRuntime().exec(command);
        } else {
            System.out.println("File is not available");
        }
    }

    @Override
    public Map<String,String> getCurrentModuleVersionsInstalledForCompany(String companyGuid) throws URISyntaxException, GeneralSecurityException, LicenseErrorException, IOException {
        Company company = dataRemoteInterface.getCompanyByGuid(companyGuid);
        Assert.notNull(company, "No company found for the given companyGuid");

        LicenseVerification verification = licenseChecker.getLicenseInfos(company.getName());
        Assert.notNull(verification, "No valid license found!");

        Map<String,String> versions = new HashMap<>();

        verification.getModules().stream()
                .map(x -> x.getName().replace("unipos-", "")).forEach(x -> {
                    switch (x) {
                        case "auth":
                            versions.put("auth", authRemoteInterface.getCurrentVersion().replace("\"",""));
                            break;
                        case "ROOT":
                            versions.put("ROOT", PomVersionExplorer.getModuleVersion(this.getClass()));
                            break;
                        case "data":
                            versions.put("data", dataRemoteInterface.getCurrentVersion());
                            break;
                        case "design":
                            versions.put("design", designRemoteInterface.getCurrentVersion());
                            break;
                        case "pos":
                            versions.put("pos", posRemoteInterface.getCurrentVersion());
                            break;
                        case "printer":
                            versions.put("printer", printerRemoteInterface.getCurrentVersion());
                            break;
                        case "report":
                            versions.put("report", reportRemoteInterface.getCurrentVersion());
                            break;
                        case "socket":
                            versions.put("socket", socketRemoteInterface.getCurrentVersion());
                            break;
                        case "sync":
                            versions.put("sync", syncRemoteInterface.getCurrentVersion());
                            break;
                    }
                });
        return versions;
    }

    @Override
    public Map<String, String> getCurrentModuleVersionsFromLicense(String companyGuid) throws URISyntaxException, GeneralSecurityException, LicenseErrorException, IOException {
        Company company = dataRemoteInterface.getCompanyByGuid(companyGuid);
        Assert.notNull(company, "No company found for the given companyGuid");

        LicenseVerification verification = licenseChecker.getLicenseInfos(company.getName());
        Assert.notNull(verification, "No valid license found!");

        Map<String,String> modules = new HashMap<>();
        verification.getModules().stream()
                .forEach(
                        module -> modules.put(
                                module.getName().replace("unipos-",""),
                                module.getDevices().stream()
                                        .filter(device -> device.getComputerId().equals(ComputerIdCalculator.calcComputerId())).findFirst().get().getLatestVersion()
                                )
                );

        return modules;
    }

    @Override
    public List<String> checkLicenseVersionsAgainstModuleVersions() {
        return null;
    }

    @Override
    public boolean isModuleInVersionAvailable(String moduleName, String moduleVersion) {
        String updateServerUrl = environment.getProperty("update.url");

        ResponseEntity<Boolean> response = restTemplate.exchange(updateServerUrl + "/update/checkModuleAndVersion?moduleName={moduleName}&moduleVersion={moduleVersion}",
                HttpMethod.GET,
                new HttpEntity<Object>(null),
                Boolean.class,
                moduleName, moduleVersion);

        if(response.getStatusCode().is2xxSuccessful()) {
            return response.getBody();
        } else {
            return false;
        }
    }
}
