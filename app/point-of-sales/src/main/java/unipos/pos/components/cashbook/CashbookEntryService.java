package unipos.pos.components.cashbook;

import unipos.common.remote.auth.model.User;
import unipos.common.remote.data.model.company.Store;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Dominik on 16.01.2016.
 */
public interface CashbookEntryService {

    List<CashbookEntry> findAll();

    CashbookEntry findByGuid(String guid);

    void addCashbookEntry(CashbookEntry cashbookEntry, Store store, User user);

    void updateCashbookEntry(CashbookEntry cashbookEntry);

    void deleteByGuid(String guid);

    BigDecimal getCurrentCashStatus(Store store);

    List<CashbookEntry> findAllByStore(Store store);

    CashbookEntry getAdjustmentCashbookEntry(Store store);

    CashbookEntry adjustCashStatus(Store store, String userGuid);

    List<CashbookEntry> findAllByStoreSinceLastDailySettlement(Store store);
}
