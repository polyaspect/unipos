package unipos.auth.components.user;

import org.junit.Test;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import unipos.auth.components.right.Right;
import unipos.auth.components.sequence.SequenceRepository;
import unipos.auth.components.sequence.SequenceTable;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinRepository;
import unipos.auth.components.user.usernamePassword.UsernamePassword;
import unipos.auth.components.user.usernamePassword.UsernamePasswordRepository;
import unipos.auth.shared.AbstractServiceTest;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Syncable;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest extends AbstractServiceTest {


    @InjectMocks
    private UserService userService = new UserServiceImpl();

    @Mock
    private UserRepository userRepository;
    @Mock
    private UsernamePasswordRepository usernamePasswordRepository;
    @Mock
    MitarbeiteridPinRepository mitarbeiteridPinRepository;
    @Mock
    private SequenceRepository sequenceRepository;
    @Mock
    SyncRemoteInterface syncRemoteInterface;

    @Test
    public void testCreateUser() throws Exception{
        User user = new User();
        user.setName("Dominik");
        user.setEnabled(true);
        user.setSurname("Schiener");
        when(sequenceRepository.getNextSequenceId(SequenceTable.USER)).thenReturn(1L);
        when(userRepository.save(any(User.class))).thenReturn(new User("Dominik", "asdf", true));
        userService.createUser(user);

        verify(userRepository).save(argThat(new ArgumentMatcher<User>() {
            @Override
            public boolean matches(Object o) {
                if(((User)o).getUserId() == 1) {
                    return true;
                }
                return false;
            }
        }));
    }

    @Test
    public void testFindAllUsers(){
        when(userRepository.findAll()).thenReturn(Arrays.asList(new User("dominik", "1234", true, 1L), new User("joyce", "alex", false, 2L)));
        assertThat(userService.findAllUsers().size(), is(2));
    }

    @Test
    public void testDeleteAllUsers() throws Exception {
        doNothing().when(userRepository).deleteAll();
        userService.deleteAllUsers();
    }

    @Test
    public void testDeleteUser() throws Exception {
        doNothing().when(userRepository).delete(any(User.class));
        when(mitarbeiteridPinRepository.findByUser(any(User.class))).thenReturn(Collections.emptyList());
        when(usernamePasswordRepository.findByUser(any(User.class))).thenReturn(Collections.emptyList());
        doNothing().when(syncRemoteInterface).syncChanges(any(Syncable.class), any(), any());
        userService.deleteUser(new User("Dominik", "asdf", true));
    }

    @Test
    public void testFindUserByUsername() throws Exception {
        Right right = new Right("ROLE_ADMIN");
        User user = new User("Dominik", "Schiener", true);
        user.setRights(Arrays.asList(right));
        UsernamePassword usernamePassword = new UsernamePassword("Dominik", "asdf", user);

        when(usernamePasswordRepository.findByUsername(anyString())).thenReturn(usernamePassword);
        assertThat(userService.findUserByUsername("Dominik").getName(), is("Dominik"));
    }

    @Test
    public void testFindUserById() throws Exception {
        when(userRepository.findOne(anyString())).thenReturn(new User("Dominik", "asdf", true));
        assertThat(userService.findUserById("jdfhasjdhf7289foi").getName(), is("Dominik"));
    }

    @Test
    public void testDeleteUserByUsername() throws Exception {
        Right right = new Right("ROLE_ADMIN");
        User user = new User("Dominik", "Schiener", true);
        user.setRights(Arrays.asList(right));
        UsernamePassword usernamePassword = new UsernamePassword("Dominik", "asdf", user);


        doNothing().when(userRepository).delete(anyString());
        when(usernamePasswordRepository.findByUsername(anyString())).thenReturn(usernamePassword);

        userService.deleteUserByUsername("Dominik");
    }
}