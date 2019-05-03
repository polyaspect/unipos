package unipos.common.remote.report;

import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.GSonHolder;
import unipos.common.container.RequestHandler;
import unipos.common.container.UrlContainer;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.model.CashbookEntry;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ReversalInvoice;
import unipos.common.remote.printer.model.ThermalPrinterLine;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Created by Dominik on 03.12.2015.
 */

@Service
public class ReportRemoteInterfaceImpl implements ReportRemoteInterface {

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    DataRemoteInterface dataRemoteInterface;

    Gson gson = GSonHolder.serializeDateGson();

    @Override
    public void printInvoice(Invoice invoice, HttpServletRequest request) {

        String authToken;
        String deviceToken;

        try {
            authToken = RequestHandler.getAuthToken(request);
            deviceToken = RequestHandler.getDeviceToken(request);
        } catch (IllegalArgumentException e) {
            return;
        }

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Cookie", "AuthToken=" + authToken+"; DeviceToken="+deviceToken);
        headers.add("Content-Type", "application/json;charset=UTF-8");

        HttpEntity<String> entity = new HttpEntity<>(gson.toJson(invoice), headers);
        restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.REPORT_PRINT_INVOICE, HttpMethod.POST, entity, Void.class);
    }

    @Override
    public void revertInvoice(ReversalInvoice remoteReversalInvoice, HttpServletRequest request) {

        String authToken;
        String deviceToken;
        try {
            authToken = RequestHandler.getAuthToken(request);
            deviceToken = RequestHandler.getDeviceToken(request);
        } catch (IllegalArgumentException e) {
            return;
        }

        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json;charset=UTF-8");
        headers.add("Cookie", "AuthToken=" + authToken+"; DeviceToken="+deviceToken);

        HttpEntity<String> entity = new HttpEntity<>(gson.toJson(remoteReversalInvoice), headers);
        restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.REPORT_REVERT_INVOICE, HttpMethod.POST, entity, Void.class);
    }

//    @Override
//    public void executeDailySettlement(LocalDateTime startDateTime, LocalDateTime endDateTime, String storeGuid) {
//        MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
//        mvm.add("startDateTime", startDateTime);
//        mvm.add("endDateTime", endDateTime);
//        mvm.add("store", storeGuid);
//
//        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
//        headers.add("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
//
//        HttpEntity httpEntity = new HttpEntity(mvm, headers);
//        restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.REPORT_EXECUTE_DAILYSETTLEMENT, httpEntity, Void.class);
//    }

    @Override
    public void executeDailySettlement(DailySettlementHelper dailySettlementHelper) {
//        MultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
//        mvm.add("startDateTime", startDateTime);
//        mvm.add("endDateTime", endDateTime);
//        mvm.add("deviceId", deviceId);
//        mvm.add("store", storeGuid);
//
        MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
        headers.add("Content-Type", "application/json;charset=UTF-8");

        HttpEntity httpEntity = new HttpEntity(gson.toJson(dailySettlementHelper), headers);
        restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.REPORT_EXECUTE_DAILYSETTLEMENT, HttpMethod.POST, httpEntity, Void.class);
    }

    @Override
    public List<ThermalPrinterLine> previewFinancialReport(String authToken, String deviceToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "AuthToken="+authToken+"; DeviceToken=" + deviceToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(null,headers);

        return new ArrayList<>(Arrays.asList(restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.REPORT_PREVIEW_FINANCIAL_REPORT, HttpMethod.POST, httpEntity, ThermalPrinterLine[].class).getBody()));
    }

    @Override
    public boolean printFinancialReportOfCurrentDay(String authToken, String deviceToken){

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "AuthToken="+authToken+"; DeviceToken=" + deviceToken);

        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(authToken, deviceToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(null,headers);

        ResponseEntity<?> response = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.REPORT_PRINT_FINANCIAL_REPORT + "?storeGuid=" + store.getGuid() + "&date=" + LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME), HttpMethod.GET, httpEntity, void.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    @Override
    public String getCurrentVersion() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.REPORT_CURRENT_VERSION, String.class);
    }

    @Override
    public boolean printMonthlySettlementReport(String authToken, String deviceToken) {

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "AuthToken="+authToken+"; DeviceToken=" + deviceToken);

        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(authToken, deviceToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(null,headers);

        ResponseEntity<?> response = restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.REPORT_PRINT_MONTHLY_REPORT, HttpMethod.GET, httpEntity, void.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    @Override
    public void printCashbookEntry(CashbookEntry cashbookEntry, String authToken, String deviceToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "AuthToken="+authToken+"; DeviceToken=" + deviceToken);

        HttpEntity<Object> httpEntity = new HttpEntity<>(cashbookEntry, headers);

        restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.REPORT_PRINT_CASHBOOK_ENTRY, HttpMethod.POST, httpEntity, void.class);
    }
}
