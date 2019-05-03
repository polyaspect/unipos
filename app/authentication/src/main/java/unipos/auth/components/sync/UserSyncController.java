package unipos.auth.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.auth.components.sequence.SequenceRepository;
import unipos.auth.components.sequence.SequenceTable;
import unipos.auth.components.user.User;
import unipos.auth.components.user.UserRepository;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPin;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinRepository;
import unipos.auth.components.user.usernamePassword.UsernamePassword;
import unipos.auth.components.user.usernamePassword.UsernamePasswordRepository;
import unipos.common.container.SyncController;

import java.util.List;

/**
 * Created by Dominik on 01.01.2016.
 */

@RestController
@RequestMapping("/syncUser")
public class UserSyncController extends SyncController<User> {

    @Autowired
    UserRepository userRepository;
    @Autowired
    SequenceRepository sequenceRepository;
    @Autowired
    UsernamePasswordRepository usernamePasswordRepository;
    @Autowired
    MitarbeiteridPinRepository mitarbeiteridPinRepository;

    @Override
    protected void deleteEntity(User entity) {
        User user = userRepository.findByGuid(entity.getGuid());

        List<UsernamePassword> usernamePasswords = usernamePasswordRepository.findByUser(user);
        usernamePasswords.forEach(x -> usernamePasswordRepository.deleteByGuid(x.getGuid()));

        List<MitarbeiteridPin> mitarbeiteridPins = mitarbeiteridPinRepository.findByUser(user);
        mitarbeiteridPins.forEach(x -> mitarbeiteridPinRepository.deleteByGuid(x.getGuid()));

        userRepository.deleteByGuid(entity.getGuid());
    }

    @Override
    public void saveEntity(User entity) {
        userRepository.save(entity);
    }

    @Override
    protected void updateEntity(User log) {
        User toUpdateUser = userRepository.findByGuid(log.getGuid());
        toUpdateUser.setCompanyGuid(log.getCompanyGuid());
        toUpdateUser.setEnabled(log.isEnabled());
        toUpdateUser.setName(log.getName());
        toUpdateUser.setRights(log.getRights());
        toUpdateUser.setSurname(log.getSurname());

        userRepository.save(toUpdateUser);
    }

    @Override
    protected void updateSequenceNumber(User entity) {
        sequenceRepository.setSequenceId(SequenceTable.USER, entity.getUserId());
    }
}
