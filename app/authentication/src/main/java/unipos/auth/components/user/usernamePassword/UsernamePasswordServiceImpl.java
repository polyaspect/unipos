package unipos.auth.components.user.usernamePassword;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import unipos.auth.components.right.Right;
import unipos.auth.components.user.User;
import unipos.auth.components.user.UserService;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;

import java.util.*;

/**
 * Created by Dominik on 01.06.2015.
 */

@Service
public class UsernamePasswordServiceImpl implements UsernamePasswordService {
    @Autowired
    UserService userService;
    @Autowired
    UsernamePasswordRepository usernamePasswordRepository;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;

    ShaPasswordEncoder passwordEncoder = new ShaPasswordEncoder(256);

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UsernamePassword credentials = usernamePasswordRepository.findByUsername(username);
        if(credentials == null) {
            return null;
        }
        User user = credentials.getUser();
        List<GrantedAuthority> authorities = buildUserAuthority(user.getRights());

        return buildUserForAuthentication(credentials, user, authorities);
    }

    private UserDetails buildUserForAuthentication(UsernamePassword credentials, User user, List<GrantedAuthority> authorities) {

        return new org.springframework.security.core.userdetails.User(credentials.getUsername(),credentials.getPasswordHash(), user.isEnabled(), true, true, true, authorities);
    }

    private List<GrantedAuthority> buildUserAuthority(List<Right> rights) {
        Set<GrantedAuthority> setAuths = new HashSet<>();

        for(Right right : rights) {
            setAuths.add(new SimpleGrantedAuthority(right.getName()));
        }

        List<GrantedAuthority> result = new ArrayList<>(setAuths);

        return result;
    }

    @Override
    public void saveCredentials(UsernamePassword credentials) {
        if((credentials.getUsername() == null || credentials.getUsername().isEmpty()) && (credentials.getPassword() == null || credentials.getPassword().isEmpty())) {
            return;
        }
        if(credentials.getGuid() == null || credentials.getGuid().isEmpty()) {
            credentials.setGuid(UUID.randomUUID().toString());
        }

        if(credentials.getPasswordSalt() == null || credentials.getPasswordSalt().isEmpty()) {
            credentials.setPasswordSalt(UUID.randomUUID().toString());
        }

        credentials.setActive(true);
        if(credentials.getPasswordHash() == null || credentials.getPasswordHash().isEmpty()){
            credentials.setPasswordHash(passwordEncoder.encodePassword(credentials.getPassword(), credentials.getPasswordSalt()));
        }

        credentials.setPassword(null);

        usernamePasswordRepository.save(credentials);

        try {
            syncRemoteInterface.syncChanges(credentials, Target.USERNAME_PASSWORD, Action.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCredentials(UsernamePassword credentials) {
        usernamePasswordRepository.delete(credentials.getId());
    }

    @Override
    public void deleteAllCredentials() {
        usernamePasswordRepository.deleteAll();
    }

    @Override
    public UsernamePassword findByUsername(String username) {
        return usernamePasswordRepository.findByUsername(username);
    }

    @Override
    public UsernamePassword findByUsernameAndPassword(String username, String password) {
        return usernamePasswordRepository.findByUsernameAndPassword(username, password);
    }

    @Override
    public List<UsernamePassword> findAll() {
        return usernamePasswordRepository.findAll();
    }

    @Override
    public void updateCredentials(UsernamePassword usernamePassword) {
        UsernamePassword toUpdateEntity = usernamePasswordRepository.findByGuid(usernamePassword.getGuid());
        toUpdateEntity.setUsername(usernamePassword.getUsername());
        toUpdateEntity.setActive(usernamePassword.isActive());
        if(usernamePassword.getPassword() != null && !usernamePassword.getPassword().isEmpty()) {
            toUpdateEntity.setPassword(usernamePassword.getPassword());
            toUpdateEntity.setPasswordHash(passwordEncoder.encodePassword(toUpdateEntity.getPassword(), toUpdateEntity.getPasswordSalt()));
            toUpdateEntity.setPassword(null);
        }
        usernamePasswordRepository.save(toUpdateEntity);

        try {
            syncRemoteInterface.syncChanges(toUpdateEntity, Target.USERNAME_PASSWORD, Action.UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeUserFromMitarbeiterNrPin(UsernamePassword usernamePassword) {
        usernamePassword.setUser(null);
        usernamePasswordRepository.save(usernamePassword);
    }

    @Override
    public UsernamePassword findByUserGuid(String userGuid) {
        User user = userService.findUserByGuid(userGuid);
        return usernamePasswordRepository.findByUser(user).get(0);
    }
}
