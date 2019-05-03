package unipos.data.components.paymentMethod;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.data.components.category.Category;

import java.util.List;

/**
 * Created by dominik on 29.07.15.
 */
public interface PaymentMethodRepository extends MongoRepository<PaymentMethod, String> {
    List<PaymentMethod> findByNameLikeIgnoreCase(String name);

    Long deleteByPaymentMethodIdentifier(Long paymentMethodIdentifier);

    PaymentMethod findFirstByGuid(String guid);
}
