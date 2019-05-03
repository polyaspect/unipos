package unipos.data.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.data.components.category.Category;
import unipos.data.components.category.CategoryRepository;
import unipos.data.components.category.CategoryServiceImpl;
import unipos.data.components.productLog.ProductLog;
import unipos.data.components.productLog.ProductLogService;
import unipos.data.components.sequence.ProductLogSequenceRepository;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateRepository;

/**
 * Created by dominik on 01.11.15.
 */
@RestController
@RequestMapping("/syncCategory")
public class CategorySyncController extends SyncController<Category> {

    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TaxRateRepository taxRateRepository;

    @Override
    protected void deleteEntity(Category entity) {
        categoryRepository.deleteByGuid(entity.getGuid());
    }

    @Override
    public void saveEntity(Category entity) {
        TaxRate taxRate = taxRateRepository.findFirstByGuid(entity.getTaxRate().getGuid());

        entity.setTaxRate(taxRate);

        categoryRepository.save(entity);
    }

    @Override
    protected void updateEntity(Category log) {
        Category updatingEntity = categoryRepository.findFirstByGuid(log.getGuid());
        TaxRate taxRate = taxRateRepository.findFirstByGuid(log.getTaxRate().getGuid());
        updatingEntity.setTaxRate(taxRate);
        updatingEntity.setName(log.getName());
        categoryRepository.save(updatingEntity);
    }

    @Override
    protected void updateSequenceNumber(Category entity) {
        //The Category does not have a sequence Number. So we don't have to do anything
    }
}
