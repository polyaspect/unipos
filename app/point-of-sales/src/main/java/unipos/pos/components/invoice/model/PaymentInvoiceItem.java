package unipos.pos.components.invoice.model;

import unipos.pos.components.invoice.model.reversalInvoice.InvoiceItemVisitor;

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
    private InvoiceItemType type = InvoiceItemType.paymentInvoiceItem;


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

    public InvoiceItemType getType() {
        return type;
    }

    public void setType(InvoiceItemType type) {
        this.type = type;
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

    @Override
    public String toString() {
        return "PaymentInvoiceItem{" +
                "paymentMethod='" + paymentMethod + '\'' +
                ", paymentMethodGuid=" + paymentMethodGuid +
                ", turnover=" + turnover +
                ", label=" + label +
                ", type=" + type +
                '}';
    }

    @Override
    public InvoiceItem accept(InvoiceItemVisitor visitor) {
        return visitor.visit(this);
    }
}
