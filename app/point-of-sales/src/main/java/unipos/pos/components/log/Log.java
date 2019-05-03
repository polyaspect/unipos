package unipos.pos.components.log;

import unipos.common.remote.auth.model.User;
import unipos.common.remote.core.model.LogDto;
import unipos.common.remote.socket.model.Workstation;

import java.util.Map;

/**
 * Created by Gradnig on 07.11.2015.
 */
public abstract class Log {
    protected Map<String, String> parameters;
    protected User user;
    protected Workstation device;

    public Log(Map<String, String> parameters){
        this.parameters = parameters;
    }

    abstract LogDto getLogDto();
    abstract void notifyClient(String deviceToken);
}
