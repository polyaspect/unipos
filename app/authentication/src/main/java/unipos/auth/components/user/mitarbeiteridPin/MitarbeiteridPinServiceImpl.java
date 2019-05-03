package unipos.auth.components.user.mitarbeiteridPin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipos.auth.components.right.Right;
import unipos.auth.components.user.LoginMethod;
import unipos.auth.components.user.User;
import unipos.auth.components.user.UserService;
import unipos.auth.components.user.usernamePassword.UsernamePassword;
import unipos.auth.components.user.usernamePassword.UsernamePasswordRepository;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;

import java.util.*;

/**
 * Created by Dominik on 03.06.2015.
 */

@Service
public class MitarbeiteridPinServiceImpl implements MitarbeiteridPinService {
    @Autowired
    UserService userService;
    @Autowired
    MitarbeiteridPinRepository mitarbeiteridPinRepository;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MitarbeiteridPin credentials = mitarbeiteridPinRepository.findByMitarbeiterid(Integer.parseInt(username));
        if (credentials == null) {
            throw new UsernameNotFoundException("Mitarbeiter Nr. oder Passwort falsch.");
        }
        User user = credentials.getUser();
        if (user == null) {
            throw new UsernameNotFoundException("Dem angegebenen User wurde keine User Instanz zugewiesen. ");
        }
        List<GrantedAuthority> authorities = buildUserAuthority(user.getRights());

        return buildUserForAuthentication(credentials, user, authorities);
    }

    private UserDetails buildUserForAuthentication(MitarbeiteridPin credentials, User user, List<GrantedAuthority> authorities) {

        return new org.springframework.security.core.userdetails.User(String.valueOf(credentials.getMitarbeiterid()), String.valueOf(credentials.getPin()), user.isEnabled(), true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(List<Right> rights) {
        Set<GrantedAuthority> setAuths = new HashSet<>();

        if (rights == null) {
            return new ArrayList<>(setAuths);
        }

        for (Right right : rights) {
            setAuths.add(new SimpleGrantedAuthority(right.getName()));
        }

        List<GrantedAuthority> result = new ArrayList<>(setAuths);

        return result;
    }

    @Override
    public void saveCredentials(MitarbeiteridPin credentials) throws IllegalArgumentException {
        if (credentials.getPin() == -1 && credentials.getId().isEmpty()) {
            return;
        }
        if (mitarbeiteridPinRepository.findByMitarbeiterid(credentials.getMitarbeiterid()) != null) {
            throw new IllegalArgumentException("There's already a Member with this MitarbeiterId present");
        }
        if (credentials.getGuid() == null || credentials.getGuid().isEmpty()) {
            credentials.setGuid(UUID.randomUUID().toString());
        }
        mitarbeiteridPinRepository.save(credentials);

        try {
            syncRemoteInterface.syncChanges(credentials, Target.MITARBEITERID_PIN, Action.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCredentials(MitarbeiteridPin credentials) {
        mitarbeiteridPinRepository.delete(credentials.getId());
    }

    @Override
    public void deleteAllCredentials() {
        mitarbeiteridPinRepository.deleteAll();
    }

    @Override
    public MitarbeiteridPin findByMitarbeiterNr(int mitarbeiternr) {
        return mitarbeiteridPinRepository.findByMitarbeiterid(mitarbeiternr);
    }

    @Override
    public MitarbeiteridPin findByMitarbeiterNrAndPin(int mitarbeiternr, int pin) {
        return mitarbeiteridPinRepository.findByMitarbeiteridAndPin(mitarbeiternr, pin);
    }

    @Override
    public MitarbeiteridPin findByUserGuid(String userGuid) {
        User user = userService.findUserByGuid(userGuid);
        return mitarbeiteridPinRepository.findByUser(user).get(0);
    }

    @Override
    public List<MitarbeiteridPin> findAll() {
        return mitarbeiteridPinRepository.findAll();
    }

    @Override
    public void updateCredentials(MitarbeiteridPin mitarbeiteridPin) {
        MitarbeiteridPin oldCredential = mitarbeiteridPinRepository.findByMitarbeiterid(mitarbeiteridPin.getMitarbeiterid());
        if (oldCredential == null || oldCredential.getUser().getGuid().equals(mitarbeiteridPin.getUser().getGuid())) {
            MitarbeiteridPin toUpdateEntity = mitarbeiteridPinRepository.findByGuid(mitarbeiteridPin.getGuid());
            toUpdateEntity.setMitarbeiterid(mitarbeiteridPin.getMitarbeiterid());
            toUpdateEntity.setActive(mitarbeiteridPin.isActive());
            if (mitarbeiteridPin.getPin() != -1) {
                toUpdateEntity.setPin(mitarbeiteridPin.getPin());
            }
            mitarbeiteridPinRepository.save(toUpdateEntity);

            try {
                syncRemoteInterface.syncChanges(toUpdateEntity, Target.MITARBEITERID_PIN, Action.UPDATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalArgumentException("There's already a Member with this MitarbeiterId present");
        }
    }

    @Override
    public void removeUserFromMitarbeiterNrPin(MitarbeiteridPin mitarbeiteridPin) {
        mitarbeiteridPin.setUser(null);
        mitarbeiteridPinRepository.save(mitarbeiteridPin);
    }
}
