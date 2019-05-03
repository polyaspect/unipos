package unipos.printer.components.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Repository;

/**
 * Created by dominik on 25.08.15.
 */

@Repository
public class ProductLogSequenceRepositoryImpl implements ProductLogSequenceRepository {

    @Autowired
    MongoOperations mongoOperations;

    @Override
    public synchronized long getNextSequenceId(String key) {
        //Check if there's already a Sequence number inside the Table
        long count = mongoOperations.count(new Query(Criteria.where("_id").is(key)), SequenceId.class);
        if(count == 0) {
            mongoOperations.insert(SequenceId.builder().id(key).seq(0L).build());
        }

        //get sequence id
        Query query = new Query(Criteria.where("_id").is(key));

        //increase sequence id by 1
        Update update = new Update();
        update.inc("seq", 1);

        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(true);

        //this is the magic happened.
        SequenceId seqId =
                mongoOperations.findAndModify(query, update, options, SequenceId.class);

        //if no id, throws SequenceException
        //optional, just a way to tell user when the sequence id is failed to generate.
        if (seqId == null) {
            throw new IllegalArgumentException("Failed to generate Sequence");
        }

        return seqId.getSeq();
    }

    @Override
    public synchronized void setSequenceId(String key, long value) {
        //Check if there's already a Sequence number inside the Table
        long count = mongoOperations.count(new Query(Criteria.where("_id").is(key)), SequenceId.class);
        if(count == 0) {
            mongoOperations.insert(SequenceId.builder().id(key).seq(0L).build());
        }

        //get sequence id
        Query query = new Query(Criteria.where("_id").is(key));

        //increase sequence id by 1
        Update update = new Update();
        update.set("seq", value);

        //return new increased id
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.returnNew(false);

        //this is the magic happened.
        SequenceId seqId =
                mongoOperations.findAndModify(query, update, options, SequenceId.class);
    }
}
