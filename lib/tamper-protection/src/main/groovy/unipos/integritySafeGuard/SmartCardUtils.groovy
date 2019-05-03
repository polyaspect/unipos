package unipos.integritySafeGuard

import org.springframework.util.Assert
import unipos.integritySafeGuard.domain.RksSuite
import unipos.integritySafeGuard.domain.SignatureInvoice
import unipos.integritySafeGuard.domain.SignatureJob
import unipos.integritySafeGuard.domain.SignatureResult
import unipos.integritySafeGuard.domain.exceptions.InvalidDecryptionKeyException

import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.smartcardio.CardException
import java.nio.ByteBuffer
import java.security.DigestException
import java.security.MessageDigest
import java.security.cert.Certificate
import java.security.cert.CertificateFactory

import org.bouncycastle.cert.X509CertificateHolder

import java.util.stream.Collectors;


/**
 * Created by domin on 30.09.2016.
 */
class SmartCardUtils {

    public static byte[] hashData(byte[] dataBytes, RksSuite rksSuite) {
        MessageDigest digest = MessageDigest.getInstance(rksSuite.hashAlgorithmForPreviousSignatureValue)
        byte[] hash = digest.digest(dataBytes)
        if (hash.length <= 0) {
            throw new DigestException("Unable to create a hash from the given data...")
        }
        hash
    }

    public static String encryptStandUmsatzZaehler(long plainStandUmsatzZaehler, SignatureJob signatureJob) {
        Assert.notNull(signatureJob, "SignatureJob must not be null")

        String IvUTF8Representation = signatureJob.kassaId + signatureJob.belegNr

        ///hash the String with the hash-algorithm defined in the cashbox-algorithm-suite
        MessageDigest messageDigest = MessageDigest.getInstance(signatureJob.rksSuite.getHashAlgorithmForPreviousSignatureValue());
        byte[] hashValue = messageDigest.digest(IvUTF8Representation.getBytes("UTF-8"));
        byte[] IV = new byte[16];
        System.arraycopy(hashValue, 0, IV, 0, 16);

        //initialisation of the data which should be encrypted
        final ByteBuffer byteBufferData = ByteBuffer.allocate(16);
        byteBufferData.putLong(plainStandUmsatzZaehler);
        final byte[] data = byteBufferData.array();

        //now the turnover counter is represented in two's-complement representation (negative values are possible)
        //length is defined by the respective implementation (min. 5 bytes)
        byte[] turnOverCounterByteRep = get2ComplementRepForLong(plainStandUmsatzZaehler, signatureJob.turnOverCounterLengthInBytes);

        //two's-complement representation is copied to the data array, and inserted at index 0
        System.arraycopy(turnOverCounterByteRep,0,data,0,turnOverCounterByteRep.length);

        final IvParameterSpec ivSpec = new IvParameterSpec(IV);

        final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
        cipher.init(Cipher.ENCRYPT_MODE, signatureJob.secretKey, ivSpec);

        // encrypt the turnover value with the prepared cipher
        final byte[] encryptedTurnOverValueComplete = cipher.doFinal(data);

        // extract bytes that will be stored in the receipt (only bytes 0-7)
        // cryptographic NOTE: this is only possible due to the use of the CTR
        // mode, would not work for ECB/CBC etc. modes
        final byte[] encryptedTurnOverValue = new byte[signatureJob.turnOverCounterLengthInBytes]; // or 5 bytes if min.
        // turnover length is
        // used
        System.arraycopy(encryptedTurnOverValueComplete, 0, encryptedTurnOverValue, 0, signatureJob.turnOverCounterLengthInBytes);

        // encode result as BASE64

        Base64.getEncoder().encodeToString(encryptedTurnOverValue);
    }

    public static long decryptStandUmsatzZaehler(String encryptedUmsatzZaehler, SignatureJob signatureJob) {

        Assert.notNull(signatureJob, "SignatureJob must not be null")
        Assert.notNull(encryptedUmsatzZaehler, "encryptedUmsatzZaehler must not be null")

        String IvUTF8Representation = signatureJob.kassaId + signatureJob.belegNr

        ///hash the String with the hash-algorithm defined in the cashbox-algorithm-suite
        MessageDigest messageDigest = MessageDigest.getInstance(signatureJob.rksSuite.getHashAlgorithmForPreviousSignatureValue());
        byte[] hashValue = messageDigest.digest(IvUTF8Representation.getBytes("UTF-8"));
        byte[] IV = new byte[16];
        System.arraycopy(hashValue, 0, IV, 0, 16);

        final byte[] encryptedTurnOverValue = Base64.getDecoder().decode(encryptedUmsatzZaehler);

        // prepare AES cipher with CTR/ICM mode
        final IvParameterSpec ivSpec = new IvParameterSpec(IV);

        final Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding", "BC");
        cipher.init(Cipher.DECRYPT_MODE, signatureJob.secretKey, ivSpec);
        final byte[] plainTurnOverValueComplete = cipher.doFinal(encryptedTurnOverValue);
        new BigInteger(plainTurnOverValueComplete).longValue();;
    }

    public static byte[] get2ComplementRepForLong(long value,int numberOfBytesFor2ComplementRepresentation) {
        if (numberOfBytesFor2ComplementRepresentation<1 || (numberOfBytesFor2ComplementRepresentation>8)) {
            throw new IllegalArgumentException();
        }

        //create byte buffer, max length 8 bytes (equal to long representation)
        ByteBuffer byteBuffer = ByteBuffer.allocate(8);
        byteBuffer.putLong(value);
        byte[] longRep = byteBuffer.array();

        //if given length for encoding is equal to 8, we are done
        if (numberOfBytesFor2ComplementRepresentation==8) {
            return longRep;
        }

        //if given length of encoding is less than 8 bytes, we truncate the representation (of course one needs to be sure
        //that the given long value is not larger than the created byte array
        byte[] byteRep = new byte[numberOfBytesFor2ComplementRepresentation];

        //truncating the 8-bytes long representation
        System.arraycopy(longRep,8-numberOfBytesFor2ComplementRepresentation,byteRep,0,numberOfBytesFor2ComplementRepresentation);
        return byteRep;
    }

    public static SecretKey getRandomAes256Key() {
        KeyGenerator keyGen = new KeyGenerator("AES")
        keyGen.init(256)
        keyGen.generateKey()
    }

    /**
     * Takes a custom password, hashes it with sha256 and then creates a {@link SecretKey} instance
     * @param customPw is the custom Pw of the user
     * @return a {@link SecretKey} instance of the sha256 value of the pw
     */
    public static SecretKey getSecretKeyByCustomPw(String customPw) {
        Assert.notNull(customPw, "The given customPw must not be null!")
        Assert.isTrue(!customPw.isEmpty(), "The given customPw must not be empty!")

        def hashAlg = MessageDigest.getInstance("SHA-256")
        def hashedBytes = hashAlg.digest(customPw.getBytes("UTF-8"))

        new SecretKeySpec(hashedBytes, "AES")
    }

    public static String getLetzterSignatureChainValue(SignatureResult signatureResult, SignatureJob signatureJob) {
        def hashResult = hashData(signatureResult.toJwsCompactRepresentation().getBytes("UTF-8"), signatureJob.rksSuite)
        byte[] extractedBytes = new byte[signatureJob.rksSuite.numberOfBytesExtractedFromPrevSigHash]
        System.arraycopy(hashResult, 0, extractedBytes, 0, signatureJob.rksSuite.numberOfBytesExtractedFromPrevSigHash)

        Base64.getEncoder().encodeToString(extractedBytes)
    }

    public static String getStartInvoiceHashValue(SignatureJob signatureJob) {
        def hashResult = hashData(signatureJob.kassaId.getBytes("UTF-8"), signatureJob.rksSuite)
        byte[] extractedBytes = new byte[signatureJob.rksSuite.numberOfBytesExtractedFromPrevSigHash]
        System.arraycopy(hashResult, 0, extractedBytes, 0, signatureJob.rksSuite.numberOfBytesExtractedFromPrevSigHash)

        Base64.getEncoder().encodeToString(extractedBytes)
    }

    public static X509CertificateHolder buildX509Certificate(List<byte[]> dataList) throws CardException {

        int size = (dataList.size() - 1) * 256;
        size += dataList.get(dataList.size() - 1).length;
        byte[] cert = new byte[size];
        int i = 0;
        for (byte[] b : dataList) {
            System.arraycopy(b, 0, cert, i, b.length);
            i += b.length;
        }
        try {
            X509CertificateHolder ch = new X509CertificateHolder(cert);
            return ch;
        } catch (Exception e) {
            e.printStackTrace();
            try {
                ByteArrayInputStream bis = new ByteArrayInputStream(cert);
                Certificate c2 = CertificateFactory.getInstance("X509").generateCertificate(bis);
                X509CertificateHolder ch = new X509CertificateHolder(c2.getEncoded());
                return ch;
            } catch (Exception e2) {
                e2.printStackTrace();
                throw new CardException("Error building certificate");
            }
        }
        /*
         * catch (Exception e) { e.printStackTrace(); throw new
         * SmardCardException("Error building certificate"); }
         */
    }

    public static String byteArrayToHexString(byte[] data) {
        String cin;
        final StringBuilder builder = new StringBuilder(8);
        for (byte b : data) {
            builder.append(String.format("%02x", b));
        }
        cin = builder.toString();
        return cin.toUpperCase();
    }

    public static byte[] getFormat2PIN(String pin) throws SmardCardException {

        if (pin.length() != 6 && pin.length() != 4) {
            throw new SmardCardException("Wrong PIN length");
        }
        byte[] ba = new byte[8];
        ba[0] = (byte) ((2 << 4) | pin.length());
        char[] ca = pin.toCharArray();
        ba[1] = (byte) (((ca[0] - 0x30) << 4) | (ca[1] - 0x30));
        ba[2] = (byte) (((ca[2] - 0x30) << 4) | (ca[3] - 0x30));
        if (pin.length() == 6) {
            ba[3] = (byte) (((ca[4] - 0x30) << 4) | (ca[5] - 0x30));
        } else {
            ba[3] = (byte) 0xFF;
        }
        ba[4] = (byte) 0xFF;
        ba[5] = (byte) 0xFF;
        ba[6] = (byte) 0xFF;
        ba[7] = (byte) 0xFF;
        return ba;
    }

    public static int byteToUnsignedint(byte b) {
        int i = b;
        if (i < 0) {
            i += 256;
        }
        return i;
    }

    static String calcCheckSumForKey(String base64EncryptedKey, RksSuite rksSuite) {

        MessageDigest digest = MessageDigest.getInstance("SHA-256")
        byte[] hash = digest.digest(base64EncryptedKey.getBytes("UTF-8"))

        Base64.getEncoder().encodeToString(((byte[])Arrays.stream(hash).limit(rksSuite.aesKeyCheckExtractedBytes).collect(Collectors.toList()).toArray()));
    }
}
