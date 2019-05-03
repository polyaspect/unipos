package unipos.data.components.productLog;

import unipos.data.components.exception.DataNotFoundException;
import unipos.data.components.product.Product;

import java.util.List;

/**
 * Created by dominik on 19.08.15.
 */
public interface ProductLogService {

    public List<Product> adminListProducts();

    public ProductLog getProductWithHighestProductNumber();

    boolean existsProductNumber(Long number);

    void publishChanges();

    void applyChanges();

    void resetChanges();

    List<Product> listProductsByCompanyGuid(String companyGuid) throws DataNotFoundException;

    int getMaxSortOrder();
}
