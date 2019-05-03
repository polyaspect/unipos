package unipos.pos.components.dailySettlement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import unipos.authChecker.domain.AuthTokenManager;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.report.DailySettlementHelper;
import unipos.common.remote.report.ReportRemoteInterface;
import unipos.common.remote.signature.SignatureRemoteInterface;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.socket.model.Workstation;
import unipos.pos.components.cashbook.CashbookEntryService;
import unipos.pos.components.invoice.InvoiceService;
import unipos.pos.components.invoice.model.Invoice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 19.01.2016.
 */
@Service
public class DailySettlementServiceImpl implements DailySettlementService {

    @Autowired
    DailySettlementRepository dailySettlementRepository;
    @Autowired
    MonthlySettlementRepository monthlySettlementRepository;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    SignatureRemoteInterface signatureRemoteInterface;
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    AuthTokenManager authTokenManager;
    @Autowired
    Environment environment;
    @Autowired
    ReportRemoteInterface reportRemoteInterface;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    CashbookEntryService cashbookEntryService;
    @Autowired
    InvoiceService invoiceService;

    @Override
    public DailySettlement addNewSettlement(Store store, Workstation workstation) {
        if (!isNewDailySettlementAllowed(store, workstation)) {
            return findLastCreatedDailySettlement(store, workstation);
        }

        DailySettlement dailySettlement = DailySettlement.builder()
                .guid(UUID.randomUUID().toString())
                .closed(false)
                .storeGuid(store.getGuid())
                .deviceId(getDailySettlementMode() == DailySettlement.Mode.DEVICE ? workstation.getDeviceId() : null)
                .dateTime(LocalDateTime.now())
                .build();

        dailySettlementRepository.save(dailySettlement);

        return dailySettlement;
    }

    @Override
    public MonthlySettlement addNewMonthlySettlement(Store store, Workstation workstation) {
        MonthlySettlement monthlySettlement = MonthlySettlement.builder()
                .guid(UUID.randomUUID().toString())
                .closed(false)
                .storeGuid(store.getGuid())
                .deviceId(getDailySettlementMode() == DailySettlement.Mode.DEVICE ? workstation.getDeviceId() : null)
                .dateTime(LocalDateTime.now())
                .build();

        monthlySettlementRepository.save(monthlySettlement);

        return monthlySettlement;
    }

    @Override
    public DailySettlement addNewSettlementByTime(LocalDateTime localDateTime, Store store, Workstation workstation) {

        DailySettlement dailySettlement = DailySettlement.builder()
                .dateTime(localDateTime)
                .guid(UUID.randomUUID().toString())
                .storeGuid(store.getGuid())
                .deviceId(getDailySettlementMode() == DailySettlement.Mode.DEVICE ? workstation.getDeviceId() : null)
                .closed(false)
                .build();

        dailySettlementRepository.save(dailySettlement);

        return dailySettlement;
    }

    @Override
    public DailySettlement addNewClosedSettlementByTime(LocalDateTime localDateTime, Store store, Workstation workstation) {

        DailySettlement dailySettlement = DailySettlement.builder()
                .dateTime(localDateTime)
                .guid(UUID.randomUUID().toString())
                .storeGuid(store.getGuid())
                .deviceId(getDailySettlementMode() == DailySettlement.Mode.DEVICE ? workstation.getDeviceId() : null)
                .closed(true)
                .build();

        try {
//            reportRemoteInterface.executeDailySettlement(DailySettlementHelper.builder().startDate(getLastClosedDailySettlementByStoreBeforeDateTime(store.getGuid(), localDateTime).getDateTime()).endDate(localDateTime).storeGuid(store.getGuid()).build());
            dailySettlementRepository.save(dailySettlement);
        } catch (Exception e) {
            dailySettlementRepository.save(dailySettlement);
        }

        return dailySettlement;
    }

    @Override
    public DailySettlement addNewClosedSettlement(Store store, Workstation workstation) {
        if (!isNewDailySettlementAllowed(store, workstation)) {
            return findLastCreatedDailySettlement(store, workstation);
        }

        DailySettlement dailySettlement = DailySettlement.builder()
                .guid(UUID.randomUUID().toString())
                .dateTime(LocalDateTime.now())
                .storeGuid(store.getGuid())
                .deviceId(getDailySettlementMode() == DailySettlement.Mode.DEVICE ? workstation.getDeviceId() : null)
                .build();

        dailySettlementRepository.save(dailySettlement);

        closeSettlement(dailySettlement.getGuid());

        return dailySettlement;
    }

    @Override
    public void addNewClosedSettlementForStoreDevices(Store store) {
        if (store == null) {
            return;
        }

        switch (getDailySettlementMode()) {
            case DEVICE:
                List<Workstation> workstations = socketRemoteInterface.findByStoreGuid(store.getGuid());
                workstations.forEach(workstation -> {
                    if (isNewDailySettlementAllowed(store, workstation)) {
//                        reportRemoteInterface.executeDailySettlement(DailySettlementHelper.builder().startDate(getLastClosedDailySettlementByStore(store.getGuid()).getDateTime()).endDate(LocalDateTime.now()).deviceId(workstation.getDeviceId()).storeGuid(store.getGuid()).build());
                        addNewClosedSettlement(store, workstation);
                    }
                });

                break;
            case GLOBAL:
//                reportRemoteInterface.executeDailySettlement(DailySettlementHelper.builder().startDate(getLastClosedDailySettlementByStore(store.getGuid()).getDateTime()).endDate(LocalDateTime.now()).storeGuid(store.getGuid()).build());
                addNewClosedSettlement(store, null);
                break;
        }


    }

    @Override
    public void addNewClosedSettlementByTimeForStoreDevices(LocalDateTime localDateTime, Store store) {
        if (store == null) {
            return;
        }

        switch (getDailySettlementMode()) {
            case DEVICE:
                List<Workstation> workstations = socketRemoteInterface.findByStoreGuid(store.getGuid());
                workstations.forEach(workstation -> {
                    addNewClosedSettlementByTime(localDateTime, store, workstation);
                });

                break;
            case GLOBAL:
                reportRemoteInterface.executeDailySettlement(DailySettlementHelper.builder().startDate(getLastClosedDailySettlementByStore(store.getGuid()).getDateTime()).endDate(LocalDateTime.now()).storeGuid(store.getGuid()).build());
                addNewClosedSettlementByTime(localDateTime, store, null);
                break;
        }


    }

    @Override
    public boolean closeSettlement(String guid) {
        if (guid == null || guid.isEmpty()) {
            return false;
        }

        DailySettlement dailySettlement = dailySettlementRepository.findByGuid(guid);

        if (dailySettlement == null) {
            return false;
        }

        dailySettlement.setClosed(true);

        DailySettlement lastDailySettlement = getLastClosedDailySettlementByStore(dailySettlement.getStoreGuid());
        LocalDateTime startDate = LocalDateTime.parse("1900-01-01T00:00:00");
        if(lastDailySettlement != null ){
            startDate = lastDailySettlement.getDateTime();
        }

        // CASHBOOK:  Auf Wechselgeldstand reduzieren
        Store store = dataRemoteInterface.getStoreByGuid(dailySettlement.getStoreGuid());
        if(cashbookEntryService.getAdjustmentCashbookEntry(store) != null){
            //Find last User of Day by Store

            String lastCashierGuid = invoiceService.findByCreationDateBetween(startDate, LocalDateTime.now()).parallelStream().
                    filter(x -> x.getStore().getGuid().equals(store.getGuid())).max((x, y) -> x.getCreationDate().compareTo(y.getCreationDate())).get().getCashier().getUserGuid();

            cashbookEntryService.adjustCashStatus(store, lastCashierGuid);
        }

        // MONATSBELEG

        if(LocalDateTime.now().minusHours(6).toLocalDate().getDayOfMonth() == startDate.minusHours(6).toLocalDate().lengthOfMonth()){
            try{
                signatureRemoteInterface.createZeroInvoice(store.getGuid(), unipos.common.remote.pos.model.Invoice.SignatureInvoiceType.MONATS);

                int month = LocalDateTime.now().minusHours(6).getMonthValue();
                if(month == 2 || month == 5 || month == 8 || month == 11){
                    // DEP SENDEN
                    signatureRemoteInterface.sendDep(store.getGuid());
                }
            }
            catch(Exception ex){
                // bad luck :-(
            }
        }

        reportRemoteInterface.executeDailySettlement(DailySettlementHelper.builder().startDate(startDate).endDate(LocalDateTime.now()).storeGuid(dailySettlement.getStoreGuid()).build());
        dailySettlementRepository.save(dailySettlement);

        return store.isPrintDailySettlementReport();
    }


    @Override
    public void deleteSettlement(String guid) {
        if (guid == null || guid.isEmpty()) {
            return;
        }
        DailySettlement dailySettlement = dailySettlementRepository.findByGuid(guid);

        if (dailySettlement != null && !dailySettlement.isClosed()) {
            dailySettlementRepository.delete(dailySettlement);
        }
    }

    @Override
    public boolean closeMonthlySettlement(String guid) {
        if (guid == null || guid.isEmpty()) {
            return false;
        }

        MonthlySettlement monthlySettlement = monthlySettlementRepository.findByGuid(guid);

        if (monthlySettlement == null) {
            return false;
        }

        monthlySettlement.setClosed(true);

        MonthlySettlement lastMontlySettlement = getLastClosedMonthlySettlementByStore(monthlySettlement.getStoreGuid());
        LocalDateTime startDate = LocalDateTime.parse("1900-01-01T00:00:00");
        if(lastMontlySettlement != null ){
            startDate = lastMontlySettlement.getDateTime();
        }

        monthlySettlementRepository.save(monthlySettlement);

        return false;
    }


    @Override
    public void deleteMonthlySettlement(String guid) {
        if (guid == null || guid.isEmpty()) {
            return;
        }
        MonthlySettlement monthlySettlement = monthlySettlementRepository.findByGuid(guid);

        if (monthlySettlement != null && !monthlySettlement.isClosed()) {
            monthlySettlementRepository.delete(monthlySettlement);
        }
    }

    @Override
    public void closeSettlement(DailySettlement dailySettlement) {
        if (dailySettlement.getGuid() == null || dailySettlement.getGuid().isEmpty()) {
            return;
        }
        DailySettlement dailySettlement1 = dailySettlementRepository.findByGuid(dailySettlement.getGuid());

        if (dailySettlement1 == null) {
            return;
        }
        dailySettlement1.setClosed(true);
        reportRemoteInterface.executeDailySettlement(DailySettlementHelper.builder().startDate(getLastClosedDailySettlementByStore(dailySettlement.getStoreGuid()).getDateTime()).endDate(LocalDateTime.now()).storeGuid(dailySettlement1.getStoreGuid()).build());
        dailySettlementRepository.save(dailySettlement1);
    }

    @Override
    public boolean isOpen(String guid) {
        if (guid == null || guid.isEmpty()) {
            return false;
        }

        DailySettlement dailySettlement = dailySettlementRepository.findByGuid(guid);
        return dailySettlement != null && !dailySettlement.isClosed();

    }

    @Override
    public boolean isOpen(DailySettlement dailySettlement) {
        if (dailySettlement == null || dailySettlement.getGuid() == null || dailySettlement.getGuid().isEmpty()) {
            return false;
        }

        DailySettlement dailySettlement1 = dailySettlementRepository.findByGuid(dailySettlement.getGuid());

        return dailySettlement1 != null && !dailySettlement1.isClosed();

    }

    @Override
    public boolean isNewDailySettlementAllowed(Store store, Workstation workstation) {
        DailySettlement dailySettlement = findLastCreatedDailySettlement(store, workstation);

        if (dailySettlement == null) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime bsDateTime = !now.toLocalTime().isBefore(store.getCloseHour().toLocalTime()) ? LocalDate.now().atTime(store.getCloseHour().toLocalTime()) : LocalDate.now().minusDays(1L).atTime(store.getCloseHour().toLocalTime());

        return !bsDateTime.isBefore(dailySettlement.getDateTime());


    }

    @Override
    public List<DailySettlement> checkDailySettlementChain(LocalDate startDate, LocalDate endDate) {
        List<DailySettlement> dailySettlementChain = dailySettlementRepository.findByDateTimeBetween(startDate.minusDays(1).atStartOfDay(), endDate.plusDays(1).atTime(LocalTime.MAX));

        List<Store> stores = dataRemoteInterface.getStoresByPlacedController();

        long daysBetween = startDate.until(endDate, ChronoUnit.DAYS) + 1;

        for (Store store : stores) {
            LocalDateTime firstCompareDate = startDate.minusDays(1L).atTime(store.getCloseHour().toLocalTime()).plusMinutes(1);

            for (int i = 0; i < daysBetween; i++) {
                LocalDateTime secondCompareDate = firstCompareDate.plusDays(1L);

                final LocalDateTime finalFirstCompareDate = firstCompareDate;
                if (dailySettlementChain.stream().filter(dailySettlement -> dailySettlement.getDateTime().isAfter(finalFirstCompareDate) && dailySettlement.getDateTime().isBefore(secondCompareDate) && dailySettlement.getStoreGuid().equals(store.getGuid())).count() == 0) {
                    LocalDateTime bsDateTime = firstCompareDate.toLocalDate().atTime(store.getCloseHour().toLocalTime()).minusSeconds(5);
                    if (bsDateTime.isBefore(firstCompareDate)) {
                        bsDateTime = bsDateTime.plusDays(1L);
                    }
                    addNewClosedSettlementByTimeForStoreDevices(bsDateTime, store);
                }

                firstCompareDate = firstCompareDate.plusDays(1L);
            }
        }


        return dailySettlementChain;
    }

    private DailySettlement findLastCreatedDailySettlement(Store store, Workstation workstation) {
        DailySettlement dailySettlement = null;
        switch (getDailySettlementMode()) {
            case GLOBAL:
                dailySettlement = dailySettlementRepository.findLastByStoreGuidOrderByDateTimeDesc(store.getGuid());
                break;
            case DEVICE:
                dailySettlement = dailySettlementRepository.findLastByStoreGuidAndDeviceIdOrderByDateTimeDesc(store.getGuid(), workstation.getDeviceId());
                break;
        }

        return dailySettlement;
    }


    @Override
    public List<DailySettlement> findAll() {
        return dailySettlementRepository.findAll();
    }

    @Override
    public List<DailySettlement> findAllByStore(Store store) {
        return dailySettlementRepository.findByStoreGuid(store.getGuid());
    }

    @Override
    public DailySettlement getLastCreatedDailySettlementByStore(Store store) {
        return dailySettlementRepository.findLastByStoreGuidOrderByDateTimeDesc(store.getGuid());
    }

    @Override
    public DailySettlement getLastClosedDailySettlementByStore(String storeGuid) {
        List<DailySettlement> dailySettlements = dailySettlementRepository.findByStoreGuid(storeGuid);
        if (dailySettlements == null) {
            return null;
        }
        dailySettlements = dailySettlements.stream().filter(x -> x.isClosed() == true).collect(Collectors.toList());
        Comparator<DailySettlement> byDateTime = Collections.reverseOrder(Comparator.comparing(DailySettlement::getDateTime));
        return dailySettlements.stream().sorted(byDateTime).findFirst().orElse(null);
    }

    @Override
    public MonthlySettlement getLastClosedMonthlySettlementByStore(String storeGuid) {
        List<MonthlySettlement> monthlySettlements = monthlySettlementRepository.findByStoreGuid(storeGuid);
        if (monthlySettlements == null) {
            return null;
        }
        monthlySettlements = monthlySettlements.stream().filter(x -> x.isClosed() == true).collect(Collectors.toList());
        Comparator<MonthlySettlement> byDateTime = Collections.reverseOrder(Comparator.comparing(MonthlySettlement::getDateTime));
        return monthlySettlements.stream().sorted(byDateTime).findFirst().orElse(null);
    }

    @Override
    public DailySettlement getLastClosedDailySettlementByStoreBeforeDateTime(String storeGuid, LocalDateTime localDateTime) {
        List<DailySettlement> dailySettlements = dailySettlementRepository.findByStoreGuid(storeGuid);
        if (dailySettlements == null) {
            return null;
        }
        Comparator<DailySettlement> byDateTime = Collections.reverseOrder(Comparator.comparing(DailySettlement::getDateTime));
        return dailySettlements.stream().sorted(byDateTime).filter(x -> x.getDateTime().isBefore(localDateTime)).findFirst().orElse(null);
    }

    @Override
    public List<DailySettlement> findByStoreGuidAndDateTimeBetween(String storeGuid, LocalDateTime startDate, LocalDateTime endDate) {
        return dailySettlementRepository.findByStoreGuidAndDateTimeBetween(storeGuid, startDate, endDate);
    }

    private DailySettlement.Mode getDailySettlementMode() {
        switch (environment.getProperty("dailySettlementMode")) {
            case "device":
                return DailySettlement.Mode.DEVICE;
            default:
                return DailySettlement.Mode.GLOBAL;
        }
    }
}
