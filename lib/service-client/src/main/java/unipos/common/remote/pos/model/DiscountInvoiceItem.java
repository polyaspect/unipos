package unipos.common.remote.pos.model;


import java.math.BigDecimal;

/**
 * Created by dominik on 04.09.15.
 */
public abstract class DiscountInvoiceItem extends InvoiceItem {

    protected String discountId;
    protected BigDecimal discount;
    protected String label;

    public String getDiscountId() {
        return discountId;
    }

    public void setDiscountId(String discountId) {
        this.discountId = discountId;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
