package unipos.pos.components.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.WebUtils;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;
import unipos.common.remote.socket.SocketRemoteInterface;
import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

/**
 * Created by dominik on 08.09.15.
 */

@RestController
@RequestMapping("/log")
public class LogController {

    @Autowired
    private LogRemoteInterface logRemoteInterface;

    @Autowired
    private SocketRemoteInterface socketRemoteInterface;

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void log(HttpServletRequest request, @RequestBody LogMetadata logMetadata) {
        Log impl = null;

        try{
            impl = (Log) Class.forName("unipos.pos.components.log." + logMetadata.getEvent() + "Log").getConstructors()[0].newInstance(logMetadata.getParameters());
            logRemoteInterface.log(impl.getLogDto());

            // send to client
            impl.notifyClient(WebUtils.getCookie(request, "DeviceToken").getValue());
        }
        catch(Exception ex) {
            // send error to client that this log type is not implemented
            LogDto logDto = new LogDto();
            logDto.setDateTime(LocalDateTime.now());
            logDto.addParameter("ExceptionType", ex.getClass().toString());
            logDto.addParameter("ExceptionMessage", ex.getMessage());

            StringWriter errors = new StringWriter();
            ex.printStackTrace(new PrintWriter(errors));

            logDto.addParameter("ExceptionStackTrace", errors.toString());
            logRemoteInterface.log(logDto);
            socketRemoteInterface.sendToDevice(WebUtils.getCookie(request, "DeviceToken").getValue(), "/error", "Beim Erstellen eines Log-Eintrages ist ein Fehler aufgetreten: '" + ex.getMessage() + "'. Naehere Informationen entnehmen Sie bitte den Server-Logs.");
        }

    }
}