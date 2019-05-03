package unipos.report.components.financialReportDay;

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
import unipos.common.remote.pos.model.PaymentInvoiceItem;
import unipos.report.components.shared.helper.TaxHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Thomas on 13.02.2016.
 */
@Document(collection = "financialReportDays")
@Data
@Builder
@AllArgsConstructor
public class FinancialReportDay {

    @Id
    private String id;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime date;
    private BigDecimal cashStatus;
    private BigDecimal sumOfSales;
    private BigDecimal dailySales;
    private Map<String, BigDecimal> paymentsMap;
    private List<PaymentInvoiceItem> paymentsList;
    private List<TaxHelper> taxList;
    private String storeGuid;
    private String userId;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime timestamp;

    public FinancialReportDay() {
        paymentsList = new ArrayList<>();
        taxList = new ArrayList<>();
        timestamp  = LocalDateTime.now();
    }

}
