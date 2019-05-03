package unipos.report.components.financialReportDay;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 13.02.2016.
 */
public interface FinancialReportDayService {


    FinancialReportDay findFinancialReportDayByDateAndStoreGuid(LocalDate date, String storeGuid);

    List<FinancialReportDay> findFinancialReportDayByDateBetweenAndStoreGuid(LocalDate startDate, LocalDate endDate, String storeGuid);
}
