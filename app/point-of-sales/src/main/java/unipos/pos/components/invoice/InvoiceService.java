package unipos.pos.components.invoice;

import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.pos.components.invoice.exception.InvoiceCreationException;
import unipos.pos.components.invoice.model.Invoice;
import unipos.pos.components.invoice.model.TaxInvoiceItem;
import unipos.pos.components.invoice.model.exception.ReversalInvoiceCreationException;

import javax.servlet.http.HttpServletRequest;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by dominik on 04.09.15.
 */
public interface InvoiceService {

    Invoice createInvoiceFromOrder(String orderId, String cashierUserId, HttpServletRequest request) throws InvoiceCreationException, SignatureException;

    List<Invoice> findAll();

    Invoice findInvoice();

    List<Invoice> findByCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Invoice> findByCreationDate(LocalDateTime date);

    List<Invoice> findAllSinceLastClosedDailySettlement(String authToken, String deviceId);

    /**
     * Ensures that the given Order Entity has not been already transformed into an Invoice
     * @param clientOrderId the clientOrderId of the order to transform into an Invoice
     * @return true if the invoice has already been transformed
     */
    boolean isAlreadyTransformedIntoInvoice(String clientOrderId);

    /**
     * Created an ReverseInvoice and the prints the producedInvoice
     * @param invoiceGuid the Guid of the Invoice you want to revert
     */
    Invoice revertInvoiceByGuid(String invoiceGuid, Company company, Store store, String deviceId, String authToken, HttpServletRequest request) throws IllegalArgumentException, ReversalInvoiceCreationException, SignatureException;

    List<Invoice> findByCompanyGuid(String companyGuid);

    List<Invoice> findZeroInvoicesByCompanyGuid(String companyGuid);

    List<Invoice> findByCompanyGuidAndSignatureType(String companyGuid, Invoice.SignatureInvoiceType signatureInvoiceType);

    List<TaxInvoiceItem> getTaxInvoiceItemsForOrder(String orderGuid);

    /**
     * Takes an persistable ready invoice. Sets the invoice id, persists it into the database and then sends it to the printer.
     * NO SIGNATURE ACTION IS PERFORMED. This method assumes that all signature actions are already done by the sending module
     * @param invoice the invoice you want to update and persist
     * @return the persisted invoice
     */
    Invoice saveExternalInvoice(Invoice invoice, HttpServletRequest request);
}
