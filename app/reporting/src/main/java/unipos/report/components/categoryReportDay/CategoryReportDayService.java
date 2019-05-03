package unipos.report.components.categoryReportDay;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 20.02.2016.
 */
public interface CategoryReportDayService {

    List<CategoryReportDay> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<CategoryReportDay> findByDateBetweenAndStoreGuid(LocalDate startDate, LocalDate endDate, String storeGuid);
}
