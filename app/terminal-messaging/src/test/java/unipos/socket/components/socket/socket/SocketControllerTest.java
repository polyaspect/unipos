package unipos.socket.components.socket.socket;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import unipos.socket.components.socket.shared.AbstractRestControllerTest;
import unipos.socket.components.workstation.Workstation;
import unipos.socket.components.workstation.WorkstationService;

import javax.servlet.http.Cookie;

import java.util.UUID;

import static org.mockito.Mockito.when;

/**
 * Created by Dominik on 16.01.2016.
 */
public class SocketControllerTest extends AbstractRestControllerTest {

    @Autowired
    WorkstationService workstationService;

    @Test
    public void testCheckDeviceToken() throws Exception {
        Cookie deviceToken = new Cookie("DeviceToken", UUID.randomUUID().toString());
        deviceToken.setPath("/");
        Cookie authToken = new Cookie("AuthToken", UUID.randomUUID().toString());
        deviceToken.setPath("/");

        when(workstationService.findByDeviceId(deviceToken.getValue())).thenReturn(Workstation.builder().build());
    }
}