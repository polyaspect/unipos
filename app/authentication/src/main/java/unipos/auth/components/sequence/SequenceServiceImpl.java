package unipos.auth.components.sequence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by dominik on 27.08.15.
 */

@Service
public class SequenceServiceImpl implements SequenceService {

    @Autowired
    SequenceRepository sequenceRepository;

    @Override
    public long getNextSequenceId(SequenceTable key) {
        return sequenceRepository.getNextSequenceId(key);
    }
}
