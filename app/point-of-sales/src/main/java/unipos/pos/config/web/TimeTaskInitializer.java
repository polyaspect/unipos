package unipos.pos.config.web;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.pos.components.dailySettlement.timer.TimeTaskService;
import unipos.pos.special.StoreRepository;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * Created by Dominik on 25.01.2016.
 */
public class TimeTaskInitializer {

    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    TimeTaskService timeTaskService;
    @Autowired
    Scheduler scheduler;
    @Autowired
    StoreRepository storeRepository;

    @PostConstruct
    public void initTimeTasks() throws SchedulerException {
        scheduler.start();

        List<Store> stores = storeRepository.findByControllerStore(true);

        stores.forEach(store -> timeTaskService.scheduleDailySettlement(store));

    }
}
