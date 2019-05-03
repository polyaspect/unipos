package unipos.core.components.update.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.GSonHolder;
import unipos.core.components.licenseSetup.LicenseInfo;
import unipos.core.components.licenseSetup.LicenseInfoRepository;
import unipos.core.components.update.UpdateRepository;
import unipos.licenseChecker.component.*;
import unipos.licenseChecker.computerId.ComputerIdCalculator;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.GeneralSecurityException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by domin on 07.03.2016.
 */

@Repository
public class UpdateRepositoryImpl implements UpdateRepository {

    @Autowired
    RestTemplate restTemplate;
    @Autowired
    LicenseChecker licenseChecker;
    @Autowired
    LicenseInfoRepository licenseInfoRepository;
    @Autowired
    Environment environment;

    @Override
    public Map<String,String> checkForNewLicenseFile(boolean updateLicenseFile) throws IOException, GeneralSecurityException {
        String updateServerUrl = environment.getProperty("update.url");

        Map<String,String> resultMap = new HashMap<>();

        List<String> toUpdateModuleNames = new ArrayList<>();

        //Retrieve all license-files that are persisted for this unipos-instance
        List<File> files = FileContainer.getLicenseFiles();
        List<LicenseInfo> licenseInfos = licenseInfoRepository.findAll();

        //We need to upload all License-Files, so that all files get updatted
        for (File licenseFile : files) {

            for (LicenseInfo licenseInfo : licenseInfos) {
                String computerId = ComputerIdCalculator.calcComputerId();
                String decryptPw = LicenseInfoEntity.builder().companyName(licenseInfo.getCompanyName()).customerNumber(licenseInfo.getCustomerId()).build().getEncryptionPw();
                LicenseVerification licenseVerification;
                try {
                    licenseVerification = licenseChecker.decryptLicenseFile(decryptPw, licenseFile);
                } catch (Exception e) {
                    continue;
                }

                if (!licenseVerification.getCompanyName().equals(licenseInfo.getCompanyName())) {
                    continue;
                }

                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);
                headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM, MediaType.valueOf("application/zip")));

                MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
                mvm.add("packageFile", new FileSystemResource(licenseFile));
                mvm.add("licenseInfoEntity", GSonHolder.serializeDateGson().toJson(LicenseInfoEntity.builder().companyName(licenseInfo.getCompanyName()).customerNumber(licenseInfo.getCustomerId()).build()));
                mvm.add("deviceIdentifier", computerId);

                HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(mvm, headers);

                ResponseEntity<byte[]> responseEntity = restTemplate.exchange(updateServerUrl + "/update/getNewVersionInfo", HttpMethod.POST, request, byte[].class);
//            ResponseEntity<byte[]> responseEntity = restTemplate.exchange(updateServerUrl + "/update/getNewVersionInfo", HttpMethod.POST, request, byte[].class);

                if (responseEntity.getStatusCode().is2xxSuccessful()) {
                    if (responseEntity.getBody() != null) {

                        File tempLicenseFile = File.createTempFile("tempLicenseFile_", ".license");

                        Files.write(tempLicenseFile.toPath(), responseEntity.getBody());

                        //After that I need to verify that the new License file is correctly signed and delete the old File
                        if (licenseChecker.verifySign(tempLicenseFile)) {

                            LicenseVerification newLicenseVerification = licenseChecker.decryptLicenseFile(decryptPw, tempLicenseFile);

                            toUpdateModuleNames.addAll(licenseVerification.getModules().stream().filter(oldModule ->
                                    oldModule.getDevices().stream().filter(oldDevice -> oldDevice.getComputerId().equals(computerId))
                                            .filter(oldDevice ->
                                                    newLicenseVerification.getModules().stream().filter(newModule ->
                                                            newModule.getName().equals(oldModule.getName()) &&
                                                                    newModule.getDevices().stream()
                                                                            .filter(newDevice -> newDevice.getComputerId().equals(computerId) && (!newDevice.getMaximumAvailableVersion().equals(oldDevice.getMaximumAvailableVersion()) || newDevice.isForceUpdate())).count() > 0

                                                    ).count() > 0
                                            ).count() > 0
                            ).map(x -> x.getName().replace("unipos-", "")).collect(toList()));
                            toUpdateModuleNames.addAll(newLicenseVerification.getModules().stream()
                                    .filter(module ->
                                            module.getDevices().stream()
                                                    .anyMatch(device -> device.getComputerId().equals(computerId)))
                                    .map(module -> module.getName().replace("unipos-", ""))
                                    .filter(moduleName ->
                                        !licenseVerification.getModules().stream()
                                                .filter(module ->
                                                        module.getDevices().stream().anyMatch(device -> device.getComputerId().equals(computerId)))
                                                .map(module -> module.getName().replace("unipos-", ""))
                                                .collect(toList()).contains(moduleName)
                                    ).collect(toList()));
                            toUpdateModuleNames = toUpdateModuleNames.stream().distinct().collect(Collectors.toList());

                            /////

                            final List<String> finalToUpdateModuleNames = toUpdateModuleNames;
                            newLicenseVerification.getModules().stream().filter(module ->
                                    finalToUpdateModuleNames.contains(module.getName().replace("unipos-",""))
                            ).map(module1 ->
                                    module1.getName().replace("unipos-", "") + ":" + module1.getDevices().stream().filter(device -> device.getComputerId().equals(computerId)).findFirst().get().getMaximumAvailableVersion()
                            )
                            .forEach(moduleVersion -> {
                                String currentModuleName = moduleVersion.split(":")[0];
                                String currentModuleVersion = moduleVersion.split(":")[1];

                                if(currentModuleName == null || currentModuleVersion == null) {
                                    return;
                                }

                                if(resultMap.containsKey(currentModuleName)) {
                                    if(new VersionComparator().compare(resultMap.get(currentModuleName), currentModuleVersion) == -1) {
                                        resultMap.put(currentModuleName, currentModuleVersion);
                                    }
                                } else {
                                    resultMap.put(currentModuleName, currentModuleVersion);
                                }
                            });

                            /////

                            if(updateLicenseFile) {
                                if (toUpdateModuleNames.size() > 0) {
                                    File newLicenseFile = FileContainer.createNewLicenseFile();
                                    newLicenseFile.delete();
                                    Files.move(tempLicenseFile.toPath(), newLicenseFile.toPath());

                                    licenseFile.delete();
                                } else {
                                    tempLicenseFile.delete();
                                }
                            } else {
                                tempLicenseFile.delete();
                            }
                        }
                    }
                }
            }
        }
        return resultMap;
    }
}
