package unipos.auth.components.user.mitarbeiteridPin;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.auth.components.user.User;

import java.util.List;

/**
 * Created by Dominik on 02.06.2015.
 */
public interface MitarbeiteridPinRepository extends MongoRepository<MitarbeiteridPin, String> {
    MitarbeiteridPin findByMitarbeiteridAndPin(int mitarbeiterid, int pin);
    MitarbeiteridPin findByMitarbeiterid(int mitarbeiterid);
    MitarbeiteridPin findByGuid(String guid);
    List<MitarbeiteridPin> findByUser(User user);

    Long deleteByGuid(String guid);
}
