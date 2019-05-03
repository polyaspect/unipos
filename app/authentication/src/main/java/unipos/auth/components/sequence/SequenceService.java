package unipos.auth.components.sequence;

/**
 * Created by dominik on 27.08.15.
 */
public interface SequenceService {
    public long getNextSequenceId(SequenceTable key);
}
