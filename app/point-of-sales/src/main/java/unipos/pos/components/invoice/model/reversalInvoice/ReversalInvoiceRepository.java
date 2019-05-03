package unipos.pos.components.invoice.model.reversalInvoice;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by domin on 26.02.2016.
 */
public interface ReversalInvoiceRepository extends MongoRepository<ReversalInvoice, String> {
    ReversalInvoice findByReversedInvoiceGuid(String guid);
}
