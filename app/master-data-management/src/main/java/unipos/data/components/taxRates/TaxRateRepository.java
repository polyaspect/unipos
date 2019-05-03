package unipos.data.components.taxRates;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.data.components.product.Product;

/**
 * Created by dominik on 28.07.15.
 */
public interface TaxRateRepository extends MongoRepository<TaxRate, String> {

    TaxRate findFirstByGuid(String guid);

    Long deleteByGuid(String guid);

    TaxRate findFirstByPercentage(int percentage);
}
