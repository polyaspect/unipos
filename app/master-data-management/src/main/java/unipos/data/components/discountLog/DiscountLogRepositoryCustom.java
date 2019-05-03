package unipos.data.components.discountLog;

import unipos.data.components.discount.Discount;
import unipos.data.components.paymentMethod.PaymentMethod;

import java.util.List;

/**
 * Created by dominik on 31.08.15.
 */
public interface DiscountLogRepositoryCustom {

    List<Discount> adminListDiscounts();

    Long setAllUnpublishedLogsToIgnored();
}
