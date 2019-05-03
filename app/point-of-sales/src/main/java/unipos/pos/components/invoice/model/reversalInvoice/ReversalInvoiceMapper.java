package unipos.pos.components.invoice.model.reversalInvoice;

import org.modelmapper.AbstractConverter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import unipos.pos.components.invoice.model.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by domin on 25.02.2016.
 */
public class ReversalInvoiceMapper {

    public static ModelMapper getReversalInvoiceMapper() {
        ModelMapper modelMapper = new ModelMapper();

        addMappings(modelMapper);

        return modelMapper;
    }

    private static void addMappings(ModelMapper modelMapper) {
        modelMapper.addMappings(new PropertyMap<ProductInvoiceItem, ProductInvoiceItem>() {
            @Override
            protected void configure() {
                map().setTurnoverGross(source.getTurnoverGross().negate());
                map().setTurnoverNet(source.getTurnoverNet().negate());
                map().setPrice(source.getPrice().negate());
                map().setTax(source.getTax().negate());
                using(new AbstractConverter<Integer, Integer>() {
                    @Override
                    protected Integer convert(Integer integer) {
                        return integer*-1;
                    }
                }).map(source.getQuantity()).setQuantity(0);
                using(new AbstractConverter<List<Discount>, List<Discount>>() {
                    @Override
                    protected List<Discount> convert(List<Discount> discounts) {
                        return discounts.stream().map(discount -> {
                            discount.setAmount(discount.getAmount().negate());
                            return discount;
                        }).collect(toList());
                    }
                }).map(source.getDiscounts()).setDiscounts(null);
            }
        });

        modelMapper.addMappings(new PropertyMap<ChangeInvoiceItem, ChangeInvoiceItem>() {
            @Override
            protected void configure() {
                map().setQuantity(source.getQuantity() * -1);
                map().setTurnover(source.getTurnover().negate());
            }
        });

        modelMapper.addMappings(new PropertyMap<OrderDiscountInvoiceItem, OrderDiscountInvoiceItem>() {
            @Override
            protected void configure() {
                map().setDiscount(source.getDiscount().negate());
            }
        });

        modelMapper.addMappings(new PropertyMap<OrderItemDiscountInvoiceItem, OrderItemDiscountInvoiceItem>() {
            @Override
            protected void configure() {
                map().setDiscount(source.getDiscount().negate());
            }
        });

        modelMapper.addMappings(new PropertyMap<ReversalInvoiceItem, ReversalInvoiceItem>() {
            @Override
            protected void configure() {
                map().setReversalAmount(source.getReversalAmount().negate());
            }
        });

        modelMapper.addMappings(new PropertyMap<PaymentInvoiceItem, PaymentInvoiceItem>() {
            @Override
            protected void configure() {
                map().setTurnover(source.getTurnover().negate());
            }
        });

        modelMapper.addMappings(new PropertyMap<TaxInvoiceItem, TaxInvoiceItem>() {
            @Override
            protected void configure() {
                map().setAmountGross(source.getAmountGross().negate());
                map().setAmountNet(source.getAmountNet().negate());
                map().setAmountTax(source.getAmountTax().negate());
            }
        });
    }
}
