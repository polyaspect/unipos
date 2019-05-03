package unipos.socket.components.workstation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Created by dominik on 18.11.15.
 */

@Repository
public class WorkstationRepositoryImpl implements WorkstationRepositoryCustom {

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public Workstation findLatestWorkstationRegistration() {
        Query q = new Query().with(new Sort(Sort.Direction.DESC, "creationDate"));
        q.limit(1);
        List<Workstation> workstationList = mongoOperations.find(q, Workstation.class);
        Assert.notNull(workstationList);
        Assert.notNull(workstationList.get(0));
        return workstationList.get(0);
    }
}
