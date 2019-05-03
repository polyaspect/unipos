package unipos.pos.components.invoice.model.reversalInvoice;

import unipos.pos.components.invoice.model.*;

/**
 * Created by domin on 25.02.2016.
 */
public abstract class InvoiceItemVisitor {

    public abstract ChangeInvoiceItem visit(ChangeInvoiceItem changeInvoiceItem);
    public abstract OrderDiscountInvoiceItem visit(OrderDiscountInvoiceItem changeInvoiceItem);
    public abstract OrderItemDiscountInvoiceItem visit(OrderItemDiscountInvoiceItem changeInvoiceItem);
    public abstract PaymentInvoiceItem visit(PaymentInvoiceItem changeInvoiceItem);
    public abstract ProductInvoiceItem visit(ProductInvoiceItem productInvoiceItem);
    public abstract ReversalInvoiceItem visit(ReversalInvoiceItem changeInvoiceItem);
    public abstract TaxInvoiceItem visit(TaxInvoiceItem changeInvoiceItem);

}
