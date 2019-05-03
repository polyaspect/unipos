package unipos.pos.components.orderItem.model.OrderItemVisitor;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.product.Product;
import unipos.pos.components.invoice.model.*;
import unipos.pos.components.orderItem.model.*;
import unipos.pos.components.shared.ModelMapperGetter;

/**
 * Created by dominik on 04.09.15.
 */

@Component
public class OrderItemToInvoiceMapperVisitor extends OrderItemVisitor {
    ModelMapper modelMapper = ModelMapperGetter.getModelMapperWithoutIdMapping();
    @Autowired
    DataRemoteInterface dataRemoteInterface;

    @Override
    public PaymentInvoiceItem visit(PaymentOrderItem paymentOrderItem) {
        PaymentInvoiceItem paymentInvoiceItem = modelMapper.map(paymentOrderItem, PaymentInvoiceItem.class);
        return paymentInvoiceItem;
    }

    @Override
    public ProductInvoiceItem visit(ProductOrderItem productOrderItem) {
        ProductInvoiceItem productInvoiceItem = modelMapper.map(productOrderItem, ProductInvoiceItem.class);

        Product product = dataRemoteInterface.getProductByProductIdentifier(productInvoiceItem.getProductNumber());
        if(product != null && product.getCategory() != null && product.getCategory().getTaxRate() != null) {
            productInvoiceItem.setTaxRate(product.getCategory().getTaxRate().getPercentage());
            productInvoiceItem.setProduct(product);
        }

        productInvoiceItem.calcTaxAndGross();

        return productInvoiceItem;
    }

    @Override
    public ReversalInvoiceItem visit(ReversalOrderItem reversalOrderItem) {
        ReversalInvoiceItem reversalInvoiceItem = modelMapper.map(reversalOrderItem, ReversalInvoiceItem.class);
        return reversalInvoiceItem;
    }

    @Override
    public OrderItemDiscountInvoiceItem visit(OrderItemDiscountOrderItem orderItemDiscountOrderItem) {
        OrderItemDiscountInvoiceItem orderItemDiscountInvoiceItem = modelMapper.map(orderItemDiscountOrderItem, OrderItemDiscountInvoiceItem.class);
        return orderItemDiscountInvoiceItem;
    }

    @Override
    public OrderDiscountInvoiceItem visit(OrderDiscountOrderItem orderDiscountOrderItem) {
        OrderDiscountInvoiceItem orderDiscountInvoiceItem = modelMapper.map(orderDiscountOrderItem, OrderDiscountInvoiceItem.class);
        return orderDiscountInvoiceItem;
    }

    @Override
    public ChangeInvoiceItem visit(ChangeOrderItem changeOrderItem) {
        ChangeInvoiceItem changeInvoiceItem = modelMapper.map(changeOrderItem, ChangeInvoiceItem.class);
        return changeInvoiceItem;
    }


}
