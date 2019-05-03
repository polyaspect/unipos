package unipos.pos.components.sequence;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by dominik on 25.08.15.
 */
public interface SequenceCustomRepository {

    public long getNextSequenceId(String key);

    public void setSequenceId(String key, long value);
}
