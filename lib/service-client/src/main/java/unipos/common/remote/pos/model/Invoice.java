package unipos.common.remote.pos.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;
import unipos.common.remote.data.model.PaymentMethod;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.signature.model.SignatureTurnoverDetail;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dominik on 03.09.15.
 */
@Data
@Builder
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "invoiceType")
@JsonSubTypes(value = {
        @JsonSubTypes.Type(value = Invoice.class, name = "invoice"),
        @JsonSubTypes.Type(value = ReversalInvoice.class, name = "reversalInvoice")
})
public class Invoice {
    private String id;
    //laufende Rechnungsnummer
    private Long invoiceId;
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
    private String orderId;
    private BigDecimal turnoverNet;
    private BigDecimal turnoverGross;
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    private LocalDateTime creationDate;
    private Cashier cashier;
    private Company company;
    private Store store;
    private String hash;
    private String deviceId;
    private boolean reverted;
    private String guid;
    private String customerGuid;
    protected InvoiceType invoiceType;
    private SignatureInvoiceType signatureInvoiceType;
    private Long umsatzZaehler;
    private String qrCode;
    private List<InvoiceTag> invoiceTags;


    public Invoice() {
        invoiceType = InvoiceType.invoice;
    }

    public Invoice(String id, Long invoiceId, List<InvoiceItem> invoiceItems, String orderId, BigDecimal turnoverNet, BigDecimal turnoverGross, LocalDateTime creationDate, Cashier cashier, Company company, Store store, String hash, String deviceId, boolean reverted, String guid, String customerGuid, InvoiceType invoiceType, SignatureInvoiceType signatureInvoiceType, Long umsatzZaehler, String qrCode, List<InvoiceTag> invoiceTags) {
        this.id = id;
        this.invoiceId = invoiceId;
        this.invoiceItems = invoiceItems;
        this.orderId = orderId;
        this.turnoverNet = turnoverNet;
        this.turnoverGross = turnoverGross;
        this.creationDate = creationDate;
        this.cashier = cashier;
        this.company = company;
        this.store = store;
        this.hash = hash;
        this.deviceId = deviceId;
        this.reverted = reverted;
        this.guid = guid;
        this.customerGuid = customerGuid;
        this.invoiceType = InvoiceType.invoice;
        this.signatureInvoiceType = signatureInvoiceType;
        this.umsatzZaehler = umsatzZaehler;
        this.qrCode = qrCode;
        this.invoiceTags = invoiceTags;
    }

    @Override
    public String toString() {
        return "Invoice{" +
                "id='" + id + '\'' +
                ", invoiceId=" + invoiceId +
                ", invoiceItems=" + invoiceItems +
                ", orderId='" + orderId + '\'' +
                ", turnoverNet=" + turnoverNet +
                ", turnoverGross=" + turnoverGross +
                ", creationDate=" + creationDate +
                ", cashier=" + cashier +
                ", company=" + company +
                ", store=" + store +
                ", hash='" + hash + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", reverted=" + reverted +
                ", guid='" + guid + '\'' +
                ", customerGuid='" + customerGuid + '\'' +
                ", invoiceType=" + invoiceType +
                ", signatureInvoiceType=" + signatureInvoiceType +
                ", umsatzZaehler=" + umsatzZaehler +
                ", qrCode='" + qrCode + '\'' +
                '}';
    }

    @JsonIgnore
    public BigDecimal getTotalAmountPaid() {
        BigDecimal money = new BigDecimal("0.0");
        for(InvoiceItem invoiceItem : invoiceItems) {
            if(invoiceItem instanceof PaymentInvoiceItem) {
                money = money.add(((PaymentInvoiceItem) invoiceItem).getTurnover());
            }
            if(invoiceItem instanceof ChangeInvoiceItem) {
                money = money.add(((ChangeInvoiceItem) invoiceItem).getTurnover());
            }
        }
        return money;
    }

    /**
     * Berechnet den Wert der Rechnung ohne Abzügen von Rabatten, Gutscheinen, etc.
     * @return
     */
    @JsonIgnore
    public BigDecimal getProductValue() {
        return invoiceItems.stream()
                .filter(x -> x instanceof ProductInvoiceItem)
                .map(x -> (ProductInvoiceItem)x)
                .map(ProductInvoiceItem::getTurnoverGross)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @JsonIgnore
    public BigDecimal getDiscountValue() {
        return invoiceItems.stream()
                .filter(x -> x instanceof ProductInvoiceItem)
                .map(x -> (ProductInvoiceItem)x)
                .flatMap(x -> x.getDiscounts().stream())
                .map(Discount::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @JsonIgnore
    public boolean containsBarumsatzPayment(List<PaymentMethod> paymentMethods) {
        return paymentMethods == null || paymentMethods.size() == 0 || invoiceItems.stream()
                .filter(x -> x instanceof PaymentInvoiceItem)
                .map(x -> (PaymentInvoiceItem)x)
                .map(x ->
                        paymentMethods.stream()
                                .filter( y -> y.getGuid().equals(x.getPaymentMethodGuid()))
                                .findFirst()
                                .orElse(PaymentMethod.builder().type(PaymentMethod.Type.BAR).build()))
                .anyMatch(x -> x.getType().isBarumsatz());
    }

    @JsonIgnore
    public BigDecimal getTaxAmount(int taxRate) {
        BigDecimal amount = new BigDecimal("0.00");
        for(InvoiceItem invoiceItem : invoiceItems) {
            if(invoiceItem instanceof ProductInvoiceItem) {
                ProductInvoiceItem productInvoiceItem = (ProductInvoiceItem)invoiceItem;
                amount = productInvoiceItem.getTaxRate() == taxRate ? amount.add(productInvoiceItem.getTax()) : amount;
            }
            if(invoiceItem instanceof ReversalInvoiceItem) {
                ReversalInvoiceItem reversalInvoiceItem = (ReversalInvoiceItem) invoiceItem;
                for(InvoiceItem invoiceItem1 : invoiceItems) {
                    if(invoiceItem1 instanceof ProductInvoiceItem) {
                        ProductInvoiceItem p = (ProductInvoiceItem)invoiceItem1;
                        if(p.getOrderItemId().equals(reversalInvoiceItem.getReceiverOrderItem()) && p.getTaxRate() == taxRate) {
                            amount = amount.subtract(p.getTax());
                        }
                    }
                }
            }
        }
        return amount;
    }

    @JsonIgnore
    public List<Tax> getInvoicesTaxStuff() {
        List<Tax> taxes = invoiceItems.stream().filter(x -> x instanceof TaxInvoiceItem).map(x -> (TaxInvoiceItem)x).map(x -> {
            Tax tax = new Tax();
            tax.setTaxRate(x.getTaxRate());
            tax.setTurnoverGross(x.getAmountGross());
            tax.setTurnoverNet(x.getAmountNet());
            tax.setTaxAmount(x.getAmountTax());
            return tax;
        }).collect(Collectors.toList());
        return taxes;
    }

    public List<SignatureTurnoverDetail> getSingatureTurnoverDetails() {
        List<ProductInvoiceItem> productInvoiceItems = invoiceItems.stream()
                .filter(x -> x instanceof ProductInvoiceItem)
                .map(x -> (ProductInvoiceItem)x)
                .filter(x -> !x.isReversalApplied())
                .collect(Collectors.toList());

        List<SignatureTurnoverDetail> signatureTurnoverDetails = new ArrayList<>();

        for (ProductInvoiceItem productInvoiceItem : productInvoiceItems) {
            SignatureTurnoverDetail signatureTurnoverDetail = signatureTurnoverDetails.stream()
                    .filter(x -> x.getTaxRate() == productInvoiceItem.getTaxRate())
                    .findFirst()
                    .orElseGet(() -> SignatureTurnoverDetail.builder()
                            .discount(BigDecimal.ZERO)
                            .taxRate(productInvoiceItem.getTaxRate())
                            .turnoverGross(BigDecimal.ZERO)
                            .turnoverGrossIncludingReducingDiscounts(BigDecimal.ZERO)
                            .build());

            signatureTurnoverDetail.setDiscount(signatureTurnoverDetail.getDiscount().add(productInvoiceItem.getDiscounts().stream().map(Discount::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add)));
            signatureTurnoverDetail.setTurnoverGross(signatureTurnoverDetail.getTurnoverGross().add(productInvoiceItem.getPrice()));
            signatureTurnoverDetail.setTurnoverGrossIncludingReducingDiscounts(signatureTurnoverDetail.getTurnoverGrossIncludingReducingDiscounts().add(productInvoiceItem.getTurnoverGross()));

            if (signatureTurnoverDetails.stream().noneMatch(x -> x.getTaxRate() == signatureTurnoverDetail.getTaxRate())) {
                signatureTurnoverDetails.add(signatureTurnoverDetail);
            }
        }

        return signatureTurnoverDetails;
    }

    public enum InvoiceType {
        invoice,
        reversalInvoice
    }

    public enum SignatureInvoiceType {
        STANDARD(false, "Standardbeleg"),
        STORNO(false, "Stornobeleg"),
        TRAINING(false, "Trainingsbeleg"),
        NULL(false, "Nullbeleg"),
        START(true, "Startbeleg"),
        SAMMEL(true, "Sammelbeleg"),
        SCHLUSS(false, "Schlussbeleg"),
        MONATS(false, "Monatsbeleg"),
        JAHRES(true, "Jahresbeleg");

        boolean signatureRequired;
        String name;

        SignatureInvoiceType(boolean signatureRequired, String name) {
            this.signatureRequired = signatureRequired;
            this.name = name;
        }

        public boolean isSignatureRequired() {
            return signatureRequired;
        }

        public String getName() {
            return name;
        }
    }
}
