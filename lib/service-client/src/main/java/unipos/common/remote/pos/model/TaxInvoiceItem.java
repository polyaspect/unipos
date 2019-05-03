package unipos.common.remote.pos.model;

import java.math.BigDecimal;

/**
 * Created by Dominik on 26.01.2016.
 */
public class TaxInvoiceItem extends InvoiceItem {

    private String taxRateId; //TaxRateId of the data module
    private int taxRate;
    private BigDecimal amountGross;
    private BigDecimal amountNet;
    private BigDecimal amountTax;

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

    @Override
    public InvoiceItemType getType() {
        return type;
    }

    @Override
    public void setType(InvoiceItemType type) {
        this.type = type;
    }
}
