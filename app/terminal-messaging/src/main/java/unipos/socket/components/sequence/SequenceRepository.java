package unipos.socket.components.sequence;

/**
 * Created by dominik on 25.08.15.
 */
public interface SequenceRepository {

    public long getNextSequenceId(SequenceTable key);

    public void setSequenceId(SequenceTable key, long value);
}
