package unipos.signature.components.event;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.integritySafeGuard.event.ScInsertedCommandImpl;
import unipos.signature.components.signatureOption.SignatureOptionRepository;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by domin on 22.01.2017.
 */
@Service
public class ScInsertedCommandNotify extends ScInsertedCommandImpl {

    @Autowired
    SignatureOptionRepository signatureOptionRepository;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;


    @Override
    public void executeExternal(Map<String, String> cards) {
        if (cards.size() > 0) {
            Gson gson = new Gson();

            socketRemoteInterface.sendToAll("/topic/smartCardInserted", gson.toJson(cards.values().stream().collect(Collectors.toList())));
        }
    }
}
