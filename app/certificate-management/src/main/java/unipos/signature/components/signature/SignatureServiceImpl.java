package unipos.signature.components.signature;

import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.spockframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.PaymentMethod;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.signature.model.SignatureTurnoverDetail;
import unipos.integritySafeGuard.SmardCardException;
import unipos.integritySafeGuard.domain.Type;
import unipos.integritySafeGuard.domain.exceptions.SignatureRequiredException;
import unipos.integritySafeGuard.smartcard.SmartCard;
import unipos.integritySafeGuard.smartcard.SmartCardHandler;
import unipos.integritySafeGuard.domain.SignatureInvoice;
import unipos.integritySafeGuard.domain.SignatureJob;
import unipos.integritySafeGuard.domain.SignatureResult;
import unipos.integritySafeGuard.smartcard.SmartCardImpl;
import unipos.integritySafeGuard.smartcard.SmartCardMock;
import unipos.signature.components.signatureOption.SignatureOption;
import unipos.signature.components.signatureOption.SignatureOptionService;
import unipos.signature.components.signatureResult.SignatureResultRepository;
import unipos.signature.components.umsatzZaehler.UmsatzZaehler;
import unipos.signature.components.umsatzZaehler.UmsatzZaehlerService;

import javax.smartcardio.CardException;
import javax.smartcardio.CardNotPresentException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.*;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

/**
 * Created by domin on 15.12.2016.
 */
@Service
public class SignatureServiceImpl implements SignatureService {

    @Autowired
    SignatureOptionService signatureOptionService;
    @Autowired
    SignatureResultRepository signatureResultRepository;
    @Autowired
    UmsatzZaehlerService umsatzZaehlerService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    SmartCardHandler smartCardHandler;


    @Override
    public Invoice signInvoice(Invoice invoice) throws SignatureRequiredException {
        Assert.notNull(invoice, "The given invoice must not be null");
        SignatureOption signatureOption = signatureOptionService.findByStoreGuid(invoice.getStore().getGuid());
        Assert.notNull(signatureOption, "No signatureJob found for the given storeId: " + invoice.getStore().getGuid());
        SignatureResult signatureResult = signatureResultRepository.findFirstByStoreGuidOrderByCreationDateDesc(invoice.getStore().getGuid());
        Assert.notNull(signatureResult, "No to chaining signatureResult found for the given storeId: " + invoice.getStore().getGuid());
        SmartCard smartCard = smartCardHandler.getSmartCardBySerialNo(signatureOption.getCrtSerialNo());
        Assert.notNull(smartCard, "No SmartCard found for the given crtSerialNo: " + signatureOption.getCrtSerialNo());

        if(invoice.getSignatureInvoiceType().isSignatureRequired() && smartCard instanceof SmartCardMock) {
            throw new SignatureRequiredException("For the given belegType " + invoice.getSignatureInvoiceType().getName() + " is it required to create a valid signature! Aborting!");
        }

        List<PaymentMethod> paymentMethods = dataRemoteInterface.getPaymentMethods();

        UmsatzZaehler currentUmsatzZaehler = umsatzZaehlerService.getLatestUmsatzZaehlerForStoreGuid(invoice.getStore().getGuid());

        Assert.notNull(currentUmsatzZaehler, "Unable to create an UmsatzZaehler for the given store: " + invoice.getStore().getGuid());

        //UmsatzZaehler Stuff
        UmsatzZaehler nextUmsatzZaehler = UmsatzZaehler.getFromInvoice(invoice, paymentMethods);
        umsatzZaehlerService.saveUmsatzZaehler(nextUmsatzZaehler);


        invoice.setUmsatzZaehler(nextUmsatzZaehler.getUmsatzZaehler().multiply(new BigDecimal("100")).longValue());

        //Signature Stuff
        SignatureJob signatureJob = signatureOption.signatureJob(String.valueOf(nextUmsatzZaehler.getAutoIncrement()));
//        SignatureJob signatureJob = new SignatureJob();
//        signatureJob.setSecretKey(signatureJob.getSecretKey());
//        signatureJob.setRksSuite(signatureJob.getRksSuite());
//        signatureJob.setTurnOverCounterLengthInBytes(signatureJob.getTurnOverCounterLengthInBytes());
//        signatureJob.setSignatureDeviceAvailable(true);
//        signatureJob.setBelegNr(String.valueOf(invoice.getInvoiceId()));
//        signatureJob.setKassaId(signatureJob.getKassaId());

        List<SignatureTurnoverDetail> signatureTurnoverDetails = invoice.getSingatureTurnoverDetails();

        SignatureInvoice signatureInvoice = SignatureInvoice.getInvoice(signatureJob,
                LocalDateTime.now(),
                signatureTurnoverDetails.stream().filter(tax -> tax.getTaxRate() == 20).findFirst().orElse(
                    SignatureTurnoverDetail.builder()
                            .turnoverGross(BigDecimal.ZERO)
                            .taxRate(20)
                            .turnoverGrossIncludingReducingDiscounts(BigDecimal.ZERO)
                            .discount(BigDecimal.ZERO)
                            .build()
                ).getTurnoverGross(),
                signatureTurnoverDetails.stream().filter(tax -> tax.getTaxRate() == 10).findFirst().orElse(
                SignatureTurnoverDetail.builder()
                        .turnoverGross(BigDecimal.ZERO)
                        .taxRate(10)
                        .turnoverGrossIncludingReducingDiscounts(BigDecimal.ZERO)
                        .discount(BigDecimal.ZERO)
                        .build()
                ).getTurnoverGross(),
                signatureTurnoverDetails.stream().filter(tax -> tax.getTaxRate() == 13).findFirst().orElse(
                        SignatureTurnoverDetail.builder()
                                .turnoverGross(BigDecimal.ZERO)
                                .taxRate(13)
                                .turnoverGrossIncludingReducingDiscounts(BigDecimal.ZERO)
                                .discount(BigDecimal.ZERO)
                                .build()
                ).getTurnoverGross(),
                signatureTurnoverDetails.stream().filter(tax -> tax.getTaxRate() == 0).findFirst().orElse(
                        SignatureTurnoverDetail.builder()
                                .turnoverGross(BigDecimal.ZERO)
                                .taxRate(0)
                                .turnoverGrossIncludingReducingDiscounts(BigDecimal.ZERO)
                                .discount(BigDecimal.ZERO)
                                .build()
                ).getTurnoverGross()
                        .add(signatureTurnoverDetails.stream().map(SignatureTurnoverDetail::getDiscount).reduce(BigDecimal.ZERO, BigDecimal::add).negate()),
                signatureTurnoverDetails.stream().filter(tax -> tax.getTaxRate() == 19).findFirst().orElse(
                        SignatureTurnoverDetail.builder()
                                .turnoverGross(BigDecimal.ZERO)
                                .taxRate(19)
                                .turnoverGrossIncludingReducingDiscounts(BigDecimal.ZERO)
                                .discount(BigDecimal.ZERO)
                                .build()
                ).getTurnoverGross(),
                invoice.getUmsatzZaehler(),
                signatureResult
        );

        signatureInvoice.setSignatureType(Type.valueOf(invoice.getSignatureInvoiceType().name()));

        SignatureResult signatureResult1;
        try {
            signatureResult1 = smartCard.signInvoice(signatureInvoice, signatureJob);
        } catch (Exception ex) {
            umsatzZaehlerService.deleteByGuid(nextUmsatzZaehler.getGuid());
            throw ex;
        }
        signatureResult1.setStoreGuid(invoice.getStore().getGuid());
        signatureResult1.setUmsatzZaehlerGuid(nextUmsatzZaehler.getGuid());
        signatureResult1.setInvoiceSignatureType(Type.valueOf(invoice.getSignatureInvoiceType().name()));
        signatureResultRepository.save(signatureResult1);

        invoice.setQrCode(signatureResult1.toQrCodeRepresentation());

        return invoice;
    }

    @Override
    public Invoice createStartInvoice(Invoice invoice) throws IllegalArgumentException, SignatureRequiredException, SmardCardException, CardException, IOException {
        Assert.notNull(invoice, "The given invoice must not be null");
        SignatureOption signatureOption = signatureOptionService.findByStoreGuid(invoice.getStore().getGuid());
        Assert.notNull(signatureOption, "No signatureOption found for the given storeId: " + invoice.getStore().getGuid());

        SmartCard smartCard = smartCardHandler.getSmartCardBySerialNo(signatureOption.getCrtSerialNo());
        Assert.notNull(smartCard, "No SmartCard found for the given crtSerialNo: " + signatureOption.getCrtSerialNo());

        if (isStartSignatureCreated(invoice.getStore().getGuid())) {
            throw new IllegalArgumentException("There's already a START-Invoice available!");
        }

        if (smartCard instanceof SmartCardMock) {
            throw new SignatureRequiredException("There's no smartCard connected. The START-Invoice requires a valid signature!");
        }

        //get the certificate of the smartCard DER encoded and save it to the signatureOption
        byte[] derCertificateAsBytes;
        try {
            derCertificateAsBytes = smartCard.getCertificate().getEncoded();
        } catch (Exception ex) {
            throw new CardNotPresentException("Das Zertifikat konnte nicht von der SmartCard geladen werden. Versuchen Sie es noch einmal!");
        }
        Assert.notNull(derCertificateAsBytes, "No certificate found on the connected SmartCard. Aborting!");
        String derCertificate = Base64.getEncoder().encodeToString(derCertificateAsBytes);
        signatureOption.setSignatureCertificateDer(derCertificate);

        signatureOptionService.saveSignatureOption(signatureOption);

        //Init the umsatzZaehler
        UmsatzZaehler umsatzZaehler = umsatzZaehlerService.getLatestUmsatzZaehlerForStoreGuid(invoice.getStore().getGuid());

        Assert.notNull(umsatzZaehler, "Unable to create an UmsatzZaehler for the given store: " + invoice.getStore().getGuid());

        umsatzZaehlerService.saveUmsatzZaehler(umsatzZaehler);

        invoice.setUmsatzZaehler(0L);

        SignatureJob signatureJob = signatureOption.signatureJob(String.valueOf(umsatzZaehler.getAutoIncrement()));

        SignatureInvoice signatureInvoice = SignatureInvoice.getStartInvoice(signatureJob, LocalDateTime.now());

        SignatureResult result;
        try {
            result = smartCard.signInvoice(signatureInvoice, signatureJob);
        } catch (Exception ex) {
            umsatzZaehlerService.deleteByGuid(umsatzZaehler.getGuid());

            throw ex;
        }
        result.setUmsatzZaehlerGuid(umsatzZaehler.getGuid());
        result.setStoreGuid(invoice.getStore().getGuid());
        signatureResultRepository.save(result);

        invoice.setQrCode(result.toQrCodeRepresentation());

        return invoice;
    }

    @Override
    public boolean isStartSignatureCreated(String storeGuid) {
        return signatureResultRepository.countByInvoiceSignatureTypeAndStoreGuid(Type.START, storeGuid) > 0;
    }

    @Override
    public boolean isSammelbelegRequired(String storeGuid) {
        SignatureResult signatureResult = signatureResultRepository.findFirstByStoreGuidOrderByCreationDateDesc(storeGuid);

        if(signatureResult == null) {
            return false;
        }

        boolean result;
        try {
            result = signatureResult.getSignatureBase64Url().equals(Base64.getUrlEncoder().withoutPadding().encodeToString("Sicherheitseinrichtung ausgefallen".getBytes("UTF-8")));
        } catch (Exception ignored) {
            return false;
        }
        return result;
    }

    @Override
    public boolean isSignatureDeviceAvailable(String storeGuid) {
        SignatureOption signatureOption = signatureOptionService.findByStoreGuid(storeGuid);
        if (signatureOption == null) {
            return false;
        }
        SmartCard smartCard = smartCardHandler.getSmartCardBySerialNo(signatureOption.getCrtSerialNo());
        return smartCard instanceof SmartCardImpl;
    }

    @Override
    public boolean verifyLatestSignature(String guid) throws SmardCardException, CardException, NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException, CertificateException, NoSuchProviderException {
        SignatureOption signatureOption = signatureOptionService.findByStoreGuid(guid);
        Assert.notNull(signatureOption, "No signatureJob found for the given storeId: " + guid);
        SignatureResult signatureResult = signatureResultRepository.findFirstByStoreGuidOrderByCreationDateDesc(guid);
        Assert.notNull(signatureResult, "No to chaining signatureResult found for the given storeId: " + guid);
        SmartCard smartCard = smartCardHandler.getSmartCardBySerialNo(signatureOption.getCrtSerialNo());
        Assert.notNull(smartCard, "No SmartCard found for the given crtSerialNo: " + signatureOption.getCrtSerialNo());

        X509CertificateHolder certificate = smartCard.getCertificate();
        X509Certificate cert = new JcaX509CertificateConverter().setProvider( "BC" )
                .getCertificate( certificate );

        Signature signature = Signature.getInstance("SHA256withECDSA", "BC");

        signature.initVerify(cert.getPublicKey());
        signature.update((signatureResult.getHeaderBase64Url() + "." + signatureResult.getPayloadBase64Url()).getBytes("UTF-8"));

        return signature.verify(Base64.getUrlDecoder().decode(signatureResult.getSignatureBase64Url()));
    }
}
