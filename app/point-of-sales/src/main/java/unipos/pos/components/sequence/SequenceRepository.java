package unipos.pos.components.sequence;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by dominik on 26.08.15.
 */
public interface SequenceRepository extends MongoRepository<SequenceId, String>, SequenceCustomRepository {
}
