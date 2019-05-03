package unipos.data.components.paymentMethodLog;

import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.components.product.Product;
import unipos.data.components.productLog.ProductLog;

import java.util.List;

/**
 * Created by dominik on 19.08.15.
 */
public interface PaymentMethodLogService {

    public List<PaymentMethod> adminListPaymentMethods();

    void publishChanges();

    void applyChanges();

    void resetChanges();

    boolean existsProductNumber(Long paymentMethodIdentifier);

    PaymentMethod findByGuid(String guid);
}
