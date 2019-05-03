package unipos.report.components.dailySalesLine;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 04.02.2016.
 */
@Service
public class DailySalesLineServiceImpl implements DailySalesLineService {
    @Autowired
    DailySalesLineRepository dailySalesLineRepository;

    @Override
    public List<DailySalesLine> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return dailySalesLineRepository.findByDateBetween(startDate, endDate.plusDays(1L));
    }

    @Override
    public List<DailySalesLine> findByStoreGuidAndDateBetween(String storeGuid, LocalDate startDate, LocalDate endDate) {
        List<DailySalesLine> dailySalesLines = dailySalesLineRepository.findByStoreGuidAndDateBetween(storeGuid, startDate, endDate.plusDays(1L));
        dailySalesLines.sort((d1, d2) -> d1.getDate().compareTo(d2.getDate()));
        return dailySalesLines;
    }
}
