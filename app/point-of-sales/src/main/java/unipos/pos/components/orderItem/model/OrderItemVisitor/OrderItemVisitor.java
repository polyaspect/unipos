package unipos.pos.components.orderItem.model.OrderItemVisitor;

import unipos.pos.components.invoice.model.*;
import unipos.pos.components.orderItem.model.*;

/**
 * Created by dominik on 04.09.15.
 */
public abstract class OrderItemVisitor {

    public abstract PaymentInvoiceItem visit(PaymentOrderItem paymentOrderItem);
    public abstract ProductInvoiceItem visit(ProductOrderItem productOrderItem);
    public abstract ReversalInvoiceItem visit(ReversalOrderItem reversalOrderItem);
    public abstract OrderItemDiscountInvoiceItem visit(OrderItemDiscountOrderItem orderItemDiscountOrderItem);
    public abstract OrderDiscountInvoiceItem visit(OrderDiscountOrderItem orderDiscountOrderItem);
    public abstract ChangeInvoiceItem visit(ChangeOrderItem changeOrderItem);
}
