package unipos.licenseChecker.component;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.FileHeader;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import unipos.common.container.GSonHolder;
import unipos.common.remote.core.CoreRemoteInterface;
import unipos.common.remote.core.model.LicenseInfo;
import unipos.licenseChecker.component.exception.LicenseErrorException;
import unipos.licenseChecker.computerId.ComputerIdCalculator;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.URISyntaxException;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Created by Dominik on 06.02.2016.
 */
public class LicenseChecker {

    @Autowired
    CertHandler certHandler;
    @Autowired
    CoreRemoteInterface coreRemoteInterface;
    @Autowired
    ModuleName moduleName;

    public LicenseVerification getLicenseInfos(String companyName) throws GeneralSecurityException, IOException, URISyntaxException, LicenseErrorException {

        //Obtain all LicenseInfos from the Core Module
        List<LicenseInfo> licenseInfos = coreRemoteInterface.getAllLicenceInfos();
        List<File> licenseFiles = FileContainer.getLicenseFiles();

        if(licenseInfos.size() == 0) {
            throw new LicenseErrorException("Not able to load any licenseInfos from the CORE-Module. Have you already activated your product???");
        }

        if(licenseFiles.size() == 0) {
            throw new LicenseErrorException("No license-Files found for your unipos-installation. Have you already activated your product???");
        }

        for(File uniposLicenseFile : licenseFiles) {

            if (!verifySign(uniposLicenseFile)) {
                return null;
            }


            //try to decrypt the pw of the license with all licenseFiles. If no deserialization is possible, there was an error
            for(LicenseInfo licenseInfo : licenseInfos) {

                String decryptPW = LicenseInfoEntity.builder().customerNumber(licenseInfo.getCustomerId()).companyName(companyName).build().getEncryptionPw();

                try {
                    LicenseVerification verification = decryptLicenseFile(decryptPW, uniposLicenseFile);

                    if (verification != null && verification.getModules().stream().anyMatch(x -> x.getName().replace("unipos-","").equals(moduleName.getName().replace("unipos-","")))) {
                        return verification;
                    }
                } catch (Exception ignored) {
                    //Brute force trying so that we can get the correct LicenseInfo to the License-Files
                }

            }
        }
        return null;
    }

    public boolean verifySign(File uniposLicensePackage, File publicKeyFile) throws IOException, GeneralSecurityException {
        PublicKey publicKey = RSA.getPublicKey(publicKeyFile.getAbsolutePath());

        InputStream licenseFileInputStream = extractLicenseFileFromPackage(uniposLicensePackage);
        InputStream licenseSignInputStream = extractLicenseSignFromPackage(uniposLicensePackage);

        String encryptedLicenseHash = DigestUtils.md5Hex(licenseFileInputStream);
        IOUtils.closeQuietly(licenseFileInputStream);

        String sign = IOUtils.toString(licenseSignInputStream);
        IOUtils.closeQuietly(licenseSignInputStream);

        boolean verified = RSA.verify(publicKey,encryptedLicenseHash, sign);

        return verified;
    }

    public boolean verifySign(File file) throws IOException, GeneralSecurityException {
        PublicKey publicKey = RSA.getPublicKey(FileContainer.getPublicKeyFile().getAbsolutePath());

        InputStream licenseFileInputStream = extractLicenseFileFromPackage(file);
        InputStream licenseSignInputStream = extractLicenseSignFromPackage(file);

        String encryptedLicenseHash = DigestUtils.md5Hex(licenseFileInputStream);
        IOUtils.closeQuietly(licenseFileInputStream);

        String sign = IOUtils.toString(licenseSignInputStream);
        IOUtils.closeQuietly(licenseSignInputStream);

        boolean verified = RSA.verify(publicKey,encryptedLicenseHash, sign);

        return verified;
    }

    public LicenseVerification decryptLicenseFile(String decryptPw, File uniposLicensePackage) throws IOException, GeneralSecurityException {

        InputStream licenseFileInputStream = extractLicenseFileFromPackage(uniposLicensePackage);

        byte[] encryptedData = IOUtils.toByteArray(licenseFileInputStream);
        IOUtils.closeQuietly(licenseFileInputStream);

        byte[] decryptPWBytes = decryptPw.getBytes();

        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(decryptPWBytes, "AES");
        c.init(Cipher.DECRYPT_MODE, k);
        byte[] data = c.doFinal(encryptedData);

        String decryptedLicenseInfos = new String(data);

        return GSonHolder.serializeDateGson().fromJson(decryptedLicenseInfos, LicenseVerification.class);
    }

    public void deployWarFile(File base64EncryptedWarPackage, String decryptPw, String moduleName) throws IOException, GeneralSecurityException {
        File encryptedWarFile = File.createTempFile("encrypted_war", "war");
        File deployableWarFile = FileContainer.createNewDeployableWarFile(moduleName);

        InputStream warInputStreamFromZip = extractLicenseFileFromPackage(base64EncryptedWarPackage);
        Base64InputStream base64InputStream = new Base64InputStream(warInputStreamFromZip);
        FileOutputStream fileOutputStream = new FileOutputStream(encryptedWarFile);

        IOUtils.copy(base64InputStream, fileOutputStream);
        IOUtils.closeQuietly(fileOutputStream);
        IOUtils.closeQuietly(base64InputStream);
        IOUtils.closeQuietly(warInputStreamFromZip);

        base64EncryptedWarPackage.delete();

        byte[] decryptPWBytes = decryptPw.getBytes();

        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(decryptPWBytes, "AES");
        c.init(Cipher.DECRYPT_MODE, k);

        FileInputStream fis = new FileInputStream(encryptedWarFile);
        CipherInputStream cis = new CipherInputStream(fis, c);
        FileOutputStream fos = new FileOutputStream(deployableWarFile);

        byte[] block = new byte[1024];
        int i;
        while ((i = cis.read(block)) != -1)
        {
            fos.write(block, 0, i);
        }
        IOUtils.closeQuietly(fos);
        IOUtils.closeQuietly(cis);
        IOUtils.closeQuietly(fis);

        encryptedWarFile.delete();
    }

    public void deployUpdater(File base64EncryptedWarPackage, String decryptPw) throws IOException, GeneralSecurityException {
        File encryptedWarFile = File.createTempFile("encrypted_updater", ".jar");
        File updaterJar = FileContainer.getUpdaterJar();

        if(updaterJar.exists()) {
            if(updaterJar.delete()) {
                updaterJar.createNewFile();
            }
        }

        InputStream warInputStreamFromZip = extractLicenseFileFromPackage(base64EncryptedWarPackage);
        Base64InputStream base64InputStream = new Base64InputStream(warInputStreamFromZip);
        FileOutputStream fileOutputStream = new FileOutputStream(encryptedWarFile);

        IOUtils.copy(base64InputStream, fileOutputStream);
        IOUtils.closeQuietly(fileOutputStream);
        IOUtils.closeQuietly(base64InputStream);
        IOUtils.closeQuietly(warInputStreamFromZip);

        base64EncryptedWarPackage.delete();

        byte[] decryptPWBytes = decryptPw.getBytes();

        Cipher c = Cipher.getInstance("AES");
        SecretKeySpec k = new SecretKeySpec(decryptPWBytes, "AES");
        c.init(Cipher.DECRYPT_MODE, k);

        FileInputStream fis = new FileInputStream(encryptedWarFile);
        CipherInputStream cis = new CipherInputStream(fis, c);
        FileOutputStream fos = new FileOutputStream(updaterJar);

        byte[] block = new byte[1024];
        int i;
        while ((i = cis.read(block)) != -1)
        {
            fos.write(block, 0, i);
        }
        IOUtils.closeQuietly(fos);
        IOUtils.closeQuietly(cis);
        IOUtils.closeQuietly(fis);

        encryptedWarFile.delete();
    }

    public LicenseVerification activateLicense(File uniposLicensePackage, String authPw, File publicKeyFile) throws GeneralSecurityException, IOException {
        RSAPublicKey publicKey = certHandler.loadPublicKeyFromServer();

        RSA.savePublicKey(publicKey, publicKeyFile);

        InputStream licenseFileInputStream = extractLicenseFileFromPackage(uniposLicensePackage);
        InputStream licenseSignInputStream = extractLicenseSignFromPackage(uniposLicensePackage);

        byte[] encryptedData = IOUtils.toByteArray(licenseFileInputStream);
        String encryptedLicenseHash = DigestUtils.md5Hex(encryptedData);
        IOUtils.closeQuietly(licenseFileInputStream);

        String sign = IOUtils.toString(licenseSignInputStream);
        IOUtils.closeQuietly(licenseSignInputStream);

        boolean verified = RSA.verify(publicKey,encryptedLicenseHash, sign);

        if(!verified) {
            return null;
        }

        byte[] decryptPWBytes = authPw.getBytes();

        Cipher c = Cipher.getInstance("AES");

        // ERROR HANDLING IF NO JCE IS INSTALLED
        SecretKeySpec k = new SecretKeySpec(decryptPWBytes, "AES");
        c.init(Cipher.DECRYPT_MODE, k);
        byte[] data = c.doFinal(encryptedData);

        String decryptedLicenseInfos = new String(data);

        return GSonHolder.serializeDateGson().fromJson(decryptedLicenseInfos, LicenseVerification.class);
    }

    private InputStream extractLicenseFileFromPackage(File uniposLicensePackage) {
        try {

            ZipFile zipFile = new ZipFile(uniposLicensePackage);

            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            FileHeader licenseFileHeader = fileHeaders.stream().filter(file -> file.getFileName().equals("unipos.license")).findFirst().get();

            return zipFile.getInputStream(licenseFileHeader);
        } catch (ZipException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }

    private InputStream extractLicenseSignFromPackage(File uniposLicensePackage) {
        try {

            ZipFile zipFile = new ZipFile(uniposLicensePackage);

            List<FileHeader> fileHeaders = zipFile.getFileHeaders();
            FileHeader licenseFileHeader = fileHeaders.stream().filter(file -> file.getFileName().equals("unipos.sign")).findFirst().get();

            return zipFile.getInputStream(licenseFileHeader);
        } catch (ZipException | NoSuchElementException e) {
            e.printStackTrace();
            return null;
        }
    }
}
