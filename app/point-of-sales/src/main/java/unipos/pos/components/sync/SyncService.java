package unipos.pos.components.sync;

/**
 * Created by dominik on 01.11.15.
 */
public interface SyncService<T> {

    void saveLog(T log);
}
