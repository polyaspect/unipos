package unipos.pos.components.invoice;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.pos.components.invoice.model.Invoice;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by dominik on 03.09.15.
 */
public interface InvoiceRepository extends MongoRepository<Invoice, String> {

    List<Invoice> findByCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    Long countByOrderId(String clientOrderId);

    Invoice findByGuid(String invoiceGuid);

    List<Invoice> findByCompany_guid(String companyGuid);

    List<Invoice> findByCompany_guidAndSignatureInvoiceType(String companyGuid, Invoice.SignatureInvoiceType signatureInvoiceType);
}
