package unipos.signature.components.signature;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.joda.time.DateTime;
import org.spockframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.pos.model.Cashier;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.socket.model.Workstation;
import unipos.integritySafeGuard.SmardCardException;
import unipos.integritySafeGuard.domain.exceptions.SignatureRequiredException;
import unipos.signature.components.signatureOption.SignatureOptionService;
import unipos.signature.components.umsatzZaehler.UmsatzZaehlerService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.smartcardio.CardException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.CertificateException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Created by domin on 15.12.2016.
 */
@RestController
@RequestMapping("/signatures")
public class SignatureController {

    @Autowired
    UmsatzZaehlerService umsatzZaehlerService;
    @Autowired
    SignatureOptionService signatureOptionService;
    @Autowired
    SignatureService signatureService;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    PosRemoteInterface posRemoteInterface;

    @RequestMapping(value = "/signInvoice", method = RequestMethod.POST)
    public Invoice signInvoice(@RequestBody Invoice invoice) throws SignatureRequiredException {
        //SignatureStuff
        Assert.notNull(invoice, "This given invoice Entity must not be null");

        signatureService.signInvoice(invoice);

        return invoice;
    }

//    old version
//    @RequestMapping(value = "/createStartInvoice", method = RequestMethod.POST)
//    public Invoice createStartInvoice(@RequestBody Invoice invoice) {
//        Assert.notNull(invoice, "This given invoice Entity must not be null");
//
//        return signatureService.createStartInvoice(invoice);
//    }

    @RequestMapping(value = "/createStartInvoice", method = RequestMethod.POST)
    public Invoice createStartInvoice(HttpServletRequest request) throws IOException, SignatureRequiredException, CardException, SmardCardException {

        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
        Assert.notNull(store, "No associated store found for the logged in User");

        Company company = dataRemoteInterface.getCompanyByGuid(store.getCompanyGuid());
        Assert.notNull(company, "No assiciated company found for the logged in User");

        String deviceId = RequestHandler.getDeviceToken(request);

        Workstation workstation = socketRemoteInterface.findByDeviceId(deviceId);
        Assert.notNull(workstation, "No workstation found for the deviceGuid: " + deviceId);
        User cashierFromWorkstation = authRemoteInterface.findUserByAuthToken(RequestHandler.getAuthToken(request));
        Assert.notNull(cashierFromWorkstation, "No cashier found for the given CashierId: " + workstation.getCashierId());

        Invoice startInvoicePlaceHolder = new Invoice();
        startInvoicePlaceHolder.setStore(store);
        startInvoicePlaceHolder.setCompany(company);
        startInvoicePlaceHolder.setCreationDate(LocalDateTime.now());
        startInvoicePlaceHolder.setTurnoverNet(BigDecimal.ZERO);
        startInvoicePlaceHolder.setTurnoverGross(BigDecimal.ZERO);
        startInvoicePlaceHolder.setUmsatzZaehler(0L);
        startInvoicePlaceHolder.setCashier(Cashier.builder()
                .name(cashierFromWorkstation.getSurname() + " " + cashierFromWorkstation.getName())
                .userId(String.valueOf(cashierFromWorkstation.getUserId()))
                .build());
        startInvoicePlaceHolder.setDeviceId(deviceId);
        startInvoicePlaceHolder.setInvoiceType(Invoice.InvoiceType.invoice);
        startInvoicePlaceHolder.setGuid(UUID.randomUUID().toString());
        startInvoicePlaceHolder.setSignatureInvoiceType(Invoice.SignatureInvoiceType.START);

        signatureService.createStartInvoice(startInvoicePlaceHolder);

        posRemoteInterface.externalInvoice(startInvoicePlaceHolder, request);

        return startInvoicePlaceHolder;
    }

    @RequestMapping(value = "/createNullInvoice", method = RequestMethod.POST)
    public Invoice createNullInvoice(HttpServletRequest request, @RequestParam Invoice.SignatureInvoiceType invoiceType) throws JsonProcessingException, SignatureRequiredException {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
        Assert.notNull(store, "No associated store found for the logged in User");

        Company company = dataRemoteInterface.getCompanyByGuid(store.getCompanyGuid());
        Assert.notNull(company, "No assiciated company found for the logged in User");

        String deviceId = RequestHandler.getDeviceToken(request);

        Workstation workstation = socketRemoteInterface.findByDeviceId(deviceId);
        Assert.notNull(workstation, "No workstation found for the deviceGuid: " + deviceId);
        User cashierFromWorkstation = authRemoteInterface.findUserByAuthToken(RequestHandler.getAuthToken(request));
        Assert.notNull(cashierFromWorkstation, "No cashier found for the given CashierId: " + workstation.getCashierId());

        Invoice nullInvoice = new Invoice();
        nullInvoice.setStore(store);
        nullInvoice.setCompany(company);
        nullInvoice.setCreationDate(LocalDateTime.now());
        nullInvoice.setTurnoverNet(BigDecimal.ZERO);
        nullInvoice.setTurnoverGross(BigDecimal.ZERO);
        nullInvoice.setUmsatzZaehler(0L);
        nullInvoice.setCashier(Cashier.builder()
                .name(cashierFromWorkstation.getSurname() + " " + cashierFromWorkstation.getName())
                .userId(String.valueOf(cashierFromWorkstation.getUserId()))
                .build());
        nullInvoice.setDeviceId(deviceId);
        nullInvoice.setInvoiceType(Invoice.InvoiceType.invoice);
        nullInvoice.setGuid(UUID.randomUUID().toString());
        nullInvoice.setSignatureInvoiceType(invoiceType);

        signatureService.signInvoice(nullInvoice);

        posRemoteInterface.externalInvoice(nullInvoice, request);

        return nullInvoice;
    }

    @RequestMapping(value = "/createNullInvoiceWithoutAuthToken", method = RequestMethod.POST)
    public Invoice createNullInvoiceWithoutAuthToken(HttpServletRequest request, @RequestParam Invoice.SignatureInvoiceType invoiceType, @RequestParam String storeGuid) throws JsonProcessingException, SignatureRequiredException {
        Store store = dataRemoteInterface.getStoreByGuid(storeGuid);
        Assert.notNull(store, "No associated store found for the logged in User");

        Company company = dataRemoteInterface.getCompanyByGuid(store.getCompanyGuid());
        Assert.notNull(company, "No assiciated company found for the logged in User");

        Invoice nullInvoice = new Invoice();
        nullInvoice.setStore(store);
        nullInvoice.setCompany(company);
        nullInvoice.setCreationDate(LocalDateTime.now());
        nullInvoice.setTurnoverNet(BigDecimal.ZERO);
        nullInvoice.setTurnoverGross(BigDecimal.ZERO);
        nullInvoice.setUmsatzZaehler(0L);
        nullInvoice.setCashier(Cashier.builder()
                .name("Nullbeleg Kassier")
                .userId(String.valueOf(-1))
                .build());
        nullInvoice.setInvoiceType(Invoice.InvoiceType.invoice);
        nullInvoice.setGuid(UUID.randomUUID().toString());
        nullInvoice.setSignatureInvoiceType(invoiceType);

        signatureService.signInvoice(nullInvoice);

        posRemoteInterface.externalInvoice(nullInvoice, request);

        return nullInvoice;
    }

    @RequestMapping(value = {"/isSignatureAllowed/{storeGuid}", "/isSignatureEnabled/{storeGuid}"}, method = RequestMethod.GET)
    public Boolean isSignatureAllowed(HttpServletRequest request, @PathVariable ("storeGuid") String storeGuid) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
        if (store == null && storeGuid != null) {
            store = dataRemoteInterface.getStoreByGuid(storeGuid);
        }

        Assert.notNull(store, "No assosiated Store found for the logged in user");

        return signatureService.isStartSignatureCreated(store.getGuid());
    }

    /**
     * This method is used to evaluate the status of the signatureModule. Just returns true, that the other modules can verify the status of the signature Module
     * @return true to verify that the module is up and running
     */
    @RequestMapping(value = "/isUpAndRunning", method = RequestMethod.GET)
    public boolean isUpAndRunning() {
        return true;
    }

    @RequestMapping(value = "/isSammelbelegRequired", method = RequestMethod.GET)
    public boolean isSammelbelegRequired(HttpServletRequest request) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
        Assert.notNull(store, "No store found for the associated Request");

        return signatureService.isSammelbelegRequired(store.getGuid());
    }

    @RequestMapping(value = "/isSignatureDeviceAvailable", method = RequestMethod.GET)
    public boolean isSignatureDeviceAvailable(HttpServletRequest request) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
        Assert.notNull(store, "No store found for the associated Request");

        return signatureService.isSignatureDeviceAvailable(store.getGuid());
    }

    @RequestMapping(value = "/isSammelbelegRequiredAndDeviceAvailable", method = RequestMethod.GET)
    public boolean isSammelbelegRequiredAndDeviceAvailable(HttpServletRequest request,  @RequestParam("storeGuid") String storeGuid) {
        Store store = dataRemoteInterface.getStoreByGuid(storeGuid);
        Assert.notNull(store, "No store found for the associated Request");

        return signatureService.isSammelbelegRequired(store.getGuid()) && signatureService.isSignatureDeviceAvailable(store.getGuid());
    }

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public boolean verifyLatestSignature(HttpServletRequest request) throws CertificateException, UnsupportedEncodingException, NoSuchAlgorithmException, CardException, SignatureException, InvalidKeyException, SmardCardException, NoSuchProviderException {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);

        return signatureService.verifyLatestSignature(store.getGuid());
    }

    @ExceptionHandler(SignatureRequiredException.class)
    public void signatureRequiredHandler(HttpServletResponse response) throws IOException {
        response.sendError(400, "A valid signature is required for this type of beleg!");
    }
}
