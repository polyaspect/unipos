package unipos.signature.components.umsatzZaehler;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;
import unipos.common.remote.data.model.PaymentMethod;
import unipos.common.remote.pos.model.Invoice;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by domin on 15.12.2016.
 */
@Document(collection = "umsatzZaehler")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UmsatzZaehler {

    @Id
    private String id;
    private Long autoIncrement;
    private BigDecimal umsatzZaehlerBefore;
    private BigDecimal umsatz;
    /**
     * This field represents the latest umsatzZaehler-Line
     */
    private BigDecimal umsatzZaehler;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime creationDateTime;
    private String storeGuid;
    private String guid;
    private Long invoiceId;

    public static UmsatzZaehler getFromInvoice(Invoice invoice, List<PaymentMethod> paymentMethods) {
        BigDecimal umsatz = null;
        if (invoice.containsBarumsatzPayment(paymentMethods)) {
            umsatz = invoice.getTurnoverGross();
        } else {
            umsatz = BigDecimal.ZERO;
        }

        return UmsatzZaehler.builder()
                .umsatz(umsatz)
                .storeGuid(invoice.getStore().getGuid())
                .creationDateTime(LocalDateTime.now())
                .invoiceId(invoice.getInvoiceId())
                .guid(UUID.randomUUID().toString())
                .build();
    }
}
