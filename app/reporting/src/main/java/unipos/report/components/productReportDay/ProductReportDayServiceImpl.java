package unipos.report.components.productReportDay;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Thomas on 04.02.2016.
 */
@Service
public class ProductReportDayServiceImpl implements ProductReportDayService {
    @Autowired
    ProductReportDayRepository productReportDayRepository;

    @Override
    public List<ProductReportDay> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return productReportDayRepository.findByDateBetween(startDate, endDate.plusDays(1L));
    }

    @Override
    public List<ProductReportDay> findByDateBetweenAndStoreGuid(LocalDate startDate, LocalDate endDate, String storeGuid) {
        return productReportDayRepository.findByStoreGuidAndDateBetween(storeGuid, startDate, endDate.plusDays(1L));
    }
}
