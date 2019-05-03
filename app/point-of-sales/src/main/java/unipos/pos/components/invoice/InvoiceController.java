package unipos.pos.components.invoice;

import java.security.SignatureException;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import unipos.common.container.RequestHandler;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.pos.components.invoice.model.Invoice;
import unipos.pos.components.invoice.model.TaxInvoiceItem;
import unipos.pos.components.invoice.model.exception.ReversalInvoiceCreationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Created by dominik on 08.09.15.
 */

@RestController
@RequestMapping("/invoices")
public class InvoiceController {

    @Autowired
    InvoiceService invoiceService;
    @Autowired
    LogRemoteInterface logRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    PosRemoteInterface posRemoteInterface;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;

    @RequestMapping(value = "/createFromOrder", method = RequestMethod.POST)
    public ResponseEntity<?> createInvoiceFromOrder(@RequestBody InvoiceCreationRequest invoiceCreationRequest, HttpServletResponse response, HttpServletRequest request) throws IOException, SignatureException {
        //First check if its valid to create a new Order
        if (!posRemoteInterface.isCreationAllowed(request)) {
            return ResponseEntity.status(423).contentType(MediaType.TEXT_PLAIN).body("DAILYSETTLEMENT_ALREADY_EXECUTED");
        }

        if (invoiceService.isAlreadyTransformedIntoInvoice(invoiceCreationRequest.getClientOrderId())) {
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("INVOICE_ALREADY_CREATED");
        }

        // JUST CHECKING AT THIS OCCATION IF AUTH TOKEN IS STILL VALID.. IF NOT, CREATE A NEW ORDER WITH ERROR
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(request);
        if(store == null){
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body("AUTH_TOKEN_INVALID");
        }

        try{
            return ResponseEntity.ok(invoiceService.createInvoiceFromOrder(invoiceCreationRequest.getClientOrderId(), invoiceCreationRequest.getCashierId(), request));
        }catch(SignatureException ex){
            return ResponseEntity.badRequest().contentType(MediaType.TEXT_PLAIN).body(ex.getMessage());
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Invoice> listInvoices() {
        List<Invoice> invoices = invoiceService.findAll();
        return invoices;
    }

    @RequestMapping(value = "/findAllSinceLastClosedDailySettlement", method = RequestMethod.GET)
    public List<Invoice> findAllSinceLastClosedDailySettlement(HttpServletRequest request) {
        return invoiceService.findAllSinceLastClosedDailySettlement(RequestHandler.getAuthToken(request), RequestHandler.getDeviceToken(request));
    }

    @RequestMapping(value = "/findOne", method = RequestMethod.GET)
    public Invoice getSingleInvoice() {
        return invoiceService.findInvoice();
    }

    @RequestMapping(value = "/findByCreationDateBetween", method = RequestMethod.GET)
    public List<Invoice> getInvoiceByCreationDateBetween(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return invoiceService.findByCreationDateBetween(startDate.minusNanos(1), endDate.plusNanos(1));
    }

    @RequestMapping(value = "/findByCreationDate", method = RequestMethod.GET)
    public List<Invoice> getInvoiceByCreationDate(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime date) {
        return invoiceService.findByCreationDate(date);
    }

    @RequestMapping(value = "/findByCompanyGuid/{companyGuid}", method = RequestMethod.GET)
    public List<Invoice> findByCompanyGuid(@PathVariable("companyGuid") String companyGuid, HttpServletResponse response) throws IOException {
        if (companyGuid == null || companyGuid.isEmpty()) {
            response.sendError(400, "No companyGuid was found!!!");
            return null;
        }

        return invoiceService.findByCompanyGuid(companyGuid);
    }

    @RequestMapping(value = "/findZeroInvoicesByCompanyGuid/{companyGuid}", method = RequestMethod.GET)
    public List<Invoice> findZeroInvoicesByCompanyGuid(@PathVariable("companyGuid") String companyGuid, HttpServletResponse response) throws IOException {
        if (companyGuid == null || companyGuid.isEmpty()) {
            response.sendError(400, "No companyGuid was found!!!");
            return null;
        }

        return invoiceService.findZeroInvoicesByCompanyGuid(companyGuid);
    }

    @RequestMapping(value = "/findByCompanyGuidAndSignatureInvoiceType", method = RequestMethod.GET)
    public List<Invoice> findByCompanyGuidAndSignatureInvoiceType(@RequestParam("companyGuid") String companyGuid, @RequestParam("signatureInvoiceType") String signatureInvoiceTypeString, HttpServletResponse response) throws IOException {
        if (companyGuid == null || companyGuid.isEmpty()) {
            response.sendError(400, "The parameter 'companyGuid' cannot be empty");
            return null;
        }
        Invoice.SignatureInvoiceType signatureInvoiceType = Invoice.SignatureInvoiceType.valueOf(signatureInvoiceTypeString);
        if(signatureInvoiceType == null){
            response.sendError(400, "The requested SignatureInvoiceType does not exist");
            return null;
        }

        return invoiceService.findByCompanyGuidAndSignatureType(companyGuid, signatureInvoiceType);
    }

    @RequestMapping(value = "/revertInvoice", method = RequestMethod.POST)
    public Invoice revertInvoice(@RequestParam("invoiceGuid") String invoiceGuid, HttpServletRequest request, HttpServletResponse response) throws IOException, SignatureException {
        //First check if its valid to create a new Order
        if (!posRemoteInterface.isCreationAllowed(request)) {
            response.sendError(423, "Could not revert Invoice because DailySettlement was already executed \"today\"");
            return null;
        }

        String authToken = RequestHandler.getAuthToken(request);
        String deviceId = RequestHandler.getDeviceToken(request);

        Assert.notNull(authToken, "AuthToken must not be null");
        Assert.notNull(deviceId, "Devicetoken must not be null");

        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(authToken, deviceId);
        Company company = dataRemoteInterface.getCompanyByGuid(store.getCompanyGuid());

        Assert.notNull(store, "No store found for the given authToken - DeviceToken combination");
        Assert.notNull(store, "No company found for the given company Guid: " + store.getCompanyGuid());

        try {
            return invoiceService.revertInvoiceByGuid(invoiceGuid, company, store, deviceId, authToken, request);
        } catch (ReversalInvoiceCreationException e) {
            response.sendError(500, e.getMessage());
            return null;
        }
    }

    @RequestMapping(value = "/taxPreviewForOrder/{orderGuid}", method = RequestMethod.GET)
    public List<TaxInvoiceItem> getTaxInvoiceItemForOrder(@PathVariable("orderGuid") String orderGuid) {
        Assert.notNull(orderGuid, "Order Guid must not be null!");

        return invoiceService.getTaxInvoiceItemsForOrder(orderGuid);
    }

    /**
     * This method takes an invoice, that has been created in another modul and stores it into the database
     * @param invoice the invoice you want to persist
     * @return the persisted invoice entity
     */
    @RequestMapping(value = "/externalInvoice", method = RequestMethod.POST)
    public Invoice createExternalInvoice(@RequestBody Invoice invoice, HttpServletRequest request) {
        Assert.notNull(invoice, "The given Invoice must not be null");

        return invoiceService.saveExternalInvoice(invoice, request);
    }
}
