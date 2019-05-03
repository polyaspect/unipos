package unipos.pos.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.pos.components.invoice.InvoiceRepository;
import unipos.pos.components.invoice.model.Invoice;
import unipos.pos.components.sequence.SequenceRepository;

/**
 * Created by Dominik on 02.12.2015.
 */

@RestController
@RequestMapping("/syncInvoice")
public class InvoiceSyncController extends SyncController<Invoice> {

    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    SequenceRepository sequenceRepository;

    @Override
    protected void deleteEntity(Invoice entity) {
        //you should never be able to delete an Invoice Entity. So we don't need to implement this method;
    }

    @Override
    public void saveEntity(Invoice entity) {
        invoiceRepository.save(entity);
    }

    @Override
    protected void updateEntity(Invoice log) {
        //you should never be able to update an Invoice Entity. So we don't need to implement this method;

    }

    @Override
    protected void updateSequenceNumber(Invoice entity) {
        sequenceRepository.setSequenceId("INVOICE" + entity.getCreationDate().getYear() + "_" + String.valueOf(entity.getStore().getStoreId()), entity.getInvoiceId());
    }
}
