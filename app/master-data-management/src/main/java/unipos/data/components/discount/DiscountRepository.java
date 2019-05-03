package unipos.data.components.discount;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.data.components.paymentMethod.PaymentMethod;

import java.util.List;

/**
 * Created by dominik on 29.07.15.
 */
public interface DiscountRepository extends MongoRepository<Discount, String> {
    List<Discount> findByNameLikeIgnoreCase(String name);

    Long deleteByDiscountIdentifier(Long discountIdentifier);
}
