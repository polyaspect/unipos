package unipos.licenseChecker.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.*;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;

/**
 * Created by domin on 04.02.2016.
 */
public class CertHandler {

    @Autowired
    private RestTemplate restTemplate;

    public PublicKey loadPublicKey(String path, String algorithm)
            throws IOException, NoSuchAlgorithmException,
            InvalidKeySpecException {
        // Read Public Key.
        File filePublicKey = new File(path);
        FileInputStream fis = new FileInputStream(filePublicKey);
        byte[] encodedPublicKey = new byte[(int) filePublicKey.length()];
        fis.read(encodedPublicKey);
        fis.close();

        // Generate KeyPair.
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(
                encodedPublicKey);
        PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);

        return publicKey;
    }

    public RSAPublicKey loadPublicKeyFromServer() throws IOException, GeneralSecurityException {

        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());

        HttpHeaders headers= new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_OCTET_STREAM));

        HttpEntity<String> entity = new HttpEntity<String>(headers);

        ResponseEntity<byte[]> responseEntity = restTemplate.exchange("https://app.unipos.at/licenseServer/licenses/publicKey", HttpMethod.GET, entity, byte[].class);
//
//        if(responseEntity.getStatusCode() == HttpStatus.OK) {
            File publicKey = FileContainer.getPublicKeyFile();
            Files.write(publicKey.toPath(), responseEntity.getBody());

            return RSA.getPublicKey(publicKey.getAbsolutePath());
//        }
//
//        return null;
    }
}
