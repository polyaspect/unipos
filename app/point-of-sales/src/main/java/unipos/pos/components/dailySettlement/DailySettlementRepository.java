package unipos.pos.components.dailySettlement;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Dominik on 19.01.2016.
 */
public interface DailySettlementRepository extends MongoRepository<DailySettlement, String> {
    DailySettlement findByGuid(String guid);

    DailySettlement findLastByStoreGuid(String guid, Sort sort);

    DailySettlement findLastByStoreGuidOrderByDateTimeDesc(String storeGuid);

    DailySettlement findLastByStoreGuidAndDeviceIdOrderByDateTimeDesc(String guid, String deviceId);

    List<DailySettlement> findByDateTimeBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<DailySettlement> findByStoreGuid(String guid);

    List<DailySettlement> findByStoreGuidAndDateTimeBetween(String storeGuid, LocalDateTime startDate, LocalDateTime endDate);
}
