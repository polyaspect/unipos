package unipos.pos.components.shared;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import unipos.pos.components.invoice.model.*;
import unipos.pos.components.orderItem.model.*;

/**
 * Created by dominik on 04.09.15.
 */
public class ModelMapperGetter {

    public static ModelMapper getModelMapperWithoutIdMapping() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(getProductInvoiceMapperSettings());
        modelMapper.addMappings(getOrderDiscountInvoiceMapperSettings());
        modelMapper.addMappings(getOrderItemDiscountInvoiceMapperSettings());
        modelMapper.addMappings(getPaymentInvoiceMapperSettings());
        modelMapper.addMappings(getReverseInvoiceMapperSettings());
        modelMapper.addMappings(getChangeInvoiceMapperSettings());
        return modelMapper;
    }

    public static PropertyMap<ProductOrderItem, ProductInvoiceItem> getProductInvoiceMapperSettings() {
        return new PropertyMap<ProductOrderItem, ProductInvoiceItem>() {
            @Override
            protected void configure() {
                skip().setId(null);
                skip().setProduct(null);
                skip().setType(null);
                map().setPrice(source.getTurnover());
                map().setTurnoverGross(source.getTurnover());
                map().setOrderItemId(source.getOrderItemId());
            }
        };
    }

    public static PropertyMap<ReversalOrderItem, ReversalInvoiceItem> getReverseInvoiceMapperSettings() {
        return new PropertyMap<ReversalOrderItem, ReversalInvoiceItem>() {
            @Override
            protected void configure() {
                skip().setId(null);
                skip().setType(null);
            }
        };
    }

    public static PropertyMap<OrderDiscountOrderItem, OrderDiscountInvoiceItem> getOrderDiscountInvoiceMapperSettings() {
        return new PropertyMap<OrderDiscountOrderItem, OrderDiscountInvoiceItem>() {
            @Override
            protected void configure() {
                skip().setId(null);
                skip().setType(null);

            }
        };
    }

    public static PropertyMap<OrderItemDiscountOrderItem, OrderItemDiscountInvoiceItem> getOrderItemDiscountInvoiceMapperSettings() {
        return new PropertyMap<OrderItemDiscountOrderItem, OrderItemDiscountInvoiceItem>() {
            @Override
            protected void configure() {
                skip().setId(null);
                skip().setType(null);

            }
        };
    }

    public static PropertyMap<PaymentOrderItem, PaymentInvoiceItem> getPaymentInvoiceMapperSettings() {
        return new PropertyMap<PaymentOrderItem, PaymentInvoiceItem>() {
            @Override
            protected void configure() {
                skip().setId(null);
                skip().setType(null);
            }
        };
    }

    public static PropertyMap<ChangeOrderItem, ChangeInvoiceItem> getChangeInvoiceMapperSettings() {
        return new PropertyMap<ChangeOrderItem, ChangeInvoiceItem>() {
            @Override
            protected void configure() {
                skip().setId(null);
                skip().setType(null);
            }
        };
    }
}
