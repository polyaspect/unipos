package unipos.core.components.log;

import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.core.components.modules.Module;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author ggradnig
 *         <p>
 *         Spring repository for CRUD operations on Entity
 */

public interface LogRepository extends MongoRepository<Log, String> {
    Page<Log> findAll(Pageable pageable);

    Page<Log> findByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}