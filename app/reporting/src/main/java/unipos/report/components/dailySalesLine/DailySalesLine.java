package unipos.report.components.dailySalesLine;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;
import unipos.report.components.shared.helper.TaxHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Thomas on 30.01.2016.
 */
@Document(collection = "dailySalesLines")
@Data
public class DailySalesLine {

    @Id
    private String id;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime date;
    private List<TaxHelper> taxes;
    private BigDecimal sum;
    private String storeGuid;
    private String userId;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime timestamp;

    public DailySalesLine() {

    }

    public DailySalesLine(LocalDate date, List<TaxHelper> taxes, BigDecimal sum, String storeGuid, String userId) {
        this.date = date.atTime(12,0);
        this.taxes = taxes;
        this.sum = sum;
        this.storeGuid = storeGuid;
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
    }


}
