package unipos.pos.components.cashbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;
import unipos.pos.components.dailySettlement.DailySettlementService;
import unipos.pos.components.sequence.SequenceRepository;
import unipos.pos.components.sequence.SequenceTable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 16.01.2016.
 */

@Service
public class CashbookEntryServiceImpl implements CashbookEntryService {

    @Autowired
    CashbookEntryRepository cashbookEntryRepository;
    @Autowired
    SequenceRepository sequenceRepository;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    DailySettlementService dailySettlementService;


    @Override
    public List<CashbookEntry> findAll() {
        return cashbookEntryRepository.findAll();
    }

    @Override
    public void deleteByGuid(String guid) {
        CashbookEntry cashbookEntry = cashbookEntryRepository.findByGuid(guid);
        cashbookEntryRepository.deleteByGuid(guid);

        try {
            syncRemoteInterface.syncChanges(cashbookEntry, Target.CASHBOOK, Action.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public BigDecimal getCurrentCashStatus(Store store) {
        return cashbookEntryRepository.findByStoreGuid(store.getGuid()).parallelStream().map(entry -> {
            switch (entry.getType()) {
                case IN:
                    return entry.getAmount();
                case OUT:
                    return entry.getAmount().negate();
                default:
                    return entry.getAmount();
            }
        }).reduce(new BigDecimal("0.00"), BigDecimal::add);
    }

    @Override
    public List<CashbookEntry> findAllByStore(Store store) {
        return cashbookEntryRepository.findByStoreGuid(store.getGuid());
    }

    @Override
    public CashbookEntry getAdjustmentCashbookEntry(Store store) {
        CashbookEntry.Type type = CashbookEntry.Type.OUT;

        if(store.getPettyCash() != null && store.getPettyCash().compareTo(BigDecimal.valueOf(0)) >= 0){
            BigDecimal outValue = getCurrentCashStatus(store).subtract(store.getPettyCash());

            if(outValue.compareTo(BigDecimal.ZERO) == 0){
                return null;
            }
            else if(outValue.compareTo(BigDecimal.valueOf(0)) == -1){
                type = CashbookEntry.Type.IN;
                outValue = outValue.multiply(BigDecimal.valueOf(-1));
            }

            return CashbookEntry.builder().amount(outValue).description("Tagesabschluss").type(type).build();
        }
        else{
            return null;
        }

    }

    @Override
    public CashbookEntry adjustCashStatus(Store store, String userGuid) {
        CashbookEntry adjustmentEntry = getAdjustmentCashbookEntry(store);

        if(adjustmentEntry != null){
            adjustmentEntry.setUserGuid(userGuid);
            addCashbookEntry(adjustmentEntry, store, null);
        }

        return adjustmentEntry;
    }

    @Override
    public List<CashbookEntry> findAllByStoreSinceLastDailySettlement(Store store) {

        unipos.pos.components.dailySettlement.DailySettlement lastDailySettlement = dailySettlementService.getLastClosedDailySettlementByStore(store.getGuid());
        LocalDateTime startDate = LocalDateTime.parse("1900-01-01T00:00:00");
        if(lastDailySettlement != null ){
            startDate = lastDailySettlement.getDateTime();
        }

        return cashbookEntryRepository.findByStoreGuidAndCreationDateBetween(store.getGuid(), startDate, LocalDateTime.now()).stream().filter(x -> x.getStoreGuid().equals(store.getGuid())).collect(Collectors.toList());
    }

    @Override
    public CashbookEntry findByGuid(String guid) {
        return cashbookEntryRepository.findByGuid(guid);
    }

    @Override
    public void addCashbookEntry(CashbookEntry cashbookEntry, Store store, User user) {
        if (cashbookEntry.getGuid() == null || cashbookEntry.getGuid().isEmpty()) {
            cashbookEntry.setGuid(UUID.randomUUID().toString());
        }
        if (cashbookEntry.getCashBookId() == null) {
            cashbookEntry.setCashBookId(sequenceRepository.getNextSequenceId(SequenceTable.CASHBOOKENTRY.toString()));
        }
        if (cashbookEntry.getStoreGuid() == null) {
            cashbookEntry.setStoreGuid(store.getGuid());
        }
        if (cashbookEntry.getUserGuid() == null && user != null) {
            cashbookEntry.setUserGuid(user.getGuid());
        }
        cashbookEntry.setCreationDate(LocalDateTime.now());
        cashbookEntryRepository.save(cashbookEntry);

        try {
            syncRemoteInterface.syncChanges(cashbookEntry, Target.CASHBOOK, Action.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCashbookEntry(CashbookEntry cashbookEntry) {
        cashbookEntryRepository.save(cashbookEntry);

        try {
            syncRemoteInterface.syncChanges(cashbookEntry, Target.CASHBOOK, Action.UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
