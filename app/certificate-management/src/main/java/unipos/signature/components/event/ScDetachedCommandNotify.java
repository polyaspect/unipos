package unipos.signature.components.event;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.integritySafeGuard.event.ScDetachedCommandImpl;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dominik on 28.01.17.
 */
@Service
public class ScDetachedCommandNotify extends ScDetachedCommandImpl {

    @Autowired
    SocketRemoteInterface socketRemoteInterface;

    @Override
    public void executeExternal(List<String> detachedReaderName) {
        if (detachedReaderName.size() > 0) {
            Gson gson = new Gson();

            socketRemoteInterface.sendToAll("/topic/smartCardDetached", gson.toJson(detachedReaderName));
        }
    }
}
