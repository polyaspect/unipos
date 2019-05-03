package unipos.auth.components.sequence;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by dominik on 25.08.15.
 */
public interface SequenceCustomRepository {

    public long getNextSequenceId(SequenceTable key);

    public void setSequenceId(SequenceTable key, long value);
}
