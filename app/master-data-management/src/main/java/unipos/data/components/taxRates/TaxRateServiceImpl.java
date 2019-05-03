package unipos.data.components.taxRates;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;

import java.util.List;
import java.util.UUID;

/**
 * Created by dominik on 28.07.15.
 */
@Service
public class TaxRateServiceImpl implements TaxRateService {

    @Autowired
    TaxRateRepository taxRateRepository;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;

    @Override
    public List<TaxRate> findAll() {
        return taxRateRepository.findAll();
    }

    @Override
    public void saveTaxRate(TaxRate taxRate) {
        if(taxRate.getGuid() == null || taxRate.getGuid().isEmpty()) {
            taxRate.setGuid(UUID.randomUUID().toString());
        }
        taxRateRepository.save(taxRate);
        try {
            syncRemoteInterface.syncChanges(taxRate, Target.TAXRATE, Action.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void deleteTaxRateByMongoId(String dbId) throws IllegalArgumentException{
        TaxRate taxRate = taxRateRepository.findOne(dbId);
        if(taxRateRepository.exists(dbId)) {
            taxRateRepository.delete(dbId);
        } else {
            throw new IllegalArgumentException("TaxRate with the given ID is not existent in the DB");
        }
        try {
            syncRemoteInterface.syncChanges(taxRate, Target.TAXRATE, Action.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateTaxRate(TaxRate taxRate) {
        taxRateRepository.save(taxRate);

        try {
            syncRemoteInterface.syncChanges(taxRate, Target.TAXRATE, Action.UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteAllTaxRates() {
        taxRateRepository.deleteAll();
    }

    @Override
    public void deleteByGuid(String guid) {
        TaxRate taxRate = taxRateRepository.findFirstByGuid(guid);

        taxRateRepository.deleteByGuid(guid);

        try {
            syncRemoteInterface.syncChanges(taxRate, Target.TAXRATE, Action.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
