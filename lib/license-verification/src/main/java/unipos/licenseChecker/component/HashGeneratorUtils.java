package unipos.licenseChecker.component;

/**
 * Created by dominik on 28.08.15.
 */
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Hash functions utility class.
 * @author www.codejava.net
 *
 */
public class HashGeneratorUtils {
    private HashGeneratorUtils() {

    }

    public static String generateMD5(String message) throws IllegalArgumentException {
        return hashString(message, "MD5");
    }

    public static String generateSHA1(String message) throws IllegalArgumentException {
        return hashString(message, "SHA-1");
    }

    public static String generateSHA256(String message) throws IllegalArgumentException {
        return hashString(message, "SHA-256");
    }

    public static String generateSHA512(String message) throws IllegalArgumentException {
        return hashString(message, "SHA-512");
    }

    public static String generateFileHash(InputStream fileInputStream) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
             DigestInputStream dis = new DigestInputStream(fileInputStream, md);
        byte[] digest = md.digest();

        return convertByteArrayToHexString(digest);
    }

    private static String hashString(String message, String algorithm)
            throws IllegalArgumentException {

        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message.getBytes("UTF-8"));

            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            throw new IllegalArgumentException(
                    "Could not generate hash from String", ex);
        }
    }

    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16)
                    .substring(1));
        }
        return stringBuffer.toString();
    }
}