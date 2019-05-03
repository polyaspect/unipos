package unipos.data.components.discount;

import unipos.data.components.paymentMethod.PaymentMethod;

import java.util.List;

/**
 * Created by dominik on 31.08.15.
 */
public interface DiscountService {

    List<Discount> findAll();

    Discount findByMongoDbId(String mongoDbId);

    Discount findByName(String name);

    void saveDiscount(Discount discount);

    public void updateDiscount(Discount discount);

    void deleteDiscount(Discount discount);

    void deleteDiscountByDiscountIdentifier(Long discountIdentifier);
}
