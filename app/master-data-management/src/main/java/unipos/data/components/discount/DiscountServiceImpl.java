package unipos.data.components.discount;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.data.components.discountLog.DiscountLog;
import unipos.data.components.discountLog.DiscountLogRepository;
import unipos.data.components.productLog.LogAction;
import unipos.data.components.sequence.ProductLogSequenceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Created by dominik on 31.08.15.
 */
@Service
public class DiscountServiceImpl implements DiscountService {

    public static final String DISCOUNT_LOG_SEQ_KEY = "discountLogSeqKey";

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    DiscountLogRepository discountLogRepository;
    @Autowired
    ProductLogSequenceRepository productLogSequenceRepository;

    @Override
    public List<Discount> findAll() {
        return discountRepository.findAll();
    }

    @Override
    public Discount findByMongoDbId(String mongoDbId) {
        return discountRepository.findOne(mongoDbId);
    }

    @Override
    public Discount findByName(String name) {
        List<Discount> listofPaymentMethods = discountRepository.findByNameLikeIgnoreCase(name);
        if(listofPaymentMethods.size() > 0) {
            return listofPaymentMethods.get(0);
        }
        else return null;
    }

    @Override
    public void saveDiscount(Discount discount) {
        if(discount != null) {
            Long productIdentifier = productLogSequenceRepository.getNextSequenceId(DISCOUNT_LOG_SEQ_KEY);
            discount.setDiscountIdentifier(productIdentifier);
            discount.setGuid(UUID.randomUUID().toString());

            DiscountLog discountLog = DiscountLog.newDiscountLogFromDiscount(discount);
            discountLog.setAction(LogAction.CREATE);
            discountLog.setDate(LocalDateTime.now());
            discountLogRepository.save(discountLog);
        } else {
            throw new IllegalArgumentException("The Attribute \"paymentMethod\" is NULL!");
        }
    }

    @Override
    public void updateDiscount(Discount discount) {
        if(discount != null) {
            DiscountLog discountLog = DiscountLog.newDiscountLogFromDiscount(discount);
            discountLog.setAction(LogAction.UPDATE);
            discountLog.setDate(LocalDateTime.now());
            discountLogRepository.save(discountLog);
        }
    }

    @Override
    public void deleteDiscount(Discount discount) {
        if(discount != null) {
            DiscountLog discountLog = DiscountLog.newDiscountLogFromDiscount(discount);
            discountLog.setAction(LogAction.DELETE);
            discountLog.setDeleted(true);
            discountLog.setDate(LocalDateTime.now());
            discountLogRepository.save(discountLog);
        }
    }

    @Override
    public void deleteDiscountByDiscountIdentifier(Long discountIdentifier) {
        if(discountLogRepository.findFirstByDiscountIdentifierAndIgnoredOrderByDateDesc(discountIdentifier, false) != null) {
            DiscountLog discountLog = discountLogRepository.findFirstByDiscountIdentifierAndIgnoredOrderByDateDesc(discountIdentifier, false);
            discountLog.set_id(null);
            discountLog.setAction(LogAction.DELETE);
            discountLog.setDeleted(true);
            discountLog.setPublished(false);
            discountLog.setDate(LocalDateTime.now());
            discountLogRepository.save(discountLog);
        } else {
            throw new IllegalArgumentException("The Discount with the given Discount-Identifier was not found");
        }
    }
}
