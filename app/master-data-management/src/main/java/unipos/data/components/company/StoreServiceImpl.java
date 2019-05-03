package unipos.data.components.company;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.core.CoreRemoteInterface;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.socket.model.Workstation;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;
import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.CompanyRepository;
import unipos.data.components.company.model.Store;
import unipos.data.components.sequence.ProductLogSequenceRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 19.12.2015.
 */
@Service
public class StoreServiceImpl implements StoreService {

    @Autowired
    StoreRepository storeRepository;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    ProductLogSequenceRepository sequenceRepository;
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    PosRemoteInterface posRemoteInterface;
    @Autowired
    CoreRemoteInterface coreRemoteInterface;
    @Autowired
    ModelMapper modelMapper;

    @Override
    public List<Store> findAll() {
        return storeRepository.findAll();
    }

    @Override
    public List<Store> findByUser(String authToken) {
        User user = authRemoteInterface.findUserByAuthToken(authToken);
        return storeRepository.findAll().stream().filter(x -> x.getCompanyGuid().equals(user.getCompanyGuid())).collect(Collectors.toList());
    }

    @Override
    public List<Store> findByUserId(String userGuid) {
        User user = authRemoteInterface.getUserByGuid(userGuid);
        return storeRepository.findAll().stream().filter(x -> x.getCompanyGuid().equals(user.getCompanyGuid())).collect(Collectors.toList());
    }

    @Override
    public Store findByGuid(String guid) {
        return storeRepository.findFirstByGuid(guid);
    }

    @Override
    public Store findByDocumentId(String documentId) {
        return storeRepository.findOne(documentId);
    }

    @Override
    public void saveStore(Store store) {
        store.setId(null);
        if (store.getGuid() == null || store.getGuid().isEmpty()) {
            store.setGuid(UUID.randomUUID().toString());
        }
        store.setStoreId(sequenceRepository.getNextSequenceId("stores"));

        //HTML Time input is one hour behind. So I need to do the following action
        if (store.getCloseHour() != null) {
            store.setCloseHour(store.getCloseHour().plusHours(1L));
        }

        //It can only be one store simultaneously marked as the "controllerStore". We need to check this
        if (store.isControllerStore()) {
            storeRepository.findByControllerStoreAndCompanyGuid(true, store.getCompanyGuid()).stream().forEach(x -> {
                x.setControllerStore(false);
                storeRepository.save(x);
            });

            unipos.common.remote.data.model.company.Store mapedStore = modelMapper.map(store, unipos.common.remote.data.model.company.Store.class);
            posRemoteInterface.addNewAutoDailySettlementDateTime(mapedStore);
            coreRemoteInterface.addNewAutoUpdaterSettlementDateTime(mapedStore);
        }

        storeRepository.save(store);
        try {
            syncRemoteInterface.syncChanges(store, Target.STORE, Action.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        addStoreToCompany(store.getGuid(), store.getCompanyGuid());
    }

    @Override
    public void updateStore(Store store) {
        Store toUpdateStore = storeRepository.findFirstByGuid(store.getGuid());
        Company oldCompany = companyRepository.findFirstByStores_guid(store.getGuid());


        //HTML Time input is one hour behind. So I need to do the following action
        if (store.getCloseHour() != null) {
            store.setCloseHour(store.getCloseHour().plusHours(1L));
        }

        //It can only be one store simultaneously marked as the "controllerStore". We need to check this
        if (store.isControllerStore() && store.getCloseHour() != null) {
            storeRepository.findByControllerStoreAndCompanyGuid(true, store.getCompanyGuid()).stream().forEach(x -> {
                x.setControllerStore(false);
                storeRepository.save(x);
            });

            unipos.common.remote.data.model.company.Store mapedStore = modelMapper.map(store, unipos.common.remote.data.model.company.Store.class);
            posRemoteInterface.addNewAutoDailySettlementDateTime(mapedStore);
            coreRemoteInterface.addNewAutoUpdaterSettlementDateTime(mapedStore);
        }

        //Delete the existing Triggers if there's no time set. This should never be the case!!!
        //ToDo: Mit Gerhard reden, ob wir das zulassen wollen
        if (store.getCloseHour() == null && store.isControllerStore()) {
            posRemoteInterface.deleteAutoDailySettlementOfStore(modelMapper.map(store, unipos.common.remote.data.model.company.Store.class));
            coreRemoteInterface.deleteAutoUpdateSchedulerOfStore(modelMapper.map(store, unipos.common.remote.data.model.company.Store.class));
        }

        storeRepository.save(store);
        try {
            syncRemoteInterface.syncChanges(store, Target.STORE, Action.UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!toUpdateStore.getCompanyGuid().equals(store.getCompanyGuid())) {
            changeStoreCompany(store.getGuid(), oldCompany.getGuid(), store.getCompanyGuid());
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        Store store = storeRepository.findFirstByGuid(guid);
        Company toUpdateCompany = companyRepository.findFirstByGuid(store.getCompanyGuid());


        posRemoteInterface.deleteAutoDailySettlementOfStore(modelMapper.map(store, unipos.common.remote.data.model.company.Store.class));

        storeRepository.deleteByGuid(guid);
        try {
            syncRemoteInterface.syncChanges(store, Target.STORE, Action.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (toUpdateCompany != null) {
            removeStorefromCompany(store.getGuid(), store.getCompanyGuid());
        }
    }

    @Override
    public void deleteByDocumentId(String documentId) {
        Store store = storeRepository.findOne(documentId);
        storeRepository.delete(documentId);
        try {
            syncRemoteInterface.syncChanges(store, Target.STORE, Action.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addStoreToCompany(String storeGuid, String companyGuid) {
        Store store = storeRepository.findFirstByGuid(storeGuid);
        Company company = companyRepository.findFirstByGuid(companyGuid);

        if (company != null && store != null) {
            company.getStores().add(store);
        }

        companyRepository.save(company);

        try {
            syncRemoteInterface.syncChanges(company, Target.COMPANY, Action.UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeStorefromCompany(String storeGuid, String companyGuid) {
        Company company = companyRepository.findFirstByGuid(companyGuid);

        company.setStores(company.getStores().stream().filter(x -> !x.getGuid().equals(storeGuid)).collect(Collectors.toList()));

        companyRepository.save(company);

        try {
            syncRemoteInterface.syncChanges(company, Target.COMPANY, Action.UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void changeStoreCompany(String storeGuid, String oldCompanyGuid, String newCompanyGuid) {
        removeStorefromCompany(storeGuid, oldCompanyGuid);
        addStoreToCompany(storeGuid, newCompanyGuid);
    }

    @Override
    public Store getStoreByUserIdAndDeviceId(String userId, String deviceId) {
        User user = authRemoteInterface.getUserByGuid(userId);
        Workstation workstation = socketRemoteInterface.findByDeviceId(deviceId);

        if (user == null || workstation == null) {
            return null;
        }

        for (String storeGuid : workstation.getStores()) {
            Store store = storeRepository.findFirstByGuid(storeGuid);
            if (store.getCompanyGuid().equals(user.getCompanyGuid())) {
                return store;
            }
        }

        return null;
    }
    @Override
    public Store getStoreByUserAndDevice(String authToken, String deviceId) {
        User user = authRemoteInterface.findUserByAuthToken(authToken);
        Workstation workstation = socketRemoteInterface.findByDeviceId(deviceId);

        if (user == null || workstation == null) {
            return null;
        }

        for (String storeGuid : workstation.getStores()) {
            Store store = storeRepository.findFirstByGuid(storeGuid);
            if (store.getCompanyGuid().equals(user.getCompanyGuid())) {
                return store;
            }
        }

        return null;
    }

    public Store getStoreByUserAndDevice(HttpServletRequest request) {

        String deviceId = "";
        String authToken = "";

        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            switch (cookie.getName()) {
                case "AuthToken":
                    authToken = cookie.getValue();
                    break;
                case "DeviceToken":
                    deviceId = cookie.getValue();
                    break;
            }
        }

        if (deviceId.isEmpty() || authToken.isEmpty()) {
            return null;
        }

        try {
            User user = authRemoteInterface.findUserByAuthToken(authToken);
            Workstation workstation = socketRemoteInterface.findByDeviceId(deviceId);


            for (String storeGuid : workstation.getStores()) {
                Store store = storeRepository.findFirstByGuid(storeGuid);
                if (store.getCompanyGuid().equals(user.getCompanyGuid())) {
                    return store;
                }
            }
        } catch (Exception e) {
            return null;
        }

        return null;
    }

    @Override
    public Store getControllerPlacedStore(String companyGuid) {
        List<Store> result = storeRepository.findByControllerStoreAndCompanyGuid(true, companyGuid);

        if (result == null || result.size() > 1) {
            return null;
        }

        if (result.size() == 1) {
            return result.get(0);
        }

        return null;
    }

    @Override
    public List<Store> getControllerPlacedStores() {
        return storeRepository.findByControllerStore(true);
    }

    @Override
    public List<Store> findByCompanyGuid(String companyGuid) {
        return storeRepository.findByCompanyGuid(companyGuid);
    }
}
