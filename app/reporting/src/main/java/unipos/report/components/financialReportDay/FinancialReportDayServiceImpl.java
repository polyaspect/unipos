package unipos.report.components.financialReportDay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Thomas on 13.02.2016.
 */
@Service
public class FinancialReportDayServiceImpl implements FinancialReportDayService {

    @Autowired
    FinancialReportDayRepository financialReportDayRepository;

    @Override
    public FinancialReportDay findFinancialReportDayByDateAndStoreGuid(LocalDate date, String storeGuid) {
        return financialReportDayRepository.findByStoreGuidAndDateBetween(storeGuid, date, date.plusDays(1L)).get(0);
    }

    @Override
    public List<FinancialReportDay> findFinancialReportDayByDateBetweenAndStoreGuid(LocalDate startDate, LocalDate endDate, String storeGuid) {
        List<FinancialReportDay> financialReportDays = financialReportDayRepository.findByStoreGuidAndDateBetween(storeGuid, startDate, endDate.plusDays(1L));
        Comparator<FinancialReportDay> sortByDate = Comparator.comparing(FinancialReportDay::getDate);
        financialReportDays.sort(sortByDate);
        return financialReportDays;
    }
}
