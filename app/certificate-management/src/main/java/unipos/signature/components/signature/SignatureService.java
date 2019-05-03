package unipos.signature.components.signature;

import unipos.common.remote.pos.model.Invoice;
import unipos.integritySafeGuard.SmardCardException;
import unipos.integritySafeGuard.domain.exceptions.SignatureRequiredException;
import unipos.signature.components.umsatzZaehler.UmsatzZaehler;

import javax.smartcardio.CardException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;

/**
 * Created by domin on 15.12.2016.
 */
public interface SignatureService {

    Invoice signInvoice(Invoice invoice) throws SignatureRequiredException;

    Invoice createStartInvoice(Invoice invoice) throws IllegalArgumentException, SignatureRequiredException, SmardCardException, CardException, IOException;

    boolean isStartSignatureCreated(String storeGuid);

    boolean isSammelbelegRequired(String guid);

    boolean isSignatureDeviceAvailable(String storeGuid);

    boolean verifyLatestSignature(String guid) throws SmardCardException, CardException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException, CertificateException, NoSuchProviderException;
}
