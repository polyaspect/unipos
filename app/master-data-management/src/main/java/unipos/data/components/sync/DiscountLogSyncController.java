package unipos.data.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.data.components.category.Category;
import unipos.data.components.category.CategoryRepository;
import unipos.data.components.discount.Discount;
import unipos.data.components.discount.DiscountServiceImpl;
import unipos.data.components.discountLog.DiscountLog;
import unipos.data.components.discountLog.DiscountLogRepository;
import unipos.data.components.discountLog.DiscountLogService;
import unipos.data.components.productLog.ProductLog;
import unipos.data.components.productLog.ProductLogService;
import unipos.data.components.sequence.ProductLogSequenceRepository;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateRepository;

import java.util.UUID;

/**
 * Created by dominik on 01.11.15.
 */
@RestController
@RequestMapping("/syncDiscount")
public class DiscountLogSyncController extends SyncController<DiscountLog> {

    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    DiscountLogService discountLogService;
    @Autowired
    ProductLogSequenceRepository sequenceRepository;

    @Override
    protected void deleteEntity(DiscountLog entity) {
        saveEntity(entity);
    }

    @Override
    public void saveEntity(DiscountLog entity) {
        entity.setPublished(false);

        mongoOperations.insert(entity);

        discountLogService.applyChanges();
    }

    @Override
    protected void updateEntity(DiscountLog log) {
        saveEntity(log);
    }

    @Override
    protected void updateSequenceNumber(DiscountLog entity) {
        sequenceRepository.setSequenceId(DiscountServiceImpl.DISCOUNT_LOG_SEQ_KEY, entity.getDiscount().getDiscountIdentifier());
    }
}
