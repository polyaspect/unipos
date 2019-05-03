package unipos.data.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.data.components.category.Category;
import unipos.data.components.category.CategoryRepository;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateRepository;

/**
 * Created by dominik on 01.11.15.
 */
@RestController
@RequestMapping("/syncTaxRate")
public class TaxRateSyncController extends SyncController<TaxRate> {

    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    TaxRateRepository taxRateRepository;

    @Override
    protected void deleteEntity(TaxRate entity) {
        taxRateRepository.deleteByGuid(entity.getGuid());
    }

    @Override
    public void saveEntity(TaxRate entity) {
        taxRateRepository.save(entity);
    }

    @Override
    protected void updateEntity(TaxRate log) {
        TaxRate toUpdateEntity = taxRateRepository.findFirstByGuid(log.getGuid());
        toUpdateEntity.setDescription(log.getDescription());
        toUpdateEntity.setName(log.getName());
        toUpdateEntity.setPercentage(log.getPercentage());
        toUpdateEntity.setTaxRateCategory(log.getTaxRateCategory());

        taxRateRepository.save(toUpdateEntity);
    }

    @Override
    protected void updateSequenceNumber(TaxRate entity) {
        //The TaxRate does not have a sequence Number. So we don't have to do anything
    }
}
