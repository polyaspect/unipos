package unipos.socket.components.workstation;

import java.util.List;

/**
 * Created by Dominik on 13.08.2015.
 */
public interface WorkstationService {

    void saveWorkstation(Workstation workstation);

    void updateWorkstation(Workstation workstation);

    Workstation findOne(String id);

    List<Workstation> findAll();

    Workstation findFirstByIpAdress(String ipAdress);

    void deleteAllWorkStations();

    void deleteOneWorkstation(String id);

    void deleteOneWorkstationByIpAdress(String ipAdress);

    Workstation findByAuthToken(String authToken);

    Workstation findByDeviceId(String deviceId);

    /**
     * Creates a new Workstation by the given Information and saves it in the DB. After that it returns the newly created Workstation.
     * @param storeGuid The GUID of the Store the device is used in.
     * @param deviceName The human readable name if the device
     * @param ipAddress The IP-Address of the creating Workstation
     * @return the newly created Workstation that is already stored in the database
     */
    Workstation createNewDevice(String storeGuid, String deviceName, String ipAddress);

    /**
     * Replaces a Device by the given deviceId. In fact this method just takes the IP Address and replaces the IP-Address of the Device with the given value
     * @param deviceId the DeviceID (GUID) of the Device
     * @param ipAddress the NEW IP-Address of the Device
     * @return the changed Device
     */
    Workstation replaceDevice(String deviceId, String ipAddress, String storeGuid);

    /**
     * Adds the store with the give storeGuid to the stores Workstation Entity inside the database and returns the newly stored entity
     * @param storeGuid the storeGuid of the store you want to add
     * @param deviceId the DeviceId you want to add the Store
     * @return the already saved Workstation
     */
    Workstation addStoreToDevice(String storeGuid, String deviceId);

    void setCashierForWorkstation(String cashierId, String deviceId, String currentStoreId);

    List<Workstation> findByStoreGuid(String storeGuid);

    void addPrinterToDevice(Printer printer, String deviceGuid) throws IllegalArgumentException;

    Printer findDefaultPrinterOfDevice(String deviceId);

    Printer findDefaultPrinterOfDevice(Workstation workstation);

    boolean hasDefaultPrinter(String deviceToken);

    void deleteByGuid(String guid);
}
