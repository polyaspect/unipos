package unipos.data.components.company;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.data.components.company.model.Store;

import java.util.List;

/**
 * Created by Dominik on 19.12.2015.
 */
public interface StoreRepository extends MongoRepository<Store, String> {
    Store findFirstByGuid(String guid);
    Long deleteByGuid(String guig);

    List<Store> findByControllerStoreAndCompanyGuid(boolean controllerLocation, String companyGuid);

    List<Store> findByControllerStore(boolean b);

    List<Store> findByCompanyGuid(String companyGuid);
}
