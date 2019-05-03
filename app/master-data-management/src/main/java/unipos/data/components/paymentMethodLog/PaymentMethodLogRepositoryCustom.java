package unipos.data.components.paymentMethodLog;

import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.components.product.Product;

import java.util.List;

/**
 * Created by dominik on 31.08.15.
 */
public interface PaymentMethodLogRepositoryCustom {

    List<PaymentMethod> adminListPaymentMethods();

    Long setAllUnpublishedLogsToIgnored();
}
