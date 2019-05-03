package unipos.report.components.categoryReportDay;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 20.02.2016.
 */
public interface CategoryReportDayRepository extends MongoRepository<CategoryReportDay, String> {

    List<CategoryReportDay> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<CategoryReportDay> findByStoreGuidAndDateBetween(String storeGuid, LocalDate startDate, LocalDate endDate);
}
