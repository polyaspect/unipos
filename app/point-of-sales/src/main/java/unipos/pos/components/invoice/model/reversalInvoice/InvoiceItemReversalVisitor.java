package unipos.pos.components.invoice.model.reversalInvoice;

import org.modelmapper.ModelMapper;
import unipos.pos.components.invoice.model.*;

/**
 * Created by domin on 25.02.2016.
 */
public class InvoiceItemReversalVisitor extends InvoiceItemVisitor {

    ModelMapper modelMapper = ReversalInvoiceMapper.getReversalInvoiceMapper();

    @Override
    public ChangeInvoiceItem visit(ChangeInvoiceItem invoiceItem) {
        return modelMapper.map(invoiceItem, ChangeInvoiceItem.class);
    }

    @Override
    public OrderDiscountInvoiceItem visit(OrderDiscountInvoiceItem invoiceItem) {
        return modelMapper.map(invoiceItem, OrderDiscountInvoiceItem.class);
    }

    @Override
    public OrderItemDiscountInvoiceItem visit(OrderItemDiscountInvoiceItem invoiceItem) {
        return modelMapper.map(invoiceItem, OrderItemDiscountInvoiceItem.class);
    }

    @Override
    public PaymentInvoiceItem visit(PaymentInvoiceItem invoiceItem) {
        return modelMapper.map (invoiceItem, PaymentInvoiceItem.class);
    }

    @Override
    public ProductInvoiceItem visit(ProductInvoiceItem invoiceItem) {
        return modelMapper.map (invoiceItem, ProductInvoiceItem.class);
    }

    @Override
    public ReversalInvoiceItem visit(ReversalInvoiceItem invoiceItem) {
        return modelMapper.map (invoiceItem, ReversalInvoiceItem.class);
    }

    @Override
    public TaxInvoiceItem visit(TaxInvoiceItem invoiceItem) {
        return modelMapper.map (invoiceItem, TaxInvoiceItem.class);
    }
}
