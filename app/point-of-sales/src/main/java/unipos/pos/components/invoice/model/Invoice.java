package unipos.pos.components.invoice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.remote.data.model.PaymentMethod;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.model.*;
import unipos.common.remote.sync.model.Syncable;
import unipos.pos.components.order.Cashier;
import unipos.pos.components.shared.HashGeneratorUtils;
import unipos.pos.components.shared.LocalDateDeserializer;
import unipos.pos.components.shared.LocalDateSerializer;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

/**
 * Created by dominik on 03.09.15.
 */
@Document(collection = "invoices")
@Data
@Builder
public class Invoice implements Syncable {
    @Id
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
    private String deviceId; //Device that created the Invoice
    private boolean reverted;
    private String guid;
    protected InvoiceType invoiceType;
    private SignatureInvoiceType signatureInvoiceType;
    private Long umsatzZaehler;
    private String qrCode;
    private List<InvoiceTag> invoiceTags;

    public Invoice() {
        invoiceType = InvoiceType.invoice;
    }

    public Invoice(String id, Long invoiceId, List<InvoiceItem> invoiceItems, String orderId, BigDecimal turnoverNet, BigDecimal turnoverGross, LocalDateTime creationDate, Cashier cashier, Company company, Store store, String hash, String deviceId, boolean reverted, String guid, InvoiceType invoiceType, SignatureInvoiceType signatureInvoiceType, Long umsatzZaehler, String qrCode, List<InvoiceTag> invoiceTags) {
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
        this.invoiceType = InvoiceType.invoice;
        this.invoiceTags = invoiceTags;
        this.signatureInvoiceType = signatureInvoiceType;
        this.umsatzZaehler = umsatzZaehler;
        this.qrCode = qrCode;
    }

    public void generateHash() {
        this.hash = HashGeneratorUtils.generateMD5(toString());
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
                ", invoiceType=" + invoiceType +
                ", signatureInvoiceType=" + signatureInvoiceType +
                ", umsatzZaehler=" + umsatzZaehler +
                ", qrCode='" + qrCode + '\'' +
                '}';
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(invoiceId).append(store.getGuid()).append(creationDate).toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Invoice))
            return false;
        if (obj == this)
            return true;

        Invoice invoice = (Invoice) obj;
        return new EqualsBuilder().append(invoiceId, invoice.getInvoiceId()).append(store.getGuid(), invoice.getStore().getGuid()).append(creationDate, invoice.getCreationDate()).isEquals();
    }

    public void calcInvoice() {
        invoiceItems = invoiceItems.stream().sorted((x,y) -> x.getPosition() - y.getPosition()).collect(Collectors.toList());

        List<ProductInvoiceItem> productInvoiceItems = getInvoiceItems().stream().filter(x -> x instanceof ProductInvoiceItem).map(x -> (ProductInvoiceItem) x).sorted((x, y) -> x.getPosition() - y.getPosition()).collect(toList());
        List<ReversalInvoiceItem> reversalInvoiceItems = getInvoiceItems().stream().filter(x -> x instanceof ReversalInvoiceItem).map(x -> (ReversalInvoiceItem) x).collect(toList());
        List<OrderDiscountInvoiceItem> orderDiscountInvoiceItems = getInvoiceItems().stream().filter(x -> x instanceof OrderDiscountInvoiceItem).map(x -> (OrderDiscountInvoiceItem) x).collect(toList());
        List<OrderItemDiscountInvoiceItem> orderItemDiscountInvoiceItems = getInvoiceItems().stream().filter(x -> x instanceof OrderItemDiscountInvoiceItem).map(x -> (OrderItemDiscountInvoiceItem) x).collect(toList());

        productInvoiceItems.forEach(ProductInvoiceItem::calcTaxAndGross);

        //Get the Invoice sums
        BigDecimal grossSum = productInvoiceItems.stream().map(ProductInvoiceItem::getTurnoverGross).reduce(new BigDecimal("0.00"), BigDecimal::add);
        BigDecimal nettoSum = productInvoiceItems.stream().map(ProductInvoiceItem::getTurnoverNet).reduce(new BigDecimal("0.00"), BigDecimal::add);


        //Now apply the reversals
        reversalInvoiceItems.stream().forEach(reversalInvoiceItem -> invoiceItems.stream().forEach(invoiceItem -> {
            if (invoiceItem.getOrderItemId().equals(reversalInvoiceItem.getReceiverOrderItem())) {
                invoiceItem.setReversalApplied(true);
            }
        }));

        //Apply productDiscounts
        orderItemDiscountInvoiceItems.stream().forEach(d -> productInvoiceItems.stream().forEach(p -> p.applyDiscount(d)));

        grossSum = productInvoiceItems.stream().filter(x -> !x.isReversalApplied()).map(ProductInvoiceItem::getTurnoverGross).reduce(BigDecimal.ZERO, BigDecimal::add);
        nettoSum = productInvoiceItems.stream().filter(x -> !x.isReversalApplied()).map(ProductInvoiceItem::getTurnoverNet).reduce(BigDecimal.ZERO, BigDecimal::add);


        setTurnoverNet(nettoSum);
        setTurnoverGross(grossSum);

        //OrderDiscounts are the last ones
        orderDiscountInvoiceItems.forEach(this::applyDiscount);

        //Round everything for on two kommastellen
        setTurnoverNet(getTurnoverNet().setScale(2, RoundingMode.HALF_UP));
        setTurnoverGross(getTurnoverGross().setScale(2, RoundingMode.HALF_UP));

        //After that add the appropriate TaxInvoiceItems for the Invoice
        createTaxInvoiceItems();
    }

    private void createTaxInvoiceItems() {

        invoiceItems = new ArrayList<>(invoiceItems);

        List<ProductInvoiceItem> productInvoiceItems = invoiceItems.stream().filter(x -> x instanceof ProductInvoiceItem).map(x -> (ProductInvoiceItem) x).collect(Collectors.toList());
        List<ReversalInvoiceItem> reversalInvoiceItems = invoiceItems.stream().filter(x -> x instanceof ReversalInvoiceItem).map(x -> (ReversalInvoiceItem)x).collect(Collectors.toList());

        // I need to map the tax data to a new TaxInvoiceItem

        List<TaxInvoiceItem> taxInvoiceItems = productInvoiceItems.stream().map(ProductInvoiceItem::getTaxRate).distinct().sorted((x,y) -> x.compareTo(y)).map(taxRate -> {
            TaxInvoiceItem taxInvoiceItem = new TaxInvoiceItem();
            taxInvoiceItem.setTaxRate(taxRate);

            List<ProductInvoiceItem> items = productInvoiceItems.stream().filter(x -> x.getTaxRate() == taxRate).collect(toList());
            taxInvoiceItem.setAmountGross(items.stream().map(ProductInvoiceItem::getTurnoverGross).reduce(BigDecimal.ZERO, BigDecimal::add));
            taxInvoiceItem.calcTaxAndGross();
            return taxInvoiceItem;
        }).collect(toList());

        reversalInvoiceItems.stream().forEach(r -> {
            productInvoiceItems.stream().filter(p -> p.getOrderItemId().equals(r.getReceiverOrderItem())).forEach(p -> {
                taxInvoiceItems.stream().filter(t -> t.getTaxRate() == p.getTaxRate()).findFirst().ifPresent(t -> {
                    t.setAmountGross(t.getAmountGross().subtract(p.getTurnoverGross()));
                    t.calcTaxAndGross();
                });
            });
        });

        for(TaxInvoiceItem taxInvoiceItem : taxInvoiceItems.stream().filter(x -> !x.getAmountGross().equals(new BigDecimal("0.00"))).collect(toList())) {
            taxInvoiceItem.setPosition(invoiceItems.size()+1);
            invoiceItems.add(taxInvoiceItem);
        }

//        for (TaxInvoiceItem taxInvoiceItem : taxInvoiceItems) {
//            taxInvoiceItem.setPosition(invoiceItems.size()+1);
//            invoiceItems.add(taxInvoiceItem);
//        }

//        for (int i = 0; i < taxInvoiceItems.size(); i++) {
//            taxInvoiceItems.get(i).setPosition(invoiceItems.size() + 1);
//            invoiceItems.add(taxInvoiceItems.get(i));
//        }
    }

    public void applyDiscount(OrderDiscountInvoiceItem orderDiscountInvoiceItem) {

        if(orderDiscountInvoiceItem.isReversalApplied()) {
            return;
        }
        BigDecimal sumGross = turnoverGross;
        BigDecimal sumGrossReduced = turnoverGross.subtract(orderDiscountInvoiceItem.getDiscount());
        BigDecimal discountTotal = orderDiscountInvoiceItem.getDiscount();

        List<ProductInvoiceItem> productInvoiceItems = invoiceItems.stream()
                .filter(x -> x instanceof ProductInvoiceItem)
                .map(x -> (ProductInvoiceItem) x)
                .filter(x -> !x.isReversalApplied()).collect(Collectors.toList());

        BigDecimal sumNetto = productInvoiceItems.stream().map(x -> {
            BigDecimal grossProductPrice = x.getTurnoverGross().subtract(x.getTurnoverGross().divide(sumGross, 9, RoundingMode.HALF_UP).multiply(discountTotal)).setScale(2, RoundingMode.HALF_UP);
            x.getDiscounts().add(Discount.builder()
                    .label(orderDiscountInvoiceItem.getLabel())
                    .discountId(orderDiscountInvoiceItem.getDiscountId())
                    .orderItemId(orderDiscountInvoiceItem.getOrderItemId())
                    .amount(x.getTurnoverGross().subtract(grossProductPrice))
                    .type(Discount.Type.INVOICE)
                    .build());
            x.setTurnoverGross(grossProductPrice);
            x.calcTaxAndGross();
            return x.getTurnoverNet();
        }).reduce(new BigDecimal("0.00"), BigDecimal::add);

        BigDecimal reducedAmount = productInvoiceItems.stream().flatMap(productInvoiceItem -> productInvoiceItem.getDiscounts().stream().filter(pd -> pd.getType() == Discount.Type.INVOICE && pd.getOrderItemId().equals(orderDiscountInvoiceItem.getOrderItemId())).map(Discount::getAmount)).reduce(new BigDecimal("0.00"), BigDecimal::add);
        if (!reducedAmount.equals(orderDiscountInvoiceItem.getDiscount())) {
            productInvoiceItems.stream().max((x, y) -> x.getTurnoverGross().compareTo(y.getTurnoverGross())).ifPresent(x -> {
                x.setTurnoverGross(x.getTurnoverGross().subtract(orderDiscountInvoiceItem.getDiscount().subtract(reducedAmount)));
                x.calcTaxAndGross();
                x.getDiscounts().stream()
                        .filter(d -> d.getOrderItemId().equals(orderDiscountInvoiceItem.getOrderItemId())).findFirst().ifPresent(d -> d.setAmount(d.getAmount().add(orderDiscountInvoiceItem.getDiscount().subtract(reducedAmount))));
            });
        }

        setTurnoverGross(productInvoiceItems.stream().map(ProductInvoiceItem::getTurnoverGross).reduce(new BigDecimal("0.00"), BigDecimal::add));
        setTurnoverNet(productInvoiceItems.stream().map(ProductInvoiceItem::getTurnoverNet).reduce(new BigDecimal("0.00"), BigDecimal::add));
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
    }
}
