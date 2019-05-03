package unipos.report.components.productReportDay;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;
import unipos.report.components.shared.helper.ProductReportHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas on 30.01.2016.
 */
@Document(collection = "productReportDays")
@Data
//@Builder
public class ProductReportDay {

    @Id
    private String id;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime date;
    private List<ProductReportHelper> productReportHelpers;
    private BigDecimal sum;
    private String storeGuid;
    private String userId;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime timestamp;

    public ProductReportDay() {
        date = LocalDate.now().atTime(12,0);
        productReportHelpers = new ArrayList<>();
        sum = new BigDecimal("0.0");
        timestamp = LocalDateTime.now();
    }

    public ProductReportDay(LocalDate date, List<ProductReportHelper> productReportHelpers, BigDecimal sum, String storeGuid, String userId) {
        this.date = date.atTime(12,0);
        this.productReportHelpers = productReportHelpers;
        this.sum = sum;
        this.storeGuid = storeGuid;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }
}
