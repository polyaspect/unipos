package unipos.report.components.productReportDay;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 04.02.2016.
 */
public interface ProductReportDayService {

    List<ProductReportDay> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<ProductReportDay> findByDateBetweenAndStoreGuid(LocalDate startDate, LocalDate endDate, String storeGuid);
}
