package unipos.auth.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.auth.components.user.User;
import unipos.auth.components.user.UserRepository;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPin;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinRepository;
import unipos.auth.components.user.usernamePassword.UsernamePassword;
import unipos.common.container.SyncController;

/**
 * Created by Dominik on 01.01.2016.
 */

@RestController
@RequestMapping("/syncMitarbeiteridPin")
public class MitarbeiteridPinSyncController extends SyncController<MitarbeiteridPin> {

    @Autowired
    MitarbeiteridPinRepository mitarbeiteridPinRepository;
    @Autowired
    UserRepository userRepository;

    @Override
    protected void deleteEntity(MitarbeiteridPin entity) {
        mitarbeiteridPinRepository.deleteByGuid(entity.getGuid());
    }

    @Override
    public void saveEntity(MitarbeiteridPin entity) {
        User applyUser = userRepository.findByGuid(entity.getUser().getGuid());
        entity.setUser(applyUser);

        mitarbeiteridPinRepository.save(entity);
    }

    @Override
    protected void updateEntity(MitarbeiteridPin log) {
        MitarbeiteridPin toUpdateEntity = mitarbeiteridPinRepository.findByGuid(log.getGuid());
        toUpdateEntity.setMitarbeiterid(log.getMitarbeiterid());
        if(log.getPin() != -1) {
            toUpdateEntity.setPin(log.getPin());
        }
        mitarbeiteridPinRepository.save(toUpdateEntity);
    }

    @Override
    protected void updateSequenceNumber(MitarbeiteridPin entity) {
        //Do nothing
    }
}
