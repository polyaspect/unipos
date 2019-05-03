package unipos.integritySafeGuard.smartcard

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException
import org.bouncycastle.cert.X509CertificateHolder
import unipos.integritySafeGuard.SmardCardException
import unipos.integritySafeGuard.SmartCardUtils
import unipos.integritySafeGuard.domain.SignatureInvoice
import unipos.integritySafeGuard.domain.SignatureJob
import unipos.integritySafeGuard.domain.SignatureResult
import unipos.integritySafeGuard.domain.Type

import javax.smartcardio.CardException
import javax.smartcardio.CommandAPDU
import javax.smartcardio.ResponseAPDU
import java.security.spec.InvalidKeySpecException
import java.time.LocalDateTime

/**
 * Created by domin on 29.12.2016.
 */
class SmartCardMock extends SmartCard {

    SmartCardMock(String certificateSerialNr) {
        this.certificateSerialNr = certificateSerialNr
    }

    @Override
    ResponseAPDU executeCommand(CommandAPDU commandAPDU) throws SmardCardException {
        return null
    }

    @Override
    void selectMasterFile() {

    }

    @Override
    void selectDfSigApp() {

    }

    @Override
    void selectEfCChDs() {

    }

    @Override
    void verifyPin(String pin) {

    }

    @Override
    byte[] signData(byte[] data, SignatureJob signatureJob) {
        return new byte[0]
    }

    byte[] signData(String data, SignatureJob signatureJob) {
        return ""
    }

    @Override
    SignatureResult signInvoice(SignatureInvoice signatureInvoice, SignatureJob signatureJob) {
        signatureInvoice.validate()

        if (!signatureJob?.secretKey)
            throw new IllegalArgumentException("key must not be null")

        if (!signatureInvoice.zertifikatSeriennummer || signatureInvoice.zertifikatSeriennummer.isEmpty()) {
            signatureInvoice.zertifikatSeriennummer = certificateSerialNr.toUpperCase()
        }

        if (signatureInvoice.signaturVorrigerBeleg && !signatureInvoice.signaturVorrigerBeleg.isEmpty()) {
            try {
                byte[] decodedBytes = Base64.getDecoder().decode(signatureInvoice.signaturVorrigerBeleg)
                if (decodedBytes.length != signatureJob.rksSuite.numberOfBytesExtractedFromPrevSigHash) {
                    throw new IllegalArgumentException("The signature of the last Job must have a length of " + signatureJob.rksSuite.numberOfBytesExtractedFromPrevSigHash + "bytes! But was: " + decodedBytes.length)
                }
            } catch (Exception ignored) {
                throw new Base64DecodingException()
            }
        } else {
            throw new IllegalArgumentException("SignaturVorrigerBeleg must not be null")
        }

        if (!signatureInvoice.hasEncryptedUmsatzZaehler()) {
            switch (signatureInvoice.signatureType) {

                case Type.STORNO:
                    signatureInvoice.standUmsatzZaehlerEncrypted = Base64.getEncoder().encodeToString("STO".getBytes("UTF-8"))
                    break;
                case Type.TRAINING:
                    signatureInvoice.standUmsatzZaehlerEncrypted = Base64.getEncoder().encodeToString("TRA".getBytes("UTF-8"))
                    break;
                default:
                    signatureInvoice.standUmsatzZaehlerEncrypted = SmartCardUtils.encryptStandUmsatzZaehler(signatureInvoice.standUmsatzZaehler, signatureJob)
                    break;
            }
        }

        if (signatureJob.secretKey.encoded.length != 32) {
            throw new InvalidKeySpecException("The Key size must have a length of 32 byte (256 Bit)")
        }

        String headerString = "{\"alg\":\"" + signatureJob.rksSuite.jwsSignatureAlgorithm + "\"}"
        byte[] headerBytes = headerString.getBytes("UTF-8")
        String headerBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(headerBytes)

        String payloadString = signatureInvoice.compressDataString()
        byte[] payloadBytes = payloadString.getBytes("UTF-8")
        String payloadBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadBytes)

        byte[] signatureBytes = "Sicherheitseinrichtung ausgefallen".getBytes("UTF-8")
        String signatureBase64 = Base64.getUrlEncoder().withoutPadding().encodeToString(signatureBytes)

        def result = SignatureResult.builder()
                .headerBase64Url(headerBase64)
                .payloadBase64Url(payloadBase64)
                .signatureBase64Url(signatureBase64)
                .invoiceSignatureType(signatureInvoice.signatureType)
                .creationDate(LocalDateTime.now())
                .build()

        return result
    }

    @Override
    X509CertificateHolder getCertificate() throws SmardCardException, CardException {
        return null
    }

    @Override
    String getCertificateSerialDecimal() throws SmardCardException, CardException {
        return null
    }

    @Override
    String getCertificateSerialHex() throws SmardCardException, CardException {
        return null
    }
}
