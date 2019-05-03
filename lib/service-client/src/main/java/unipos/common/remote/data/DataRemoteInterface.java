package unipos.common.remote.data;

import unipos.common.remote.data.model.PaymentMethod;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.data.model.discount.Discount;
import unipos.common.remote.data.model.product.Product;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by dominik on 04.09.15.
 */
public interface DataRemoteInterface {

    Product getProductByDocumentId(String documentId);

    Product getProductByProductIdentifier(String productIdentifier);

    Discount getDiscountByDocumentId(String documentId);

    Store getStoreByUserIdAndDeviceId(String userId, String deviceId);

    Store getStoreByAuthtokenAndDeviceid(String authToken, String deviceId);

    Store getStoreByAuthtokenAndDeviceid(HttpServletRequest request) throws IllegalArgumentException;

    Store getStoreByGuid(String guid);

    public Company getCompanyByGuid(String guid);

    PaymentMethod getPaymentMethodByGuid(String guid);

    List<PaymentMethod> getPaymentMethods();

    public Store getControllerPlacedStore(String companyGuid);

    List<Store> getStoresByPlacedController();

    String getCurrentVersion();

    List<Product> getByAtrributes(List<String> attributes, HttpServletRequest request);

    Company addCompany(Company company);

    void reduceStockAmountForProductGuid(Product product);
}
