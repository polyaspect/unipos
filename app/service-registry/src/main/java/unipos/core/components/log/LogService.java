package unipos.core.components.log;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author ggradnig
 */
public interface LogService {
    void addLog(Log log);
    List<Log> showAllLogs();

    List<Log> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
