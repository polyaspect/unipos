package unipos.pos.components.dailySettlement.timer;

import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.common.remote.data.model.company.Store;

import java.util.*;

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

    @RequestMapping(value = "/addDailySettlementDateTime", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addDailySettlementDateTime(@RequestBody Store store) {
        if (store == null || store.getCloseHour() == null) {
            return;
        }

        timeTaskService.scheduleDailySettlement(store);
    }

    @RequestMapping(value = "/deleteByStoreGuid", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void removeJobByName(@RequestParam("storeGuid") String storeGuid) throws SchedulerException {
        timeTaskService.deleteByStoreGuid(storeGuid);
    }
}
