package unipos.signature.components.event;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.integritySafeGuard.event.ScRemovedCommandImpl;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by domin on 22.01.2017.
 */
@Service
public class ScRemovedCommandNotify extends ScRemovedCommandImpl {

    @Autowired
    SocketRemoteInterface socketRemoteInterface;

    @Override
    public void executeExternal(Map<String, String> cards) {
        if (cards.size() > 0) {
            Gson gson = new Gson();

            socketRemoteInterface.sendToAll("/topic/smartCardRemoved", gson.toJson(cards.values().stream().collect(Collectors.toList())));
        }
    }
}
