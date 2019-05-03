package unipos.auth.components.user.mitarbeiteridPin;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import unipos.auth.components.right.Right;
import unipos.auth.components.user.User;
import unipos.auth.shared.AbstractServiceTest;
import unipos.common.remote.sync.SyncRemoteInterface;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Dominik on 04.06.2015.
 */
public class MitarbeiteridPinServiceTest extends AbstractServiceTest{

    @InjectMocks
    MitarbeiteridPinService mitarbeiteridPinService = new MitarbeiteridPinServiceImpl();

    @Mock
    SyncRemoteInterface syncRemoteInterface;

    @Mock
    MitarbeiteridPinRepository mitarbeiteridPinRepository;

    @Test
    public void testSaveCredentials() throws Exception {
        User user = new User("Gerhard", "Gradnig", true);
        MitarbeiteridPin credential = new MitarbeiteridPin(4789,9856, user);
        credential.setId("JRHFNF74JFNR87HF");

        when(mitarbeiteridPinRepository.save(any(MitarbeiteridPin.class))).thenReturn(credential);

        mitarbeiteridPinService.saveCredentials(credential);

        verify(mitarbeiteridPinRepository, times(1)).save(any(MitarbeiteridPin.class));
    }

    @Test
    public void testDeleteCredentials() throws Exception {
        User user = new User("Dominik", "Schiener", true);
        MitarbeiteridPin credential = new MitarbeiteridPin(1234,4321, user);
        credential.setId("67uk4nhgetdzfuiro5l4");

        doNothing().when(mitarbeiteridPinRepository).delete(anyString());

        mitarbeiteridPinService.deleteCredentials(credential);

        verify(mitarbeiteridPinRepository, times(1)).delete(anyString());
    }

    @Test
    public void testDeleteAllCredentials() throws Exception {
        doNothing().when(mitarbeiteridPinRepository).deleteAll();

        mitarbeiteridPinService.deleteAllCredentials();
        verify(mitarbeiteridPinRepository, times(1)).deleteAll();
    }

    @Test
    public void testLoadByUsernameWhereEverythingIsSetTheCorrectWay() throws Exception {
        Right admin = new Right("Admin");
        Right userRight = new Right("User");
        User user = new User("Joyce", "Kudumthanamkuzhy", true);
        user.setId("user1");
        user.setRights(Arrays.asList(admin,userRight));
        MitarbeiteridPin credentail = new MitarbeiteridPin(666,666, user);
        credentail.setId("credential1");

        when(mitarbeiteridPinRepository.findByMitarbeiterid(anyInt())).thenReturn(credentail);

        assertThat(mitarbeiteridPinService.loadUserByUsername("1234").getUsername(), is("666"));
        assertThat(mitarbeiteridPinService.loadUserByUsername("1234").getPassword(), is("666"));
        assertThat(mitarbeiteridPinService.loadUserByUsername("1234").getAuthorities().size(), is(2));
    }

    @Test(expected = NumberFormatException.class)
    public void testLoadByUsernameWithAlphanumericPin() throws Exception {
        mitarbeiteridPinService.loadUserByUsername("asdf");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadByUsernameWhereNoMitarbeiterpinInstanceIsFound() throws Exception {
        when(mitarbeiteridPinRepository.findByMitarbeiterid(anyInt())).thenReturn(null);

        mitarbeiteridPinService.loadUserByUsername("1234");
    }

    @Test(expected = UsernameNotFoundException.class)
    public void testLoadByUsernameWhereNoUserIsFound() throws Exception {
        MitarbeiteridPin credential = new MitarbeiteridPin(1234,4321, null);

        when(mitarbeiteridPinRepository.findByMitarbeiterid(anyInt())).thenReturn(credential);

        mitarbeiteridPinService.loadUserByUsername("1234");
    }

    @Test
    public void testLoadUserWithoutAnyRoles() throws Exception {
        User user = new User("Dominik", "Schiener", true);
        MitarbeiteridPin credential = new MitarbeiteridPin(1234, 4321, user);

        when(mitarbeiteridPinRepository.findByMitarbeiterid(anyInt())).thenReturn(credential);

        mitarbeiteridPinService.loadUserByUsername("1234");
    }

    @Test
    public void testLoadUserWhereRightsIsNULL() throws Exception {
        User user = new User("Dominik", "Schiener", true);
        user.setRights(null);
        MitarbeiteridPin credential = new MitarbeiteridPin(1234,4321, user);

        when(mitarbeiteridPinRepository.findByMitarbeiterid(anyInt())).thenReturn(credential);

        mitarbeiteridPinService.loadUserByUsername("1234");
    }

    @Test
    public void testFindMyMitarbeiterNr() throws Exception {
        User user = new User("Dominik", "Schiener", true);
        MitarbeiteridPin credential = new MitarbeiteridPin(1234, 4321, user);

        when(mitarbeiteridPinRepository.findByMitarbeiterid(anyInt())).thenReturn(credential);

        assertThat(mitarbeiteridPinService.findByMitarbeiterNr(1234).getMitarbeiterid(), is(1234));
        assertThat(mitarbeiteridPinService.findByMitarbeiterNr(1234).getPin(), is(4321));
        verify(mitarbeiteridPinRepository, times(2)).findByMitarbeiterid(1234);
    }

    @Test
    public void testFindByMitarbeiterNrAndPin() throws Exception {
        User user = new User("Dominik", "Schiener", true);
        MitarbeiteridPin credential = new MitarbeiteridPin(1234, 4321, user);

        when(mitarbeiteridPinRepository.findByMitarbeiteridAndPin(anyInt(), anyInt())).thenReturn(credential);

        assertThat(mitarbeiteridPinService.findByMitarbeiterNrAndPin(1234,4321).getMitarbeiterid(),is(1234));
        assertThat(mitarbeiteridPinService.findByMitarbeiterNrAndPin(1234,4321).getPin(),is(4321));
        verify(mitarbeiteridPinRepository, times(2)).findByMitarbeiteridAndPin(1234,4321);
    }
}