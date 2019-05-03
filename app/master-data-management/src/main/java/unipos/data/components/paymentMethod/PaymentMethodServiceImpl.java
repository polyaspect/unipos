package unipos.data.components.paymentMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.data.components.paymentMethodLog.PaymentMethodLog;
import unipos.data.components.paymentMethodLog.PaymentMethodLogRepository;
import unipos.data.components.productLog.LogAction;
import unipos.data.components.productLog.ProductLog;
import unipos.data.components.sequence.ProductLogSequenceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by dominik on 31.08.15.
 */
@Service
public class PaymentMethodServiceImpl implements PaymentMethodService {

    public static final String PAYMENT_METHOD_LOG_SEQ_KEY = "paymentMethodLogSeqKey";

    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    PaymentMethodLogRepository paymentMethodLogRepository;
    @Autowired
    ProductLogSequenceRepository productLogSequenceRepository;

    @Override
    public List<PaymentMethod> findAll() {
        return paymentMethodRepository.findAll();
    }

    @Override
    public PaymentMethod findByMongoDbId(String mongoDbId) {
        return paymentMethodRepository.findOne(mongoDbId);
    }

    @Override
    public PaymentMethod findByName(String name) {
        List<PaymentMethod> listofPaymentMethods = paymentMethodRepository.findByNameLikeIgnoreCase(name);
        if(listofPaymentMethods.size() > 0) {
            return listofPaymentMethods.get(0);
        }
        else return null;
    }

    @Override
    public void savePaymentMethod(PaymentMethod paymentMethod) {
        if(paymentMethod != null) {
            Long productIdentifier = productLogSequenceRepository.getNextSequenceId(PAYMENT_METHOD_LOG_SEQ_KEY);
            paymentMethod.setPaymentMethodIdentifier(productIdentifier);
            paymentMethod.setGuid(UUID.randomUUID().toString());

            PaymentMethodLog paymentMethodLog = PaymentMethodLog.newPaymentMethodLogFromPaymentMethod(paymentMethod);
            paymentMethodLog.setAction(LogAction.CREATE);
            paymentMethodLog.setDate(LocalDateTime.now());
            paymentMethodLogRepository.save(paymentMethodLog);
        } else {
            throw new IllegalArgumentException("The Attribute \"paymentMethod\" is NULL!");
        }
    }

    @Override
    public void updatePaymentMethod(PaymentMethod paymentMethod) {
        if(paymentMethod != null) {
            PaymentMethodLog paymentMethodLog = PaymentMethodLog.newPaymentMethodLogFromPaymentMethod(paymentMethod);
            paymentMethodLog.setAction(LogAction.UPDATE);
            paymentMethodLog.setDate(LocalDateTime.now());
            paymentMethodLogRepository.save(paymentMethodLog);
        }
    }

    @Override
    public void deletePaymentMethod(PaymentMethod paymentMethod) {
        if(paymentMethod != null) {
            PaymentMethodLog paymentMethodLog = PaymentMethodLog.newPaymentMethodLogFromPaymentMethod(paymentMethod);
            paymentMethodLog.setAction(LogAction.DELETE);
            paymentMethodLog.setDeleted(true);
            paymentMethodLog.setDate(LocalDateTime.now());
            paymentMethodLogRepository.save(paymentMethodLog);
        }
    }

    @Override
    public void deletePaymentMethodByPaymentMethodIdentifier(Long paymentMethodIdentifier) {
        if(paymentMethodLogRepository.findFirstByPaymentMethodIdentifierAndIgnoredOrderByDateDesc(paymentMethodIdentifier, false) != null) {
            PaymentMethodLog paymentMethodLog = paymentMethodLogRepository.findFirstByPaymentMethodIdentifierAndIgnoredOrderByDateDesc(paymentMethodIdentifier, false);
            paymentMethodLog.set_id(null);
            paymentMethodLog.setAction(LogAction.DELETE);
            paymentMethodLog.setDeleted(true);
            paymentMethodLog.setPublished(false);
            paymentMethodLog.setDate(LocalDateTime.now());
            paymentMethodLogRepository.save(paymentMethodLog);
        } else {
            throw new IllegalArgumentException("The Product with the given ProductNumber was not found");
        }
    }

    @Override
    public PaymentMethod findByGuid(String guid) {
        return paymentMethodRepository.findFirstByGuid(guid);
    }
}
