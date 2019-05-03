package unipos.auth.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.auth.components.sequence.SequenceRepository;
import unipos.auth.components.sequence.SequenceTable;
import unipos.auth.components.user.User;
import unipos.auth.components.user.UserRepository;
import unipos.auth.components.user.usernamePassword.UsernamePassword;
import unipos.auth.components.user.usernamePassword.UsernamePasswordRepository;
import unipos.common.container.SyncController;

/**
 * Created by Dominik on 01.01.2016.
 */

@RestController
@RequestMapping("/syncUsernamePassword")
public class UsernamePasswordSyncController extends SyncController<UsernamePassword> {

    @Autowired
    UsernamePasswordRepository usernamePasswordRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void deleteEntity(UsernamePassword entity) {
        usernamePasswordRepository.deleteByGuid(entity.getGuid());
    }

    @Override
    public void saveEntity(UsernamePassword entity) {
        User applyUser = userRepository.findByGuid(entity.getUser().getGuid());
        entity.setUser(applyUser);

        usernamePasswordRepository.save(entity);
    }

    @Override
    protected void updateEntity(UsernamePassword log) {
        UsernamePassword toUpdateEntity = usernamePasswordRepository.findByGuid(log.getGuid());
        toUpdateEntity.setUsername(log.getUsername());
        toUpdateEntity.setActive(log.isActive());
        if(log.getPassword() != null && !log.getPassword().isEmpty()) {
            toUpdateEntity.setPassword(log.getPassword());
        }
        usernamePasswordRepository.save(toUpdateEntity);
    }

    @Override
    protected void updateSequenceNumber(UsernamePassword entity) {
        //Do nothing
    }
}
