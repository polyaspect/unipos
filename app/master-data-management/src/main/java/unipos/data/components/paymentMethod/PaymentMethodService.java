package unipos.data.components.paymentMethod;

import java.util.List;

/**
 * Created by dominik on 31.08.15.
 */
public interface PaymentMethodService {

    List<PaymentMethod> findAll();

    PaymentMethod findByMongoDbId(String mongoDbId);

    PaymentMethod findByName(String name);

    void savePaymentMethod(PaymentMethod paymentMethod);

    public void updatePaymentMethod(PaymentMethod paymentMethod);

    void deletePaymentMethod(PaymentMethod paymentMethod);

    void deletePaymentMethodByPaymentMethodIdentifier(Long paymentMethodIdentifier);

    PaymentMethod findByGuid(String guid);
}
