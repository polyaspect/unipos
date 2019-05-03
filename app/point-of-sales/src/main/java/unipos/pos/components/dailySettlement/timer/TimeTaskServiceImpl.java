package unipos.pos.components.dailySettlement.timer;

import org.quartz.*;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.stereotype.Service;
import unipos.common.remote.data.model.company.Store;
import unipos.pos.components.dailySettlement.DailySettlementService;

import java.time.LocalTime;
import java.util.*;

/**
 * Created by Dominik on 25.01.2016.
 */
@Service
public class TimeTaskServiceImpl implements TimeTaskService {

    @Autowired
    DailySettlementService dailySettlementService;
    @Autowired
    Scheduler scheduler;

    @Override
    public void scheduleDailySettlement(Store store) {
        try {
            MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();
            jobDetail.setTargetObject(dailySettlementService);
            jobDetail.setTargetMethod("addNewClosedSettlementForStoreDevices");
            jobDetail.setArguments(new Store[]{store});
            jobDetail.setGroup("DailySettlement");
            jobDetail.setName(store.getGuid());
            jobDetail.setConcurrent(false);
            jobDetail.afterPropertiesSet();

            // create CRON Trigger
            CronTriggerFactoryBean cronTrigger = new CronTriggerFactoryBean();
            cronTrigger.setBeanName(store.getGuid());

            LocalTime closeTime = store.getCloseHour().toLocalTime();
            closeTime = closeTime.minusSeconds(5L);
            String expression = closeTime.getSecond() + " " + closeTime.getMinute() + " " + closeTime.getHour() + " * * ?";
            cronTrigger.setCronExpression(expression);
            cronTrigger.setJobDetail(jobDetail.getObject());
            cronTrigger.afterPropertiesSet();

            //scheduler.scheduleJob(jobDetail, cronTrigger);

            Set<CronTrigger> set = new HashSet<>();
            set.add(cronTrigger.getObject());

            scheduler.scheduleJob(jobDetail.getObject(), set, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAllJobs() throws SchedulerException {
        List<String> result = new ArrayList<>();

        for (String groupName : scheduler.getJobGroupNames()) {

            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                String jobName = jobKey.getName();
                String jobGroup = jobKey.getGroup();

                //get job's trigger
                List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
                Date nextFireTime = triggers.get(0).getNextFireTime();

                result.add("[jobName] : " + jobName + " [groupName] : "
                        + jobGroup + " - " + nextFireTime);

            }

        }

        return result;
    }

    @Override
    public void deleteByStoreGuid(String storeGuid) throws SchedulerException {
        for (String groupName : scheduler.getJobGroupNames()) {

            for (JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {

                String jobName = jobKey.getName();
                String jobGroup = jobKey.getGroup();

                if(jobName.equals(storeGuid) && jobGroup.equals("DailySettlement")) {
                    scheduler.deleteJob(jobKey);
                }
            }

        }
    }
}
