package unipos.report.components.dailySettlement;

import unipos.report.components.categoryReportDay.CategoryReportDay;
import unipos.report.components.financialReportDay.FinancialReportDay;
import unipos.report.components.shared.helper.DailySalesHelper;
import unipos.report.components.shared.helper.ProductReportHelper;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 04.02.2016.
 */
public interface DailySettlementReportService {

    void addProductReportDay(List<ProductReportHelper> productReportHelpers, LocalDate date, String storeGuid, String userId);

    void addDailySalesLine(DailySalesHelper dailySales, LocalDate date, String storeGuid, String userId);

    void addFinancialReportDay(FinancialReportDay financialReportDay, LocalDate date, String storeGuid, String userId);

    void addCategoryReportDay(CategoryReportDay categoryReportDay, LocalDate date, String storeGuid, String userId);

    void addEmptyReport(LocalDate date, String storeGuid, String userId);

    void clearReportLinesAndDays(LocalDate date, String storeGuid);
}
