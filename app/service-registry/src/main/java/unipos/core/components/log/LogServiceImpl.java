package unipos.core.components.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import unipos.core.components.modules.ModuleRepository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @author ggradnig
 */
@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private LogRepository logRepository;

    @Override
    public void addLog(Log log) {
        logRepository.save(log);
    }

    @Override
    public List<Log> showAllLogs() {
        return logRepository.findAll();
    }

    @Override
    public List<Log> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        return logRepository.findByDateTimeBetween(startDate, endDate, pageable).getContent();
    }
}
