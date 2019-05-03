package unipos.pos.components.dailySettlement;

import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.socket.model.Workstation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by Dominik on 19.01.2016.
 */

/**
 * A Daily Settlement indicated the end of a Day (a business day)
 */
public interface DailySettlementService {

    /**
     * Add a new Settlement with the current DateTime.
     * The created Settlement is still open and not in a closed state.
     * @param store is the targetStore you make a DailySettlement
     * @return the new Settlement that was created now
     */
    DailySettlement addNewSettlement(Store store, Workstation workstation);
    MonthlySettlement addNewMonthlySettlement(Store store, Workstation workstation);

    /**
     * Add a new Settlement with the given LocalDateTime
     * The created Settlement is still open and not in a closed state.
     * @param localDateTime the DateTime of the Settlement
     * @param store is the targetStore you make a DailySettlement
     * @return the new Settlement that was created now
     */
    DailySettlement addNewSettlementByTime(LocalDateTime localDateTime, Store store, Workstation workstation);

    public DailySettlement addNewClosedSettlementByTime(LocalDateTime localDateTime, Store store, Workstation workstation);

    /**
     * Create a new DailySettlement with the LocalDate from now
     * The created DailySettlement is in the closed state and can't be deleted anymore.
     * @param store is the targetStore you make a DailySettlement
     * @return the already closed and persisted Entity
     */
    DailySettlement addNewClosedSettlement(Store store, Workstation workstation);

    public void addNewClosedSettlementForStoreDevices(Store store);

    public void addNewClosedSettlementByTimeForStoreDevices(LocalDateTime localDateTime, Store store);

    /**
     * Close a Settlement by its guid.
     * @param guid the guid of the Settlement you want to close
     */
    boolean closeSettlement(String guid);

    /**
     * Deletes an DailySettlement, if it is not closed, from the given GUID
     * @param guid the guid of the Settlement you want to delete
     */
    void deleteSettlement(String guid);

    boolean closeMonthlySettlement(String guid);
    void deleteMonthlySettlement(String guid);

    /**
     * Close a Settlement by the domain-instance
     * @param dailySettlement the settlement instance you want to close
     */
    void closeSettlement(DailySettlement dailySettlement);

    /**
     * Check if the Settlement is still open
     * @param guid the guid of the DailySettlement
     * @return true if the Settlement is still open
     */
    boolean isOpen(String guid);

    /**
     * Check if the Settlement is still open
     * @param dailySettlement the DailySettlement instance you want to check
     * @return true if the Settlement is still open
     */
    boolean isOpen(DailySettlement dailySettlement);

    /**
     * Checks if there creation of a new DailySettlement is allowed at the moment. The policy is, that there's only on DailySettlement allowed per business day.
     * That means --> one DailySettlement between 03:00 - 2:59 if the store closeHour is 3:00
     * If there's already a DailySettlement created, it's now allowed to create a new one.
     * @param store is the the store instance, where the actual close hours are stored.
     * @return true if you are allowed to persist a new DailySettlement
     */
    boolean isNewDailySettlementAllowed(Store store, Workstation workstation);

    /**
     * Checks, if there exist valid DailySettlement between the given Date. If not so, create them and return the newly created DailySettlements
     * @param startDate the start-date you want to perform the validation task
     * @param endDate the end-date of you task
     */
    List<DailySettlement> checkDailySettlementChain(LocalDate startDate, LocalDate endDate);

    List<DailySettlement> findAll();

    List<DailySettlement> findAllByStore(Store store);

    DailySettlement getLastCreatedDailySettlementByStore(Store store);

    DailySettlement getLastClosedDailySettlementByStore(String storeGuid);
    MonthlySettlement getLastClosedMonthlySettlementByStore(String storeGuid);

    DailySettlement getLastClosedDailySettlementByStoreBeforeDateTime(String storeGuid, LocalDateTime localDateTime);

    List<DailySettlement> findByStoreGuidAndDateTimeBetween(String storeGuid, LocalDateTime startDate, LocalDateTime endDate);
}
