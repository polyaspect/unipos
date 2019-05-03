package unipos.report.components.dailySettlement;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import unipos.report.components.categoryReportDay.CategoryReportDayRepository;
import unipos.report.components.dailySalesLine.DailySalesLineRepository;
import unipos.report.components.financialReportDay.FinancialReportDayRepository;
import unipos.report.components.productReportDay.ProductReportDayRepository;
import unipos.report.shared.AbstractServiceTest;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

/**
 * Created by Thomas on 26.09.2016.
 */
public class DailySettlementReportServiceTest extends AbstractServiceTest {

    @InjectMocks
    DailySettlementReportService dailySettlementReportService = new DailySettlementReportServiceImpl();
    @Mock
    ProductReportDayRepository productReportDayRepository;
    @Mock
    DailySalesLineRepository dailySalesLineRepository;
    @Mock
    CategoryReportDayRepository categoryReportDayRepository;
    @Mock
    FinancialReportDayRepository financialReportDayRepository;

    @Test
    public void testClearReportLinesAndDaysWithoutDatasets() throws Exception {
        when(productReportDayRepository.findByStoreGuidAndDateBetween(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(
                Arrays.asList()
        );
        when(dailySalesLineRepository.findByStoreGuidAndDateBetween(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(
                Arrays.asList()
        );
        when(categoryReportDayRepository.findByStoreGuidAndDateBetween(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(
                Arrays.asList()
        );
        when(financialReportDayRepository.findByStoreGuidAndDateBetween(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(
                Arrays.asList()
        );

        dailySettlementReportService.clearReportLinesAndDays(LocalDate.now(), "asdf");
    }

    @Test
    public void testClearReportLinesAndDaysWithoutNullValues() throws Exception {
        when(productReportDayRepository.findByStoreGuidAndDateBetween(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(
                null
        );
        when(dailySalesLineRepository.findByStoreGuidAndDateBetween(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(
                null
        );
        when(categoryReportDayRepository.findByStoreGuidAndDateBetween(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(
                null
        );
        when(financialReportDayRepository.findByStoreGuidAndDateBetween(any(String.class), any(LocalDate.class), any(LocalDate.class))).thenReturn(
                null
        );

        dailySettlementReportService.clearReportLinesAndDays(LocalDate.now(), "asdf");
    }

}