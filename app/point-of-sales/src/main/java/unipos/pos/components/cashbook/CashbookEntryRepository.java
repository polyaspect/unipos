package unipos.pos.components.cashbook;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Dominik on 16.01.2016.
 */
public interface CashbookEntryRepository extends MongoRepository<CashbookEntry, String> {

    Long deleteByGuid(String guid);
    CashbookEntry findByGuid(String guid);

    List<CashbookEntry> findByStoreGuid(String guid);
    List<CashbookEntry> findByStoreGuidAndCreationDateBetween(String guid, LocalDateTime startDate, LocalDateTime endDate);
}
