package unipos.pos.components.invoice.model;


import unipos.common.remote.data.model.product.Product;
import unipos.pos.components.invoice.model.reversalInvoice.InvoiceItemVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dominik on 04.09.15.
 */
public class ProductInvoiceItem extends InvoiceItem {

    public String productNumber;
    public int quantity;
    public BigDecimal turnoverNet;
    public BigDecimal turnoverGross;
    //Das ist der Verkaufspreis des Produktes, nicht abzüglich der Rabatte; Berechnungsbasis in Brutto.
    public BigDecimal price;
    public int taxRate = -1;
    public BigDecimal tax;
    public String label;
    public List<Discount> discounts = new ArrayList<>();
    public Product product;
    private InvoiceItemType type = InvoiceItemType.productInvoiceItem;

    public ProductInvoiceItem() {super();}

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    public BigDecimal getTurnoverNet() {
        return turnoverNet;
    }

    public void setTurnoverNet(BigDecimal turnoverNet) {
        this.turnoverNet = turnoverNet;
    }

    public BigDecimal getTurnoverGross() {
        return turnoverGross;
    }

    public void setTurnoverGross(BigDecimal turnoverGross) {
        this.turnoverGross = turnoverGross;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getTax() {
        return tax;
    }

    public void setTax(BigDecimal tax) {
        this.tax = tax;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public InvoiceItemType getType() {
        return type;
    }

    public void setType(InvoiceItemType type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public boolean isReversalApplied() {
        return reversalApplied;
    }

    public void setReversalApplied(boolean reversalApplied) {
        this.reversalApplied = reversalApplied;
    }

    @Override
    public String toString() {
        return "ProductInvoiceItem{" +
                "productNumber='" + productNumber + "'" +
                ", quantity=" + quantity +
                ", label='" + label + '\'' +
                ", turnoverNet=" + turnoverNet +
                ", turnoverGross=" + turnoverGross +
                ", price=" + price +
                ", taxRate=" + taxRate +
                ", tax=" + tax +
                ", type=" + type +
                '}';
    }

    public void calcTaxAndGross() {
        if(taxRate == -1 || (turnoverGross== null && price == null)) {
            return;
        }
        if(turnoverGross == null) {
            turnoverGross = price;
        }
        turnoverNet = turnoverGross.divide(
                new BigDecimal("1")
                        .add(new BigDecimal(String.valueOf(taxRate))
                                .divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)), 4, RoundingMode.HALF_UP);
        tax = turnoverGross.subtract(turnoverNet);
    }

    public void applyDiscount(OrderItemDiscountInvoiceItem orderItemDiscountInvoiceItem) throws IllegalArgumentException {
        if(orderItemDiscountInvoiceItem.isReversalApplied()) {
            return;
        }
        if(orderItemDiscountInvoiceItem.getReceiverOrderItemId().equals(orderItemId)) {
            if(orderItemDiscountInvoiceItem.getDiscount().compareTo(turnoverGross) == 1) {
                throw new IllegalArgumentException("Not able to substract more discount than is available in the TurnoverGross");
            }
            discounts.add(Discount.builder()
                    .amount(orderItemDiscountInvoiceItem.getDiscount())
                    .label(orderItemDiscountInvoiceItem.getLabel())
                    .discountId(orderItemDiscountInvoiceItem.getDiscountId())
                    .orderItemId(orderItemDiscountInvoiceItem.getOrderItemId())
                    .type(Discount.Type.LINE)
                    .build());
            setTurnoverGross(getTurnoverGross().subtract(orderItemDiscountInvoiceItem.getDiscount()));
            calcTaxAndGross();
        }
    }

    @Override
    public InvoiceItem accept(InvoiceItemVisitor visitor) {
        return visitor.visit(this);
    }
}
