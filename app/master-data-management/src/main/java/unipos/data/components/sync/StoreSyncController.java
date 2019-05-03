package unipos.data.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.data.components.company.StoreRepository;
import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.CompanyRepository;
import unipos.data.components.company.model.Store;

/**
 * Created by dominik on 01.11.15.
 */
@RestController
@RequestMapping("/syncStore")
public class StoreSyncController extends SyncController<Store> {

    @Autowired
    StoreRepository storeRepository;

    @Override
    protected void deleteEntity(Store entity) {
        storeRepository.deleteByGuid(entity.getGuid());
    }

    @Override
    public void saveEntity(Store entity) {
        entity.setControllerStore(false);
        storeRepository.save(entity);
    }

    @Override
    protected void updateEntity(Store log) {
        Store updatingStore = storeRepository.findFirstByGuid(log.getGuid());
        updatingStore.setName(log.getName());
        updatingStore.setAddress(log.getAddress());
        updatingStore.setStoreId(log.getStoreId());
        updatingStore.setControllerStore(false); //We need to set this to false, because we don't want to sync the flag. It indicated that the current controller is located in this store
        updatingStore.setCompanyGuid(log.getCompanyGuid());
        storeRepository.save(updatingStore);
    }

    @Override
    protected void updateSequenceNumber(Store entity) {
        //The Category does not have a sequence Number. So we don't have to do anything
    }
}
