package unipos.report.components.categoryReportDay;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import unipos.common.container.Serialization.LocalDateDeserializer;
import unipos.common.container.Serialization.LocalDateSerializer;
import unipos.report.components.shared.helper.CategoryReportHelper;
import unipos.report.components.shared.helper.TaxHelper;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Thomas on 19.02.2016.
 */
@Data
@Builder
@Document(collection = "categoryReportDays")
public class CategoryReportDay {

    @Id
    private String id;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime date;
    private List<CategoryReportHelper> categories;
    private List<TaxHelper> turnoverTaxes;
    private String storeGuid;
    private String userId;
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonDeserialize(using = LocalDateDeserializer.class)
    private LocalDateTime timestamp;
}
