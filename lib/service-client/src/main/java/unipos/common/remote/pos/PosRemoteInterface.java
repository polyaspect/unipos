package unipos.common.remote.pos;


import com.fasterxml.jackson.core.JsonProcessingException;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.model.CashierUsage;
import unipos.common.remote.pos.model.DailySettlement;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.report.DailySettlementHelper;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface PosRemoteInterface {

    List<Invoice> findByCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    List<Invoice> findByCreationDate (LocalDateTime date);
    void addNewAutoDailySettlementDateTime(Store store);
    public void deleteAutoDailySettlementOfStore(Store store);
    boolean isCreationAllowed(HttpServletRequest request);
    boolean isActionAllowed(HttpServletRequest request);
    DailySettlement findLastCreatedDailySettlementByStoreGuid(String storeGuid);
    DailySettlement findLastCreatedMonthlySettlementByStoreGuid(String storeGuid);
    List<DailySettlement> findByStoreGuidAndDateTimeBetween(String storeGuid, LocalDateTime startDate, LocalDateTime endDate);

    BigDecimal getCurrendCashbookStatus(Store store);

    String getCurrentVersion();

    User getDefaultCashierId(CashierUsage usage);

    Invoice externalInvoice(Invoice invoice, HttpServletRequest request) throws JsonProcessingException;
}