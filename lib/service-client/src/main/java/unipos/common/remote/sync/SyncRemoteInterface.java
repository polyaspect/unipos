package unipos.common.remote.sync;

import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Syncable;
import unipos.common.remote.sync.model.Target;

/**
 * Created by dominik on 01.11.15.
 */
public interface SyncRemoteInterface {

    /**
     * This method gets the Instance that I have to sync with the other modules and sends them
     * With Reflection, I can get the RequestMappingPath dynamically.
     * I'm just propagating the Log files, so they can do a "REDO"
     * @param data is the instance that the other modules have to store
     */
    void syncChanges(Syncable data, Target target) throws Exception;

    void syncChanges(Syncable data, Target target, Action action) throws Exception;

    String getCurrentVersion();
}
