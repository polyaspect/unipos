package unipos.pos.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.pos.components.cashbook.CashbookEntry;
import unipos.pos.components.cashbook.CashbookEntryRepository;
import unipos.pos.components.sequence.SequenceRepository;
import unipos.pos.components.sequence.SequenceTable;

/**
 * Created by Dominik on 02.12.2015.
 */

@RestController
@RequestMapping("/syncCashbook")
public class CashbookSyncController extends SyncController<CashbookEntry> {

    @Autowired
    CashbookEntryRepository cashbookEntryRepository;
    @Autowired
    SequenceRepository sequenceRepository;

    @Override
    protected void deleteEntity(CashbookEntry entity) {
        cashbookEntryRepository.deleteByGuid(entity.getGuid());
    }

    @Override
    public void saveEntity(CashbookEntry entity) {
        cashbookEntryRepository.save(entity);
    }

    @Override
    protected void updateEntity(CashbookEntry log) {
        CashbookEntry toUpdateEntity = cashbookEntryRepository.findByGuid(log.getGuid());
        toUpdateEntity.setCashBookId(log.getCashBookId());
        toUpdateEntity.setCreationDate(log.getCreationDate());
        toUpdateEntity.setAmount(log.getAmount());
        toUpdateEntity.setDescription(log.getDescription());
        toUpdateEntity.setReference(log.getReference());
        toUpdateEntity.setType(log.getType());
        toUpdateEntity.setStoreGuid(log.getStoreGuid());

        cashbookEntryRepository.save(toUpdateEntity);

    }

    @Override
    protected void updateSequenceNumber(CashbookEntry entity) {
        sequenceRepository.setSequenceId(SequenceTable.CASHBOOKENTRY.toString(), entity.getCashBookId());
    }
}
