package unipos.common.remote.pos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.RequestHandler;
import unipos.common.container.UrlContainer;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.model.CashierUsage;
import unipos.common.remote.pos.model.DailySettlement;
import unipos.common.remote.pos.model.Invoice;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Thomas on 29.11.2015.
 */
@Service
public class PosRemoteInterfaceImpl implements PosRemoteInterface {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public List<Invoice> findByCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate) {
        List<Invoice> invoices = new ArrayList<>(Arrays.asList(restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.POS_FIND_BY_CREATION_DATE_BETWEEN + "?startDate=" + startDate.toString() + "&endDate=" + endDate.toString(), Invoice[].class)));
        return invoices;
    }

    public List<Invoice> findByCreationDate(LocalDateTime date) {
        return new ArrayList<>(Arrays.asList(restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.POS_FIND_BY_CREATION_DATE + "?date=" + date.toString(), Invoice[].class)));
    }

    public void addNewAutoDailySettlementDateTime(Store store) {
        restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.POS_TIMETASK_ADD_DAILY_SETTLEMENT_DATETIME, store, Void.class);
    }

    public void deleteAutoDailySettlementOfStore(Store store) {
        MultiValueMap<String, String> mvm = new LinkedMultiValueMap<>();
        mvm.add("storeGuid", store.getGuid());

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

        HttpEntity httpEntity = new HttpEntity(mvm, headers);

        restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.POS_TIMETASK_DELETE_BY_STORE_GUID, httpEntity, Void.class);
    }

    @Override
    public boolean isCreationAllowed(HttpServletRequest request) {
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");
        String deviceId = RequestHandler.getCookieValue(request, "DeviceToken");

        if(authToken == null ||authToken.isEmpty()) {
            return false;
        }

        if(deviceId == null || deviceId.isEmpty()) {
            return false;
        }

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Cookie", "AuthToken=" + authToken + "; DeviceToken="+deviceId);
        HttpEntity requestEntity = new HttpEntity(null, requestHeaders);

        ResponseEntity<Boolean> result = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.POS_DAILYSETTLEMENT_IS_CREATION_ALLOWED, HttpMethod.GET, requestEntity, Boolean.class);
        return result.getBody();
    }

    @Override
    public boolean isActionAllowed(HttpServletRequest request) {
        return isCreationAllowed(request);
    }

    @Override
    public DailySettlement findLastCreatedDailySettlementByStoreGuid(String storeGuid) {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.POS_FIND_LAST_CLOSED_DAILY_SETTLEMENT_BY_STORE_GUID + "?storeGuid=" + storeGuid, DailySettlement.class);
    }

    @Override
    public DailySettlement findLastCreatedMonthlySettlementByStoreGuid(String storeGuid) {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.POS_FIND_LAST_MONTHLY_CLOSED_DAILY_SETTLEMENT_BY_STORE_GUID + "?storeGuid=" + storeGuid, DailySettlement.class);
    }

    @Override
    public List<DailySettlement> findByStoreGuidAndDateTimeBetween(String storeGuid, LocalDateTime startDate, LocalDateTime endDate) {
        return new ArrayList<>(Arrays.asList(restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.POS_FIND_DAILYSETTLEMENTS_BY_STOREGUID_AND_DATETIME_BETWEEN + "?storeGuid=" + storeGuid + "&startDate=" + startDate + "&endDate=" + endDate, DailySettlement[].class)));
    }

    @Override
    public BigDecimal getCurrendCashbookStatus(Store store) {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.POS_CASHBOOK_CURRENT_CASH_STATUS + "?storeGuid=" + store.getGuid(), BigDecimal.class);
    }

    @Override
    public String getCurrentVersion() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.POS_CURRENT_VERSION, String.class);
    }

    @Override
    public User getDefaultCashierId(CashierUsage usage) {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.POS_ORDER_GET_DEFAULT_CASHIER_ID + "?usage=" + usage, User.class);
    }

    @Override
    public Invoice externalInvoice(Invoice invoice, HttpServletRequest request) throws JsonProcessingException {
        String authToken = "";
        String deviceToken = "";

        try {
            authToken = RequestHandler.getAuthToken(request);
            deviceToken = RequestHandler.getDeviceToken(request);
        } catch (IllegalArgumentException e) {
        }

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", "AuthToken=" + authToken+"; DeviceToken="+deviceToken);
        headers.add("Content-Type", "application/json;charset=UTF-8");

        ObjectMapper objectMapper = new ObjectMapper();

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(invoice), headers);
        ResponseEntity<Invoice> responseEntity = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.POS_EXTERNAL_INVOICE, HttpMethod.POST, entity, Invoice.class);

        if(responseEntity.getStatusCode().is2xxSuccessful()) {
            return responseEntity.getBody();
        } else {
            return null;
        }
    }
}
