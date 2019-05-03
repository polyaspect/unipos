package unipos.report.components.dailySalesLine;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 04.02.2016.
 */
public interface DailySalesLineRepository extends MongoRepository<DailySalesLine, String> {

    List<DailySalesLine> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<DailySalesLine> findByStoreGuidAndDateBetween(String storeGuid, LocalDate startDate, LocalDate endDate);

}
