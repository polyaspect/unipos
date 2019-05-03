package unipos.data.components.product;

import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.Store;
import unipos.data.components.productLog.ProductLog;

import java.util.List;

/**
 * Created by Dominik on 23.07.2015.
 */
public interface ProductService {
    void setSortOrderAutomatically(ProductLog product);

    void setSortOrderToNumber();

    void saveProduct(Product product);

    void saveProducts(Product... products);

    void updateProduct(Product product);

    void updateStores(Product product);

    List<Product> listProducts();

    List<Product> listProductsByStore(Store store);

    List<Product> listProductsByCompany(Company company);

    List<Product> listProductsByCompanyGuid(String companyGuid);

    void deleteProduct(Product product);

    void deleteProduct(String id);

    void deleteProductByProductNumber(Long productNumber);

    void deleteAllProducts();

    Product findProductById(String id);

    Product findProductByProductIdentifier(int number);

    List<Product> findProductByNumberOrName(String searchString);

    List<String> getAllAtributes();

    List<Product> getByAtributes(List<String> attributes);

    void reduceStockAmountForProductGuid(Product product);
}
