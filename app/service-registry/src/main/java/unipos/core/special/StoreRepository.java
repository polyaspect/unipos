package unipos.core.special;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.common.remote.data.model.company.Store;

import java.util.List;

/**
 * Created by Dominik on 25.01.2016.
 */
public interface StoreRepository extends MongoRepository<Store, String> {

    List<Store> findByControllerStore(boolean controllerStore);
}
