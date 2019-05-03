package unipos.report.components.dailySettlement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.report.components.categoryReportDay.CategoryReportDay;
import unipos.report.components.categoryReportDay.CategoryReportDayRepository;
import unipos.report.components.dailySalesLine.DailySalesLine;
import unipos.report.components.dailySalesLine.DailySalesLineRepository;
import unipos.report.components.financialReportDay.FinancialReportDay;
import unipos.report.components.financialReportDay.FinancialReportDayRepository;
import unipos.report.components.productReportDay.ProductReportDay;
import unipos.report.components.shared.helper.DailySalesHelper;
import unipos.report.components.productReportDay.ProductReportDayRepository;
import unipos.report.components.shared.helper.ProductReportHelper;
import unipos.report.components.shared.helper.TaxHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by Thomas on 04.02.2016.
 */
@Service
public class DailySettlementReportServiceImpl implements DailySettlementReportService {

    @Autowired
    ProductReportDayRepository productReportDayRepository;
    @Autowired
    DailySalesLineRepository dailySalesLineRepository;
    @Autowired
    FinancialReportDayRepository financialReportDayRepository;
    @Autowired
    CategoryReportDayRepository categoryReportDayRepository;

    @Override
    public void addProductReportDay(List<ProductReportHelper> productReportHelpers, LocalDate date, String storeGuid, String userId) {

        ProductReportDay productReportDay = new ProductReportDay(date, productReportHelpers, productReportHelpers.stream().map(ProductReportHelper::getTurnover).reduce(new BigDecimal("0.00"), BigDecimal::add), storeGuid, userId);
        productReportDayRepository.save(productReportDay);
    }

    @Override
    public void addDailySalesLine(DailySalesHelper dailySales, LocalDate date, String storeGuid, String userId) {

//        dailySalesLine.setDate(dailySales.getDate().withHour(0).withMinute(0).withSecond(0).withNano(0));
        List<TaxHelper> taxList = new ArrayList<>();
        taxList.add(TaxHelper.builder().taxRate(0).netto(dailySales.getNet0()).build());
        taxList.add(TaxHelper.builder().taxRate(10).netto(dailySales.getNet10()).mwst(dailySales.getMwst10()).build());
        taxList.add(TaxHelper.builder().taxRate(13).netto(dailySales.getNet13()).mwst(dailySales.getMwst13()).build());
        taxList.add(TaxHelper.builder().taxRate(20).netto(dailySales.getNet20()).mwst(dailySales.getMwst20()).build());
        DailySalesLine dailySalesLine = new DailySalesLine(date, taxList, dailySales.getSum(), storeGuid, userId);
        dailySalesLineRepository.save(dailySalesLine);
    }

    @Override
    public void addFinancialReportDay(FinancialReportDay financialReportDay, LocalDate date, String storeGuid, String userId) {
        financialReportDay.setDate(date.atTime(12,0));
        financialReportDay.setUserId(userId);
        financialReportDay.setStoreGuid(storeGuid);
        financialReportDay.setTimestamp(LocalDateTime.now());
        financialReportDayRepository.save(financialReportDay);
    }

    @Override
    public void addCategoryReportDay(CategoryReportDay categoryReportDay, LocalDate date, String storeGuid, String userId) {
        categoryReportDay.setDate(date.atTime(12,0));
        categoryReportDay.setStoreGuid(storeGuid);
        categoryReportDay.setUserId(userId);
        categoryReportDay.setTimestamp(LocalDateTime.now());
        categoryReportDayRepository.save(categoryReportDay);
    }

    @Override
    public void addEmptyReport(LocalDate date, String storeGuid, String userId) {
        clearReportLinesAndDays(date, storeGuid);
        productReportDayRepository.save(new ProductReportDay(date, null, new BigDecimal("0.00"), storeGuid, userId));
        dailySalesLineRepository.save(new DailySalesLine(date, new ArrayList<>(), new BigDecimal("0.00"), storeGuid, userId));
        financialReportDayRepository.save(FinancialReportDay.builder().date(date.atTime(12,0)).sumOfSales(new BigDecimal("0.00")).dailySales(new BigDecimal("0.00")).paymentsList(new ArrayList<>()).taxList(new ArrayList<>()).storeGuid(storeGuid).userId(userId).timestamp(LocalDateTime.now()).build());
        categoryReportDayRepository.save(CategoryReportDay.builder().date(date.atTime(12,0)).storeGuid(storeGuid).userId(userId).turnoverTaxes(new ArrayList<>()).categories(new ArrayList<>()).timestamp(LocalDateTime.now()).build());
    }

    @Override
    public void clearReportLinesAndDays(LocalDate date, String storeGuid) {
        List<ProductReportDay> productReportDay = productReportDayRepository.findByStoreGuidAndDateBetween(storeGuid, date, date.plusDays(1));
        if (productReportDay != null && productReportDay.size() > 0) {
            productReportDayRepository.delete(productReportDay.get(0));
        }

        List<DailySalesLine> dailySalesLine = dailySalesLineRepository.findByStoreGuidAndDateBetween(storeGuid, date, date.plusDays(1));
        if (dailySalesLine != null && dailySalesLine.size() > 0) {
            dailySalesLineRepository.delete(dailySalesLine.get(0));
        }

        List<CategoryReportDay> categoryReportDay = categoryReportDayRepository.findByStoreGuidAndDateBetween(storeGuid, date, date.plusDays(1));
        if (categoryReportDay != null && categoryReportDay.size() > 0) {
            categoryReportDayRepository.delete(categoryReportDay.get(0));
        }

        List<FinancialReportDay> financialReportDay = financialReportDayRepository.findByStoreGuidAndDateBetween(storeGuid, date, date.plusDays(1));
        if (financialReportDay != null && financialReportDay.size() > 0) {
            financialReportDayRepository.delete(financialReportDay.get(0));
        }
    }
}
