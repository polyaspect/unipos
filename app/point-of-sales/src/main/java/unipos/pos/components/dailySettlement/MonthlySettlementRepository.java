package unipos.pos.components.dailySettlement;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Dominik on 19.01.2016.
 */
public interface MonthlySettlementRepository extends MongoRepository<MonthlySettlement, String> {
    MonthlySettlement findByGuid(String guid);

    MonthlySettlement findLastByStoreGuid(String guid, Sort sort);

    MonthlySettlement findLastByStoreGuidOrderByDateTimeDesc(String storeGuid);

    MonthlySettlement findLastByStoreGuidAndDeviceIdOrderByDateTimeDesc(String guid, String deviceId);

    List<MonthlySettlement> findByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<MonthlySettlement> findByStoreGuid(String guid);

    List<MonthlySettlement> findByStoreGuidAndDateTimeBetween(String storeGuid, LocalDateTime startDate, LocalDateTime endDate);
}
