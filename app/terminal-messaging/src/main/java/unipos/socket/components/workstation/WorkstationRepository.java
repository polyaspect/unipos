package unipos.socket.components.workstation;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Dominik on 13.08.2015.
 */
public interface WorkstationRepository extends MongoRepository<Workstation, String>, WorkstationRepositoryCustom {
    Workstation findFirstByIpAdress(String ipAdress);

    Long deleteByIpAdress(String ipAdress);

    Workstation findFirstByAuthTokenOrderByCreationDate(String authToken);

    Workstation findByDeviceId(String deviceId);

    List<Workstation> findByStoresIn(String storeGuid);

    Long deleteByDeviceId(String deviceId);
}
