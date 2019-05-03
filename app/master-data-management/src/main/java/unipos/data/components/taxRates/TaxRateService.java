package unipos.data.components.taxRates;

import java.util.List;

/**
 * Created by dominik on 28.07.15.
 */
public interface TaxRateService {
    List<TaxRate> findAll();

    void deleteAllTaxRates();

    void deleteByGuid(String guid);

    void saveTaxRate(TaxRate taxRate);

    void deleteTaxRateByMongoId(String dbId);

    void updateTaxRate(TaxRate taxRate);
}
