package unipos.data.remote.sync;

import org.junit.Test;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.SyncRemoteInterfaceImpl;
import unipos.data.shared.AbstractServiceTest;

/**
 * Created by dominik on 01.11.15.
 */
public class SyncRemoteServiceTest extends AbstractServiceTest {

    SyncRemoteInterface syncRemoteInterface = new SyncRemoteInterfaceImpl();

    @Test
    public void testSyncChanges() throws Exception {
    }
}
