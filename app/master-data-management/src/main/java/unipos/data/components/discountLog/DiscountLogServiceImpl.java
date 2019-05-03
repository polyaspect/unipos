package unipos.data.components.discountLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;
import unipos.data.components.discount.Discount;
import unipos.data.components.discount.DiscountRepository;
import java.util.*;

/**
 * Created by dominik on 31.08.15.
 */

@Service
public class DiscountLogServiceImpl implements DiscountLogService {

    @Autowired
    DiscountRepository discountRepository;
    @Autowired
    DiscountLogRepository discountLogRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;

    @Override
    public List<Discount> adminListDiscounts() {
        return discountLogRepository.adminListDiscounts();
    }

    @Override
    public void publishChanges() {
        List<DiscountLog> unpublishedLogs;
        List<DiscountLog> createdDiscounts = new ArrayList<>();
        List<DiscountLog> updatedDiscounts = new ArrayList<>();
        List<DiscountLog> deletedDiscounts = new ArrayList<>();

        List<DiscountLog> logs = discountLogRepository.findByPublishedAndIgnored(false, false);
        Collections.sort(logs, (x, y) -> (x.getDiscountIdentifier().compareTo(y.getDiscountIdentifier())));

        Map<Long, DiscountLog> map = new HashMap<>();
        for (DiscountLog discountLog : logs) {
            if(!map.containsKey(discountLog.getDiscountIdentifier())) {
                map.put(discountLog.getDiscountIdentifier(), discountLog);
            }
            if(map.get(discountLog.getDiscountIdentifier()).getDate().compareTo(discountLog.getDate()) < 0) {
                map.put(discountLog.getDiscountIdentifier(), discountLog);
            }
        }
        unpublishedLogs = new ArrayList<>(map.values());

        //First of all I need to fetch all unpublished changes and seperate them by their executed Action
        for(DiscountLog discountLog : unpublishedLogs) {
            switch (discountLog.getAction()) {
                case DELETE:
                    deletedDiscounts.add(discountLog);
                    break;
                case UPDATE:
                    updatedDiscounts.add(discountLog);
                    break;
                case CREATE:
                    createdDiscounts.add(discountLog);
                    break;
            }
        }

        //After that I need to update,create or delete the products in the products table.
        createdDiscounts.forEach((x) -> discountRepository.save(x.getDiscount()));

        for(DiscountLog discountLog : updatedDiscounts) {
            discountRepository.deleteByDiscountIdentifier(discountLog.getDiscountIdentifier());
            discountRepository.save(discountLog.getDiscount());
        }

        deletedDiscounts.forEach((x) -> discountRepository.deleteByDiscountIdentifier(x.getDiscountIdentifier()));

        for(DiscountLog discountLog : logs) {
            discountLog.setPublished(true);
            discountLogRepository.save(discountLog);
            try {
                syncRemoteInterface.syncChanges(discountLog, Target.DISCOUNT, Action.valueOf(discountLog.getAction().name().toUpperCase()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void applyChanges() {
        List<DiscountLog> unpublishedLogs;
        List<DiscountLog> createdDiscounts = new ArrayList<>();
        List<DiscountLog> updatedDiscounts = new ArrayList<>();
        List<DiscountLog> deletedDiscounts = new ArrayList<>();

        List<DiscountLog> logs = discountLogRepository.findByPublishedAndIgnored(false, false);
        Collections.sort(logs, (x, y) -> (x.getDiscountIdentifier().compareTo(y.getDiscountIdentifier())));

        Map<Long, DiscountLog> map = new HashMap<>();
        for (DiscountLog discountLog : logs) {
            if(!map.containsKey(discountLog.getDiscountIdentifier())) {
                map.put(discountLog.getDiscountIdentifier(), discountLog);
            }
            if(map.get(discountLog.getDiscountIdentifier()).getDate().compareTo(discountLog.getDate()) < 0) {
                map.put(discountLog.getDiscountIdentifier(), discountLog);
            }
        }
        unpublishedLogs = new ArrayList<>(map.values());

        //First of all I need to fetch all unpublished changes and seperate them by their executed Action
        for(DiscountLog discountLog : unpublishedLogs) {
            switch (discountLog.getAction()) {
                case DELETE:
                    deletedDiscounts.add(discountLog);
                    break;
                case UPDATE:
                    updatedDiscounts.add(discountLog);
                    break;
                case CREATE:
                    createdDiscounts.add(discountLog);
                    break;
            }
        }

        //After that I need to update,create or delete the products in the products table.
        createdDiscounts.forEach((x) -> discountRepository.save(x.getDiscount()));

        for(DiscountLog discountLog : updatedDiscounts) {
            discountRepository.deleteByDiscountIdentifier(discountLog.getDiscountIdentifier());
            discountRepository.save(discountLog.getDiscount());
        }

        deletedDiscounts.forEach((x) -> discountRepository.deleteByDiscountIdentifier(x.getDiscountIdentifier()));

        for(DiscountLog discountLog : logs) {
            discountLog.setPublished(true);
            discountLogRepository.save(discountLog);
        }
    }

    @Override
    public void resetChanges() {
        discountLogRepository.setAllUnpublishedLogsToIgnored();
    }

    @Override
    public boolean existsDiscountIdentifier(Long discountIdentifier) {
        if(discountLogRepository.findFirstByDiscountIdentifierAndIgnoredOrderByDateDesc(discountIdentifier, false) == null) {
            return false;
        } else {
            return true;
        }
    }
}
