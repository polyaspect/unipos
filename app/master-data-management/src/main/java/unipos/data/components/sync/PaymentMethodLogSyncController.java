package unipos.data.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.data.components.category.Category;
import unipos.data.components.category.CategoryRepository;
import unipos.data.components.paymentMethod.PaymentMethodService;
import unipos.data.components.paymentMethod.PaymentMethodServiceImpl;
import unipos.data.components.paymentMethodLog.PaymentMethodLog;
import unipos.data.components.paymentMethodLog.PaymentMethodLogService;
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
@RequestMapping("/syncPaymentMethod")
public class PaymentMethodLogSyncController extends SyncController<PaymentMethodLog> {

    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    ProductLogSequenceRepository sequenceRepository;
    @Autowired
    PaymentMethodLogService paymentMethodLogService;

    @Override
    protected void deleteEntity(PaymentMethodLog entity) {
        saveEntity(entity);
    }

    @Override
    public void saveEntity(PaymentMethodLog entity) {
        entity.setPublished(false);

        mongoOperations.insert(entity);

        paymentMethodLogService.applyChanges();
    }

    @Override
    protected void updateEntity(PaymentMethodLog log) {
        saveEntity(log);
    }

    @Override
    protected void updateSequenceNumber(PaymentMethodLog entity) {
        sequenceRepository.setSequenceId(PaymentMethodServiceImpl.PAYMENT_METHOD_LOG_SEQ_KEY, entity.getPaymentMethod().getPaymentMethodIdentifier());
    }
}
