package unipos.data.components.discountLog;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.data.components.paymentMethodLog.PaymentMethodLog;
import unipos.data.components.paymentMethodLog.PaymentMethodLogRepositoryCustom;

import java.util.List;

/**
 * Created by dominik on 31.08.15.
 */
public interface DiscountLogRepository extends MongoRepository<DiscountLog, String>, DiscountLogRepositoryCustom {

    DiscountLog findFirstByDiscountIdentifierAndIgnoredOrderByDateDesc(Long discountIdentifier, boolean b);

    List<DiscountLog> findByPublishedAndIgnored(boolean b, boolean b1);
}
