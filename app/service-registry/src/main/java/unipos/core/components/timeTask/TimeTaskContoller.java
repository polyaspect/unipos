package unipos.core.components.timeTask;

import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.common.remote.data.model.company.Store;

import java.util.List;

/**
 * Created by Dominik on 21.01.2016.
 */
@RestController
@RequestMapping("/timeTask")
public class TimeTaskContoller {

    @Autowired
    TimeTaskService timeTaskService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<String> findAllJobs() throws Exception {
        return timeTaskService.getAllJobs();
    }

    @RequestMapping(value = "/new", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addNewUpdateSchedule(@RequestBody Store store) {
        timeTaskService.scheduleAutoUpdate(store);
    }

    @RequestMapping(value = "/deleteByGuid/{storeGuid}", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteAutoUpdaterByStoreGuid(@PathVariable("storeGuid") String storeGuid) throws SchedulerException {
        timeTaskService.deleteByStoreGuid(storeGuid);
    }
}
