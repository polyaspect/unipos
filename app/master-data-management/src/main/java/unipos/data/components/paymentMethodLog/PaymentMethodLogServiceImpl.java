package unipos.data.components.paymentMethodLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;
import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.components.paymentMethod.PaymentMethodRepository;

import java.util.*;

/**
 * Created by dominik on 31.08.15.
 */

@Service
public class PaymentMethodLogServiceImpl implements PaymentMethodLogService {

    @Autowired
    PaymentMethodRepository paymentMethodRepository;
    @Autowired
    PaymentMethodLogRepository paymentMethodLogRepository;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;

    @Override
    public List<PaymentMethod> adminListPaymentMethods() {
        return paymentMethodLogRepository.adminListPaymentMethods();
    }

    @Override
    public void publishChanges() {
        List<PaymentMethodLog> unpublishedLogs;
        List<PaymentMethodLog> createdPaymentMethods = new ArrayList<>();
        List<PaymentMethodLog> updatedPaymentMethods = new ArrayList<>();
        List<PaymentMethodLog> deletedPayMentMethods = new ArrayList<>();

        List<PaymentMethodLog> logs = paymentMethodLogRepository.findByPublishedAndIgnored(false, false);
        Collections.sort(logs, (x, y) -> (x.getPaymentMethodIdentifier().compareTo(y.getPaymentMethodIdentifier())));

        Map<Long, PaymentMethodLog> map = new HashMap<>();
        for (PaymentMethodLog paymentMethodLog : logs) {
            if(!map.containsKey(paymentMethodLog.getPaymentMethodIdentifier())) {
                map.put(paymentMethodLog.getPaymentMethodIdentifier(), paymentMethodLog);
            }
            if(map.get(paymentMethodLog.getPaymentMethodIdentifier()).getDate().compareTo(paymentMethodLog.getDate()) < 0) {
                map.put(paymentMethodLog.getPaymentMethodIdentifier(), paymentMethodLog);
            }
        }
        unpublishedLogs = new ArrayList<>(map.values());

        //First of all I need to fetch all unpublished changes and seperate them by their executed Action
        for(PaymentMethodLog paymentMethodLog : unpublishedLogs) {
            switch (paymentMethodLog.getAction()) {
                case DELETE:
                    deletedPayMentMethods.add(paymentMethodLog);
                    break;
                case UPDATE:
                    updatedPaymentMethods.add(paymentMethodLog);
                    break;
                case CREATE:
                    createdPaymentMethods.add(paymentMethodLog);
                    break;
            }
        }

        //After that I need to update,create or delete the products in the products table.
        createdPaymentMethods.forEach((x) -> paymentMethodRepository.save(x.getPaymentMethod()));

        for(PaymentMethodLog paymentMethodLog : updatedPaymentMethods) {
            paymentMethodRepository.deleteByPaymentMethodIdentifier(paymentMethodLog.getPaymentMethodIdentifier());
            paymentMethodRepository.save(paymentMethodLog.getPaymentMethod());
        }

        deletedPayMentMethods.forEach((x) -> paymentMethodRepository.deleteByPaymentMethodIdentifier(x.getPaymentMethodIdentifier()));

        for(PaymentMethodLog paymentMethodLog : logs) {
            paymentMethodLog.setPublished(true);
            paymentMethodLogRepository.save(paymentMethodLog);
            try {
                syncRemoteInterface.syncChanges(paymentMethodLog, Target.PAYMENTMETHOD, Action.valueOf(paymentMethodLog.getAction().name()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void applyChanges() {
        List<PaymentMethodLog> unpublishedLogs;
        List<PaymentMethodLog> createdPaymentMethods = new ArrayList<>();
        List<PaymentMethodLog> updatedPaymentMethods = new ArrayList<>();
        List<PaymentMethodLog> deletedPayMentMethods = new ArrayList<>();

        List<PaymentMethodLog> logs = paymentMethodLogRepository.findByPublishedAndIgnored(false, false);
        Collections.sort(logs, (x, y) -> (x.getPaymentMethodIdentifier().compareTo(y.getPaymentMethodIdentifier())));

        Map<Long, PaymentMethodLog> map = new HashMap<>();
        for (PaymentMethodLog paymentMethodLog : logs) {
            if(!map.containsKey(paymentMethodLog.getPaymentMethodIdentifier())) {
                map.put(paymentMethodLog.getPaymentMethodIdentifier(), paymentMethodLog);
            }
            if(map.get(paymentMethodLog.getPaymentMethodIdentifier()).getDate().compareTo(paymentMethodLog.getDate()) < 0) {
                map.put(paymentMethodLog.getPaymentMethodIdentifier(), paymentMethodLog);
            }
        }
        unpublishedLogs = new ArrayList<>(map.values());

        //First of all I need to fetch all unpublished changes and seperate them by their executed Action
        for(PaymentMethodLog paymentMethodLog : unpublishedLogs) {
            switch (paymentMethodLog.getAction()) {
                case DELETE:
                    deletedPayMentMethods.add(paymentMethodLog);
                    break;
                case UPDATE:
                    updatedPaymentMethods.add(paymentMethodLog);
                    break;
                case CREATE:
                    createdPaymentMethods.add(paymentMethodLog);
                    break;
            }
        }

        //After that I need to update,create or delete the products in the products table.
        createdPaymentMethods.forEach((x) -> paymentMethodRepository.save(x.getPaymentMethod()));

        for(PaymentMethodLog paymentMethodLog : updatedPaymentMethods) {
            paymentMethodRepository.deleteByPaymentMethodIdentifier(paymentMethodLog.getPaymentMethodIdentifier());
            paymentMethodRepository.save(paymentMethodLog.getPaymentMethod());
        }

        deletedPayMentMethods.forEach((x) -> paymentMethodRepository.deleteByPaymentMethodIdentifier(x.getPaymentMethodIdentifier()));

        for(PaymentMethodLog paymentMethodLog : logs) {
            paymentMethodLog.setPublished(true);
            paymentMethodLogRepository.save(paymentMethodLog);
        }
    }

    @Override
    public void resetChanges() {
        paymentMethodLogRepository.setAllUnpublishedLogsToIgnored();
    }

    @Override
    public boolean existsProductNumber(Long paymentMethodIdentifier) {
        if(paymentMethodLogRepository.findFirstByPaymentMethodIdentifierAndIgnoredOrderByDateDesc(paymentMethodIdentifier, false) == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public PaymentMethod findByGuid(String guid) {
        return paymentMethodLogRepository.findFirstByGuid(guid);
    }
}
