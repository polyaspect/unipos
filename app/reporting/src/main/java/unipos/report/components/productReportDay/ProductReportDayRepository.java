package unipos.report.components.productReportDay;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Thomas on 04.02.2016.
 */
public interface ProductReportDayRepository extends MongoRepository<ProductReportDay, String> {

    List<ProductReportDay> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<ProductReportDay> findByStoreGuidAndDateBetween(String storeGuid, LocalDate startDate, LocalDate endDate);
}
