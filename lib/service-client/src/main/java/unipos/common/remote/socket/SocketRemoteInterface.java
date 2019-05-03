package unipos.common.remote.socket;

import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.socket.model.Printer;
import unipos.common.remote.socket.model.Workstation;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by Gradnig on 07.11.2015.
 */
public interface SocketRemoteInterface {
    void sendToUser(String userId, String target, Object data);
    void sendToDevice(String deviceToken, String target, Object data);
    void sendToAll(String target, Object data);
    Workstation getWorkstationByAuthtoken(String authToken);
    Workstation findByDeviceId(String deviceId);
    List<Workstation> findByStoreGuid(String storeGuid);
    Printer findDeviceDefaultPrinter(String deviceId);
    Printer findDeviceDefaultPrinter(HttpServletRequest request);
    String getCurrentVersion();

    Store getStoreByDevice(String deviceToken);
}
