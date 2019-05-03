package unipos.data.components.paymentMethodLog;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.components.productLog.ProductLog;
import unipos.data.components.productLog.ProductLogRepositoryCustom;

import java.util.List;

/**
 * Created by dominik on 31.08.15.
 */
public interface PaymentMethodLogRepository extends MongoRepository<PaymentMethodLog, String>, PaymentMethodLogRepositoryCustom {
    PaymentMethodLog findFirstByPaymentMethodIdentifierAndIgnoredOrderByDateDesc(Long paymentMethodIdentifier, boolean b);

    List<PaymentMethodLog> findByPublishedAndIgnored(boolean b, boolean b1);

    PaymentMethod findFirstByGuid(String guid);
}
