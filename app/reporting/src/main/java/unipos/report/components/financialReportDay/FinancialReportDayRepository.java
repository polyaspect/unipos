package unipos.report.components.financialReportDay;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 13.02.2016.
 */
public interface FinancialReportDayRepository extends MongoRepository<FinancialReportDay, String> {

    FinancialReportDay findFinancialReportByDate(LocalDate date);
    List<FinancialReportDay> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<FinancialReportDay> findByStoreGuidAndDateBetween(String storeGuid, LocalDate startDate, LocalDate endDate);
}
