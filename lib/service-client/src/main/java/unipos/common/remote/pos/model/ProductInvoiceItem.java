package unipos.common.remote.pos.model;



import unipos.common.remote.data.model.product.Product;

import java.math.BigDecimal;
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
    public List<Discount> discounts;
    public String orderItemId; //I need this orderItemId, so that I can use the old Id of the OrderItem for the Discounts.
    public Product product;
    public boolean reversalApplied; //This flag indicates of this productOrderItem has been storniert.

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

    public String getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
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
}
