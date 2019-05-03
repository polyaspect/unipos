package unipos.pos.components.log;

import unipos.common.remote.core.CoreRemoteInterface;
import unipos.common.remote.core.CoreRemoteInterfaceImpl;
import unipos.common.remote.core.model.LogDto;
import unipos.common.remote.core.model.Module;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterfaceImpl;

import java.util.List;
import java.util.Map;

/**
 * Created by Gradnig on 07.11.2015.
 */
public class Queue404Log extends Log{
    CoreRemoteInterface coreRemoteInterface = new CoreRemoteInterfaceImpl();
    SocketRemoteInterface socketRemoteInterface = new SocketRemoteInterfaceImpl();

    private String requestedModule = "";

    private enum Cause{
        CORE_NOT_INSTALLED,
        MODULE_NOT_INSTALLED,
        ACTION_DOES_NOT_EXIST
    }
    private Cause cause;

    public Queue404Log(Map<String, String> parameters) {
        super( parameters);

        // find out what caused the problem
        try{
            List<Module> startedModules = coreRemoteInterface.startedModules();
            requestedModule = parameters.get("url").split("/")[1];

            for(Module module : startedModules){
                if(module.getName().equalsIgnoreCase(requestedModule)) {
                    cause = Cause.ACTION_DOES_NOT_EXIST;
                }
            }
            if(cause == null){
                cause = Cause.MODULE_NOT_INSTALLED;
            }
        }
        catch(Exception ex){
            cause = Cause.CORE_NOT_INSTALLED;
        }

    }

    @Override
    public LogDto getLogDto() {
        LogDto log = new LogDto();
        log.setLevel(LogDto.Level.ERROR);
        log.setMessage("POST request to '" + parameters.get("url") + "' resulted in an error 404 - " +
                "Not found. Most likely reason is: " + cause.toString());

        return log;
    }

    @Override
    public void notifyClient(String deviceToken) {
        if(cause == Cause.MODULE_NOT_INSTALLED){
            socketRemoteInterface.sendToDevice(deviceToken, "error", "Das Modul '" + requestedModule + "' ist nicht aktiv. Bitte ueberpr�fen Sie, ob Sie das Modul installiert und gestartet haben.");
        }
        else if(cause == Cause.CORE_NOT_INSTALLED){
            socketRemoteInterface.sendToDevice(deviceToken, "info", "Sie befinden sich im Sandbox-Modus des POS-Moduls. Die angeforderte URL '" + parameters.get("url") + "' hat keine Implementierung. Bitte fuegen Sie eine Sandbox-Implementierung hinzu.");
        }
        else if(cause == Cause.ACTION_DOES_NOT_EXIST){
            socketRemoteInterface.sendToDevice(deviceToken, "error", "Die angeforderte URL '" + parameters.get("url") + "' wurde nicht gefunden. Der Fehler wurde bereits an den Software-Hersteller weitergeleitet.");
        }
    }
}
