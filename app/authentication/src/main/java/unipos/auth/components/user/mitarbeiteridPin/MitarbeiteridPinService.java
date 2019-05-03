package unipos.auth.components.user.mitarbeiteridPin;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Created by Dominik on 03.06.2015.
 */
public interface MitarbeiteridPinService extends UserDetailsService {
    void saveCredentials(MitarbeiteridPin credentials) throws IllegalArgumentException;

    void deleteCredentials(MitarbeiteridPin credentials);

    void deleteAllCredentials();

    MitarbeiteridPin findByMitarbeiterNr(int mitarbeiternr);

    MitarbeiteridPin findByMitarbeiterNrAndPin(int mitarbeiternr, int pin);

    MitarbeiteridPin findByUserGuid(String userGuid);

    List<MitarbeiteridPin> findAll();

    void updateCredentials(MitarbeiteridPin mitarbeiteridPin);

    void removeUserFromMitarbeiterNrPin(MitarbeiteridPin mitarbeiteridPin);
}
