package unipos.common.remote.pos.model;

import java.math.BigDecimal;

/**
 * Created by dominik on 04.09.15.
 */
public class PaymentInvoiceItem extends InvoiceItem {

    private String paymentMethod;
    private String paymentMethodGuid;
    private BigDecimal turnover;
    private String label;
    private boolean isBarumsatz;

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTurnover() {
        return turnover;
    }

    public void setTurnover(BigDecimal turnover) {
        this.turnover = turnover;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getPaymentMethodGuid() {
        return paymentMethodGuid;
    }

    public void setPaymentMethodGuid(String paymentMethodGuid) {
        this.paymentMethodGuid = paymentMethodGuid;
    }

    public boolean isBarumsatz() {
        return isBarumsatz;
    }

    public void setBarumsatz(boolean barumsatz) {
        isBarumsatz = barumsatz;
    }
}
