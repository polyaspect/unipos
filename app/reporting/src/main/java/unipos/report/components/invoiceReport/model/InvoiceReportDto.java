package unipos.report.components.invoiceReport.model;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.model.Cashier;
import unipos.common.remote.pos.model.InvoiceItem;
import unipos.common.remote.pos.model.ProductInvoiceItem;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 15.06.2016.
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class InvoiceReportDto {

    @Id
    private String id;
    //laufende Rechnungsnummer
    private Long invoiceId;
    private List<InvoiceItem> invoiceItems = new ArrayList<>();
    private List<ProductInvoiceItem> invoiceItemsEssen = new ArrayList<>();
    private List<ProductInvoiceItem> invoiceItemsGetraenke = new ArrayList<>();
    private List<ProductInvoiceItem> invoiceItemsHendl = new ArrayList<>();
    private List<ProductInvoiceItem> invoiceItemsCafe = new ArrayList<>();
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
    private String customerGuid;
}
