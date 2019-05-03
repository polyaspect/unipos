package unipos.common.remote.pos.model;

import lombok.Data;

/**
 * Created by domin on 25.02.2016.
 */
@Data
public class ReversalInvoice extends Invoice {

    private String reversedInvoiceGuid;
    private Long reversedInvoiceNumber;

    public ReversalInvoice() {
        invoiceType = InvoiceType.reversalInvoice;
    }

    public ReversalInvoice(String reversedInvoiceGuid) {
        this.reversedInvoiceGuid = reversedInvoiceGuid;
    }
}
