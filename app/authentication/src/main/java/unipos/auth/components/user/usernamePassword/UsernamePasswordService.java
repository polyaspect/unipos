package unipos.auth.components.user.usernamePassword;

import org.springframework.security.core.userdetails.UserDetailsService;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPin;

import java.util.List;

/**
 * Created by Dominik on 01.06.2015.
 */
public interface UsernamePasswordService extends UserDetailsService {
    void saveCredentials(UsernamePassword credentials);
    void deleteCredentials(UsernamePassword credentials);
    void deleteAllCredentials();
    UsernamePassword findByUsername(String username);
    UsernamePassword findByUsernameAndPassword(String username, String password);

    List<UsernamePassword> findAll();

    void updateCredentials(UsernamePassword usernamePassword);

    void removeUserFromMitarbeiterNrPin(UsernamePassword usernamePassword);

    UsernamePassword findByUserGuid(String userGuid);
}
