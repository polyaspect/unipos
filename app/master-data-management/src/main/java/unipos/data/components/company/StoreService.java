package unipos.data.components.company;

import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.Store;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Dominik on 19.12.2015.
 */
public interface StoreService {

    List<Store> findAll();
    List<Store> findByUser(String authToken);

    List<Store> findByUserId(String userGuid);

    Store findByGuid(String guid);
    Store findByDocumentId(String documentId);

    void saveStore(Store store);
    void updateStore(Store store);

    void deleteByGuid(String guid);
    void deleteByDocumentId(String documentId);

    void addStoreToCompany(String storeGuid, String companyGuid);
    void removeStorefromCompany(String storeGuid, String companyGuid);
    void changeStoreCompany(String storeGuid, String oldCompanyGuid, String newCompanyGuid);

    Store getStoreByUserIdAndDeviceId(String userId, String deviceId);
    Store getStoreByUserAndDevice(String authToken, String deviceToken);
    Store getStoreByUserAndDevice(HttpServletRequest request);

    /**
     * This method returns the store, the Unipos-Controller is placed in.
     * A unipos Controller can only get placed in one Store per company.
     * @param companyGuid is the guid of the store the unipos-Cotnroller is placed in
     * @return the Store instance the Controller o is placed in.
     */
    Store getControllerPlacedStore(String companyGuid);

    List<Store> getControllerPlacedStores();

    List<Store> findByCompanyGuid(String companyGuid);
}
