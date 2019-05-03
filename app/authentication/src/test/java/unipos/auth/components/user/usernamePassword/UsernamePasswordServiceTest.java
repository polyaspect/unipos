package unipos.auth.components.user.usernamePassword;

import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import unipos.auth.components.right.Right;
import unipos.auth.components.user.User;
import unipos.auth.shared.AbstractServiceTest;
import unipos.common.remote.sync.SyncRemoteInterface;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Created by Dominik on 01.06.2015.
 */
public class UsernamePasswordServiceTest extends AbstractServiceTest{

    @Mock
    UsernamePasswordRepository usernamePasswordRepository;

    @Mock
    SyncRemoteInterface syncRemoteInterface;

    @InjectMocks
    UsernamePasswordService usernamePasswordService = new UsernamePasswordServiceImpl();

    @Test
    public void testLoadUserByUsername() throws Exception {
        //Init the MockValues
        Right right = new Right("ROLE_ADMIN");
        User user = new User("Dominik", "Schiener", true);
        user.setRights(Arrays.asList(right));
        UsernamePassword dominikCredentials = new UsernamePassword("Dominik", "password", user);
        dominikCredentials.setPasswordHash("testpasswordhash");

        //Testing
        when(usernamePasswordRepository.findByUsername(anyString())).thenReturn(dominikCredentials);
        assertThat(usernamePasswordService.loadUserByUsername("Dominik").getUsername(),is("Dominik"));
        assertThat(usernamePasswordService.loadUserByUsername("Dominik").getPassword(),is("testpasswordhash"));
        assertThat(usernamePasswordService.loadUserByUsername("Dominik").getAuthorities().size(), is(1));
    }

    @Test
    public void testSaveCredentials() throws Exception {
        Right right = new Right("ROLE_ADMIN");
        User user = new User("Dominik", "Schiener", true);
        user.setRights(Arrays.asList(right));
        UsernamePassword dominikCredentials = new UsernamePassword("Dominik", "asdf", user);

        when(usernamePasswordRepository.save(any(UsernamePassword.class))).thenReturn(dominikCredentials);
        usernamePasswordService.saveCredentials(dominikCredentials);
        verify(usernamePasswordRepository, times(1)).save(any(UsernamePassword.class));
    }

    @Test
    public void testDeleteCredentials() throws Exception {
        Right right = new Right("ROLE_ADMIN");
        User user = new User("Dominik", "Schiener", true);
        user.setRights(Arrays.asList(right));
        UsernamePassword dominikCredentials = new UsernamePassword("Dominik", "asdf", user);
        dominikCredentials.setId("1");

        doNothing().when(usernamePasswordRepository).delete(anyString());
        usernamePasswordService.deleteCredentials(dominikCredentials);
        verify(usernamePasswordRepository, times(1)).delete(anyString());
    }

    @Test
    public void testDeleteAllCredentials() throws Exception {
        doNothing().when(usernamePasswordRepository).deleteAll();
        usernamePasswordService.deleteAllCredentials();
        verify(usernamePasswordRepository, times(1)).deleteAll();
    }

    @Test
    public void testFindByUsername() throws Exception {
        when(usernamePasswordRepository.findByUsername(anyString())).thenReturn(new UsernamePassword("Dominik", "asdf", new User("Dominik", "Schiener", true)));
        usernamePasswordService.findByUsername("Dominik");
        verify(usernamePasswordRepository, times(1)).findByUsername("Dominik");
    }

    @Test
    public void testFindByUsernameAndPassword() throws Exception {
        when(usernamePasswordRepository.findByUsernameAndPassword(anyString(), anyString()))
                .thenReturn(new UsernamePassword("Dominik", "asdf", new User("Dominik", "Schiener", false)));
        usernamePasswordService.findByUsernameAndPassword("Dominik", "asdf");
        verify(usernamePasswordRepository, times(1)).findByUsernameAndPassword("Dominik","asdf");
    }
}