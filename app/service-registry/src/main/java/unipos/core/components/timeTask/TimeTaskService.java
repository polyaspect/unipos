package unipos.core.components.timeTask;

import org.quartz.SchedulerException;
import unipos.common.remote.data.model.company.Store;

import java.util.List;

/**
 * Created by Dominik on 25.01.2016.
 */
public interface TimeTaskService {

    void scheduleAutoUpdate(Store store);

    List<String> getAllJobs() throws SchedulerException;

    public void deleteByStoreGuid(String storeGuid) throws SchedulerException;
}
