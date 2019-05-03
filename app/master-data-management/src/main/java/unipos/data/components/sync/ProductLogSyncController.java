package unipos.data.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.data.components.category.Category;
import unipos.data.components.category.CategoryRepository;
import unipos.data.components.product.ProductRepository;
import unipos.data.components.product.ProductServiceImpl;
import unipos.data.components.productLog.ProductLog;
import unipos.data.components.productLog.ProductLogService;
import unipos.data.components.sequence.ProductLogSequenceRepository;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateRepository;

/**
 * Created by dominik on 01.11.15.
 */
@RestController
@RequestMapping("/syncProduct")
public class ProductLogSyncController extends SyncController<ProductLog> {

    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    ProductLogService productLogService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    TaxRateRepository taxRateRepository;
    @Autowired
    ProductLogSequenceRepository sequenceRepository;

    @Override
    protected void deleteEntity(ProductLog entity) {
        saveEntity(entity);
    }

    @Override
    public void saveEntity(ProductLog entity) {
        Category category = categoryRepository.findFirstByGuid(entity.getProduct().getCategory().getGuid());
        if(category!=null) {
            TaxRate taxRate = taxRateRepository.findFirstByGuid(entity.getProduct().getCategory().getTaxRate().getGuid());
            category.setTaxRate(taxRate);
        }

        entity.getProduct().setCategory(category);
        entity.setPublished(false);

        mongoOperations.insert(entity);

        productLogService.applyChanges();
    }

    @Override
    protected void updateEntity(ProductLog log) {
        saveEntity(log);
    }

    @Override
    protected void updateSequenceNumber(ProductLog entity) {
        sequenceRepository.setSequenceId(ProductServiceImpl.PRODUCT_SEQ_KEY, entity.getProduct().getProductIdentifier());
    }
}
