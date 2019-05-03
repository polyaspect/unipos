package unipos.pos.components.invoice.model.reversalInvoice;

import lombok.Builder;
import lombok.Data;
import org.modelmapper.ModelMapper;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.pos.components.invoice.model.Invoice;
import unipos.pos.components.invoice.model.InvoiceItem;
import unipos.pos.components.invoice.model.exception.ReversalInvoiceCreationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by domin on 25.02.2016.
 */
@Data
@Document(collection = "invoices")
public class ReversalInvoice extends Invoice {

    private String reversedInvoiceGuid;
    private Long reversedInvoiceNumber;

    public ReversalInvoice() {
        invoiceType = InvoiceType.reversalInvoice;
    }

    public void createReversalInvoiceFromInvoice(Invoice invoice) throws ReversalInvoiceCreationException {
        ModelMapper modelMapper = ReversalInvoiceMapper.getReversalInvoiceMapper();
        InvoiceItemVisitor visitor = new InvoiceItemReversalVisitor();

        this.setReversedInvoiceGuid(invoice.getGuid());
        this.setReversedInvoiceNumber(invoice.getInvoiceId());

        this.setCashier(invoice.getCashier());
        this.setCompany(invoice.getCompany());
        this.setCreationDate(LocalDateTime.now());
        this.setDeviceId(invoice.getDeviceId());
        this.setGuid(UUID.randomUUID().toString());
        this.setOrderId(invoice.getOrderId());
        this.setStore(invoice.getStore());
        this.setTurnoverGross(invoice.getTurnoverGross().negate());
        this.setTurnoverNet(invoice.getTurnoverNet().negate());

        List<InvoiceItem> reversedInvoiceItems = new ArrayList<>();

        for(InvoiceItem invoiceItem : invoice.getInvoiceItems()) {
            InvoiceItem reversedInvoiceItem = invoiceItem.accept(visitor);
            reversedInvoiceItems.add(reversedInvoiceItem);
        }

        if(reversedInvoiceItems.size() != invoice.getInvoiceItems().size()) {
            throw new ReversalInvoiceCreationException("Unable to create a correct ReversalInvoice from the given Invoice. InvoiceItem size is " + invoice.getInvoiceItems().size() + " but reversalInvoiceItems size is " + reversedInvoiceItems.size());
        }

        this.setInvoiceItems(reversedInvoiceItems);

        generateHash();
    }

    //ToDo: New ToString() Method here for HashGeneration
}
