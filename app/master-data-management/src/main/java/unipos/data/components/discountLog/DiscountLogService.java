package unipos.data.components.discountLog;

import unipos.data.components.discount.Discount;
import unipos.data.components.paymentMethod.PaymentMethod;

import java.util.List;

/**
 * Created by dominik on 19.08.15.
 */
public interface DiscountLogService {

    public List<Discount> adminListDiscounts();

    void publishChanges();

    void applyChanges();

    void resetChanges();

    boolean existsDiscountIdentifier(Long discountIdentifier);
}
