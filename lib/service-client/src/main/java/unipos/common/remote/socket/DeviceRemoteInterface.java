package unipos.common.remote.socket;

import javax.servlet.http.Cookie;

/**
 * Created by Joyce on 05.05.2016.
 */
public interface DeviceRemoteInterface {
    Cookie addStoreToDevice(String user);

    void setCashierForWorkstation(String cashierId, String deviceId);
}
