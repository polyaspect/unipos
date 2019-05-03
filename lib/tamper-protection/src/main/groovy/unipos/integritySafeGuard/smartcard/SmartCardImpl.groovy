package unipos.integritySafeGuard.smartcard

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException
import org.bouncycastle.cert.X509CertificateHolder
import unipos.integritySafeGuard.SmardCardException
import unipos.integritySafeGuard.SmartCardUtils
import unipos.integritySafeGuard.apdu.ApduProvider
import unipos.integritySafeGuard.domain.SignatureInvoice
import unipos.integritySafeGuard.domain.SignatureJob
import unipos.integritySafeGuard.domain.SignatureResult
import unipos.integritySafeGuard.domain.Type
import unipos.integritySafeGuard.domain.exceptions.SignatureRequiredException

import javax.smartcardio.Card
import javax.smartcardio.CardChannel
import javax.smartcardio.CardException
import javax.smartcardio.CardTerminal
import javax.smartcardio.CommandAPDU
import javax.smartcardio.ResponseAPDU
import java.security.spec.InvalidKeySpecException
import java.time.LocalDateTime

/**
 * Created by domin on 28.12.2016.
 */
class SmartCardImpl extends SmartCard {

    CardTerminal cardTerminal
    Card card
    String readerName
    protected CardChannel channel

    SmartCardImpl(CardTerminal cardTerminal) {
        this.cardTerminal = cardTerminal
        this.card = cardTerminal.connect("*")
        this.channel = card.getBasicChannel()
        this.readerName = cardTerminal.name
        certificateSerialNr = getCertificateSerialHex()
    }

    ResponseAPDU executeCommand(CommandAPDU commandAPDU) throws SmardCardException {
        ResponseAPDU responseAPDU = null;
        try {
            responseAPDU = channel.transmit(commandAPDU);
            if (responseAPDU.getSW() != 0x9000 && responseAPDU.getSW() != 0x6A82) {
                throw new SmardCardException("Response APDU status is " + responseAPDU.getSW());
            }
        } catch (Exception e) {
            throw new SmardCardException(e);
        }
        return responseAPDU
    }

    void selectMasterFile() {
        executeCommand(ApduProvider.selectMasterFile)
    }

    void selectDfSigApp() {
        executeCommand(ApduProvider.selectDfSigApp)
    }

    void selectEfCChDs() {
        executeCommand(ApduProvider.selectEfCChDs)
    }

    void verifyPin(String pin) {
        selectMasterFile()
        selectDfSigApp()
        executeCommand(ApduProvider.getVerify(pin))
    }

    byte[] signData(byte[] data, SignatureJob signatureJob) {
        verifyPin(signatureJob.pin)
        def response = executeCommand(ApduProvider.getSignatureApdu(SmartCardUtils.hashData(data, signatureJob.rksSuite)))
        response.getData()
    }

    byte[] signData(String data, SignatureJob signatureJob) {
        signData(data.getBytes("UTF-8"), signatureJob)
    }

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
        String headerBase64 = Base64.urlEncoder.withoutPadding().encodeToString(headerBytes);

        String payloadString = signatureInvoice.compressDataString()
        byte[] payloadBytes = payloadString.getBytes("UTF-8")
        String payloadBase64 = Base64.urlEncoder.withoutPadding().encodeToString(payloadBytes);

        String inputString = headerBase64 + "." + payloadBase64;
        byte[] inputStringbytes = inputString.getBytes("UTF-8");

        byte[] signatureBytes
        try {
            signatureBytes = signData(inputStringbytes, signatureJob)
        } catch (CardException | SmardCardException ex) {
            if(signatureInvoice.signatureType.signatureRequired) {
                throw new SignatureRequiredException("The beleg " + signatureInvoice.signatureType.name + " must have a valid signature!")
            }
            signatureBytes = "Sicherheitseinrichtung ausgefallen".getBytes("UTF-8");
        }
        String signatureBase64 = Base64.urlEncoder.withoutPadding().encodeToString(signatureBytes);

        def result = SignatureResult.builder()
                .headerBase64Url(headerBase64)
                .payloadBase64Url(payloadBase64)
                .signatureBase64Url(signatureBase64)
                .invoiceSignatureType(signatureInvoice.signatureType)
                .creationDate(LocalDateTime.now())
                .build()

        signatureInvoice.signature = result.signatureBase64Url;
        result
    }

    private List<byte[]> getBuffer(boolean onlyFirst) {
        selectMasterFile()
        selectDfSigApp()
        selectEfCChDs()

        int offset = 0;
        List<byte[]> dataList = new ArrayList<>(8);
        while (true) {
            ResponseAPDU resp = channel.transmit(new CommandAPDU(0x00, 0xB0, 0x7F & (offset >> 8), offset & 0xFF, 256));
            if (resp.getSW() != 0x9000) {
                break;
            }
            dataList.add(resp.getData());
            if (onlyFirst) {
                break;
            }
            offset += 256;
        }
        dataList;
    }

    public String getCertificateSerial2() {
        def certificate = certificate
        if(!certificate) {
            return null
        }
        def serialId = certificate.subject.style.attrNameToOID("serialnumber")
        def serialnumber = certificate.subject.getRDNs(serialId).first().first.toString()
    }

    X509CertificateHolder getCertificate() throws SmardCardException, CardException {
        selectMasterFile()
        selectDfSigApp()
        selectEfCChDs()

        SmartCardUtils.buildX509Certificate(getBuffer(false));
    }

    private BigInteger getCertificateSerial() throws SmardCardException, CardException {

        List<byte[]> dataList = getBuffer(true);
        byte[] ba = dataList.get(0);
        int length = SmartCardUtils.byteToUnsignedint(ba[14]);
        BigInteger bi = BigInteger.valueOf(0);
        for (int i = 0; i < length; i++) {
            bi = bi.shiftLeft(8).add(BigInteger.valueOf(SmartCardUtils.byteToUnsignedint(ba[15 + i])));
        }
        return bi;
    }

    public String getCertificateSerialDecimal() throws SmardCardException, CardException {

        BigInteger serial = getCertificateSerial();
        return serial.toString().toUpperCase()
    }

    public String getCertificateSerialHex() throws SmardCardException, CardException {

        BigInteger serial = getCertificateSerial()
        return serial.toString(16).toUpperCase()
    }

    public boolean isSmartCardAvailable() {
        try {
            selectMasterFile()
        } catch (SmardCardException ex){
            false
        }
        true
    }
}
