package unipos.auth.components.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.auth.components.sequence.SequenceRepository;
import unipos.auth.components.sequence.SequenceTable;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPin;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinRepository;
import unipos.auth.components.user.usernamePassword.UsernamePassword;
import unipos.auth.components.user.usernamePassword.UsernamePasswordRepository;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;

import java.util.*;

/**
 * Created by Dominik on 27.05.2015.
 */
@Service("userServiceImpl")
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;
    @Autowired
    UsernamePasswordRepository usernamePasswordRepository;
    @Autowired
    MitarbeiteridPinRepository mitarbeiteridPinRepository;
    @Autowired
    SequenceRepository sequenceRepository;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;

    @Override
    public void createUser(User user) {
        if (user.getId() == null) {
            Long userId = sequenceRepository.getNextSequenceId(SequenceTable.USER);
            user.setUserId(userId);
        }
        if (user.getGuid() == null || user.getGuid().isEmpty()) {
            user.setGuid(UUID.randomUUID().toString());
        }
        userRepository.save(user);

        try {
            syncRemoteInterface.syncChanges(user, Target.USER, Action.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUser(User user) {

        //First I need to delete the appropriate Login Methods, so that it's not longer posible to login with them again
        List<UsernamePassword> usernamePasswords = usernamePasswordRepository.findByUser(user);
        usernamePasswords.forEach(x -> usernamePasswordRepository.deleteByGuid(x.getGuid()));

        List<MitarbeiteridPin> mitarbeiteridPins = mitarbeiteridPinRepository.findByUser(user);
        mitarbeiteridPins.forEach(x -> mitarbeiteridPinRepository.deleteByGuid(x.getGuid()));

        userRepository.delete(user);

        try {
            syncRemoteInterface.syncChanges(user, Target.USER, Action.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public User findUserByUsername(String username) {
        return usernamePasswordRepository.findByUsername(username).getUser();
    }

    @Override
    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

    @Override
    public User findUserById(String id) {
        return userRepository.findOne(id);
    }

    @Override
    public void deleteUser(String id) {

        User user = userRepository.findOne(id);
        //First I need to delete the appropriate Login Methods, so that it's not longer posible to login with them again
        List<UsernamePassword> usernamePasswords = usernamePasswordRepository.findByUser(user);
        usernamePasswords.forEach(x -> usernamePasswordRepository.deleteByGuid(x.getGuid()));

        List<MitarbeiteridPin> mitarbeiteridPins = mitarbeiteridPinRepository.findByUser(user);
        mitarbeiteridPins.forEach(x -> mitarbeiteridPinRepository.deleteByGuid(x.getGuid()));

        userRepository.delete(id);

        try {
            syncRemoteInterface.syncChanges(userRepository.findOne(id), Target.USER, Action.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteUserByUsername(String username) {
        userRepository.delete(usernamePasswordRepository.findByUsername(username).getUser().getId());

        try {
            syncRemoteInterface.syncChanges(usernamePasswordRepository.findByUsername(username).getUser(), Target.USER, Action.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteByGuid(String guid) {
        User user = userRepository.findByGuid(guid);

        //First I need to delete the appropriate Login Methods, so that it's not longer posible to login with them again
        List<UsernamePassword> usernamePasswords = usernamePasswordRepository.findByUser(user);
        usernamePasswords.forEach(x -> usernamePasswordRepository.deleteByGuid(x.getGuid()));

        List<MitarbeiteridPin> mitarbeiteridPins = mitarbeiteridPinRepository.findByUser(user);
        mitarbeiteridPins.forEach(x -> mitarbeiteridPinRepository.deleteByGuid(x.getGuid()));

        userRepository.deleteByGuid(guid);

        try {
            syncRemoteInterface.syncChanges(user, Target.USER, Action.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateUser(User user) {
        userRepository.save(user);

        try {
            syncRemoteInterface.syncChanges(user, Target.USER, Action.UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = userRepository.findAll();
        users.sort((x, y) -> x.getUserId().compareTo(y.getUserId()));
        return users;
    }

    @Override
    public User findUserByGuid(String guid) {
        return userRepository.findByGuid(guid);
    }

    @Override
    public User findUserByUserId(Long userId) {
        return userRepository.findByUserId(userId);
    }
}
