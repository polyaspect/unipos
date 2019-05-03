package unipos.common.remote.pos.model;

import java.math.BigDecimal;

/**
 * Created by Joyce on 10.10.2015.
 */
public class ChangeInvoiceItem extends InvoiceItem {
    private String productNumber;
    private BigDecimal turnover;
    private int quantity;
    private String label;

    public String getProductNumber() {
        return productNumber;
    }

    public void setProductNumber(String productNumber) {
        this.productNumber = productNumber;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
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
}
