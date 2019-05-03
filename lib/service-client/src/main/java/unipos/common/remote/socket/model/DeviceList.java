package unipos.common.remote.socket.model;

import unipos.common.remote.auth.model.User;

import java.util.ArrayList;

/**
 * Created by ggradnig on 18.01.2017.
 */
public class DeviceList extends ArrayList<Workstation> {
    public boolean containsDeviceId(String deviceId){
        for(Workstation workstation : this){
            if(workstation.getDeviceId().equals(deviceId))
                return true;
        }
        return false;
    }
}
