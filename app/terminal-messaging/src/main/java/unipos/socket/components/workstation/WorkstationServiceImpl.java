package unipos.socket.components.workstation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.socket.components.sequence.SequenceRepository;
import unipos.socket.components.sequence.SequenceTable;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 13.08.2015.
 */

@Service
public class WorkstationServiceImpl implements WorkstationService {

    @Autowired
    WorkstationRepository workstationRepository;
    @Autowired
    SequenceRepository sequenceRepository;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public void saveWorkstation(Workstation workstation) {
        workstation.setDeviceNumber(sequenceRepository.getNextSequenceId(SequenceTable.WORKSTATION));
        workstationRepository.save(workstation);
    }

    @Override
    public void updateWorkstation(Workstation workstation) {
        workstationRepository.save(workstation);
    }

    @Override
    public Workstation findOne(String id) {
        return workstationRepository.findOne(id);
    }

    @Override
    public List<Workstation> findAll() {
        return workstationRepository.findAll();
    }

    @Override
    public Workstation findFirstByIpAdress(String ipAdress) {
        return workstationRepository.findFirstByIpAdress(ipAdress);
    }

    @Override
    public void deleteAllWorkStations() {
        workstationRepository.deleteAll();
    }

    @Override
    public void deleteOneWorkstation(String id) {
        workstationRepository.delete(id);
    }

    @Override
    public void deleteOneWorkstationByIpAdress(String ipAdress) {
        workstationRepository.deleteByIpAdress(ipAdress);
    }

    @Override
    public Workstation findByAuthToken(String authToken) {
        return workstationRepository.findFirstByAuthTokenOrderByCreationDate(authToken);
    }

    @Override
    public Workstation findByDeviceId(String deviceId) {
        return workstationRepository.findByDeviceId(deviceId);
    }

    @Override
    public Workstation createNewDevice(String storeGuid, String deviceName, String ipAddress) {
        Workstation workstation = Workstation.builder()
                .ipAdress(ipAddress)
                .creationDate(LocalDateTime.now())
                .deviceId(UUID.randomUUID().toString())
                .deviceNumber(sequenceRepository.getNextSequenceId(SequenceTable.WORKSTATION))
                .deviceName(deviceName)
                .stores(Arrays.asList(storeGuid))
                .currentStoreId(storeGuid)
                .build();

        workstationRepository.save(workstation);

        return workstation;
    }

    @Override
    public Workstation replaceDevice(String deviceId, String ipAddress, String storeGuid) {
        Workstation workstation = workstationRepository.findByDeviceId(deviceId);

        workstation.setIpAdress(ipAddress);
        workstation.setStores(Collections.singletonList(storeGuid));

        workstationRepository.save(workstation);

        return workstation;
    }

    @Override
    public Workstation addStoreToDevice(String storeGuid, String deviceId) {
        Workstation workstation = workstationRepository.findByDeviceId(deviceId);

        workstation.setCurrentStoreId(storeGuid);

        List<String> stores = workstation.getStores();
        if(!stores.contains(storeGuid)){
            stores.add(storeGuid);
        }
        workstation.setStores(stores);

        workstationRepository.save(workstation);

        return workstation;
    }

    @Override
    public void setCashierForWorkstation(String cashierId, String deviceId, String currentStoreId){
        Workstation workstation = workstationRepository.findByDeviceId(deviceId);
        workstation.setCashierId(cashierId);
        workstation.setCurrentStoreId(currentStoreId);
        workstationRepository.save(workstation);
    }

    @Override
    public List<Workstation> findByStoreGuid(String storeGuid) {
        return workstationRepository.findByStoresIn(storeGuid);
    }

    @Override
    public void addPrinterToDevice(Printer printer, String deviceGuid) throws IllegalArgumentException {

        if(printer == null || deviceGuid == null || deviceGuid.isEmpty()) {
            throw new IllegalArgumentException("The printer or the deviceGuid was NULL");
        }

        Workstation device = workstationRepository.findByDeviceId(deviceGuid);

        if(device == null) {
            throw new IllegalArgumentException("No Device found for the given DeviceGuid");
        }

        //This removes the printer with the identical guid from the list --> works like an update, because it removes it, and the next lines add it again
        device.setPrinters(device.getPrinters().stream().filter(p -> p.getPrinterGuid() != null && !p.getPrinterGuid().equals(printer.getPrinterGuid())).collect(Collectors.toList()));

        //if theres a new default printer, remove first the old default printer and then add the new printer as the default
        if(printer.isDefaultPrinter()) {
            device.getPrinters().stream().forEach(x -> x.setDefaultPrinter(false));
        }

        device.getPrinters().add(printer);

        workstationRepository.save(device);
    }

    @Override
    public Printer findDefaultPrinterOfDevice(String deviceId) {


        Workstation workstation = workstationRepository.findByDeviceId(deviceId);

        return workstation.getPrinters().stream().filter(x -> x.isDefaultPrinter()).findFirst().orElse(null);
    }

    @Override
    public Printer findDefaultPrinterOfDevice(Workstation workstation) {
        return null;
    }

    @Override
    public boolean hasDefaultPrinter(String deviceToken) {
        Workstation workstation = workstationRepository.findByDeviceId(deviceToken);

        return workstation != null && workstation.getPrinters().stream().filter(Printer::isDefaultPrinter).count() > 0;

    }

    @Override
    public void deleteByGuid(String guid) {
        workstationRepository.deleteByDeviceId(guid);
    }
}
