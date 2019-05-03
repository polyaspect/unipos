package unipos.report.components.categoryReportDay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 20.02.2016.
 */
@Service
public class CategoryReportDayServiceImpl implements CategoryReportDayService {

    @Autowired
    CategoryReportDayRepository categoryReportDayRepository;

    @Override
    public List<CategoryReportDay> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return categoryReportDayRepository.findByDateBetween(startDate, endDate.plusDays(1L));
    }

    @Override
    public List<CategoryReportDay> findByDateBetweenAndStoreGuid(LocalDate startDate, LocalDate endDate, String storeGuid) {
        return categoryReportDayRepository.findByStoreGuidAndDateBetween(storeGuid, startDate, endDate.plusDays(1L));
    }
}
