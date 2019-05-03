package unipos.signature.components.sequence;

/**
 * Created by dominik on 25.08.15.
 */
public interface SequenceRepository {

    public long getNextSequenceId(String key);

    public void setSequenceId(String key, long value);
}
