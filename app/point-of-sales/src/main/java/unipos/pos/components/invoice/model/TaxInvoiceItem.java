package unipos.pos.components.invoice.model;

import unipos.pos.components.invoice.model.reversalInvoice.InvoiceItemVisitor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Dominik on 26.01.2016.
 */

public class TaxInvoiceItem extends InvoiceItem {

    private String taxRateId; //TaxRateId of the data module
    private int taxRate = -1;
    private BigDecimal amountGross;
    private BigDecimal amountNet;
    private BigDecimal amountTax;
    private InvoiceItemType type = InvoiceItemType.taxInvoiceItem;

    public TaxInvoiceItem() {
    }

    public String getTaxRateId() {
        return taxRateId;
    }

    public void setTaxRateId(String taxRateId) {
        this.taxRateId = taxRateId;
    }

    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    public BigDecimal getAmountGross() {
        return amountGross;
    }

    public void setAmountGross(BigDecimal amountGross) {
        this.amountGross = amountGross;
    }

    public BigDecimal getAmountNet() {
        return amountNet;
    }

    public void setAmountNet(BigDecimal amountNet) {
        this.amountNet = amountNet;
    }

    public BigDecimal getAmountTax() {
        return amountTax;
    }

    public void setAmountTax(BigDecimal amountTax) {
        this.amountTax = amountTax;
    }

    public InvoiceItemType getType() {
        return type;
    }

    public void setType(InvoiceItemType type) {
        this.type = type;
    }

    public void calcTaxAndGross() {
        if(taxRate == -1 || getAmountGross() == null) {
            return;
        }
        amountNet = amountGross.divide(
                BigDecimal.ONE
                        .add(new BigDecimal(String.valueOf(taxRate))
                                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)), 2, RoundingMode.HALF_UP);
        amountTax = amountGross.subtract(amountNet);
    }

    @Override
    public InvoiceItem accept(InvoiceItemVisitor visitor) {
        return visitor.visit(this);
    }
}
