package unipos.signature.components.umsatzZaehler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

/**
 * Created by domin on 15.12.2016.
 */
@Repository
public class UmsatzZaehlerRepositoryImpl implements UmsatzZaehlerRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public synchronized void saveSynchronised(UmsatzZaehler umsatzZaehler) {
        mongoTemplate.save(umsatzZaehler);
    }
}
