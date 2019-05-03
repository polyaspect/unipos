package unipos.integritySafeGuard.smartcard

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException
import org.bouncycastle.cert.X509CertificateHolder
import unipos.integritySafeGuard.SmardCardException
import unipos.integritySafeGuard.apdu.ApduProvider
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
abstract class SmartCard {

    String certificateSerialNr

    abstract ResponseAPDU executeCommand(CommandAPDU commandAPDU) throws SmardCardException

    abstract void selectMasterFile()

    abstract void selectDfSigApp()

    abstract void selectEfCChDs()

    abstract void verifyPin(String pin)

    abstract byte[] signData(byte[] data, SignatureJob signatureJob)

    abstract byte[] signData(String data, SignatureJob signatureJob)

    abstract SignatureResult signInvoice(SignatureInvoice signatureInvoice, SignatureJob signatureJob)

    abstract X509CertificateHolder getCertificate() throws SmardCardException, CardException

    abstract String getCertificateSerialDecimal() throws SmardCardException, CardException

    abstract String getCertificateSerialHex() throws SmardCardException, CardException
}