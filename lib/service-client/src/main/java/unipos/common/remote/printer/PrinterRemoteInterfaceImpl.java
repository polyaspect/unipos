package unipos.common.remote.printer;

import org.json.HTTP;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.GSonHolder;
import unipos.common.container.RequestHandler;
import unipos.common.container.UrlContainer;
import unipos.common.remote.printer.model.ThermalPrinterLine;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.List;

/**
 * Created by Dominik on 03.12.2015.
 */

@Service
public class PrinterRemoteInterfaceImpl implements PrinterRemoteInterface {

    @Autowired
    RestTemplate restTemplate;

    @Override
    public void printUniposXmlFile(File uniposXmlFile, HttpServletRequest request) {
        String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");

        if(deviceToken == null || deviceToken.isEmpty() || authToken == null | authToken.isEmpty()) {
            return;
        }

        LinkedMultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
        mvm.add("file", new FileSystemResource(uniposXmlFile.getPath()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "AuthToken=" + authToken+"; DeviceToken="+deviceToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(mvm, headers);

        restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.PRINTER_SOCKET, HttpMethod.POST, entity, Void.class);
    }

    @Override
    public String getInvoiceAsText(File uniposXmlFile) {
        LinkedMultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
        mvm.add("file", new FileSystemResource(uniposXmlFile.getPath()));
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        String invoiceAsText = restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.PRINTER_GETINVOICETEXT, mvm, String.class);

        return invoiceAsText;
    }

    @Override
    public void reprintInvoice(File uniposXmlFile, HttpServletRequest request) throws UniposRemoteInterfaceException {

        String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");

        if(deviceToken == null || deviceToken.isEmpty() || authToken == null | authToken.isEmpty()) {
            return;
        }

        LinkedMultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
        mvm.add("file", new FileSystemResource(uniposXmlFile.getPath()));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "AuthToken=" + authToken+"; DeviceToken="+deviceToken);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<Object> httpRequest = new HttpEntity<>(mvm, headers);

        ResponseEntity responseEntity =  restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.PRINTER_REPRINT_INVOICE, HttpMethod.POST, httpRequest, Void.class);
        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new UniposRemoteInterfaceException("Unable to reprint Invoice.");
        }
    }

    public void printText(List<String> lines, HttpServletRequest request) {
        String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");

        if(deviceToken == null || deviceToken.isEmpty() || authToken == null | authToken.isEmpty()) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "AuthToken=" + authToken+"; DeviceToken="+deviceToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(GSonHolder.serializeDateGson().toJson(lines), headers);

        restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.PRINTER_PRINTTEXT, HttpMethod.POST, httpEntity, Void.class);
    }

    public void printTextWithEscSeq(List<ThermalPrinterLine> escapeSequences, HttpServletRequest request) {
//        LinkedMultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
//        mvm.add("lines", Arrays.toString(lines.toArray()));
//        mvm.add("escSequences", escapeSequences.toArray());

//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        HttpEntity request = new HttpEntity<>(mvm, headers);
        String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");

        if(deviceToken == null || deviceToken.isEmpty() || authToken == null | authToken.isEmpty()) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add("Cookie", "AuthToken=" + authToken+"; DeviceToken="+deviceToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> httpEntity = new HttpEntity<>(GSonHolder.serializeDateGson().toJson(escapeSequences), headers);

        restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.PRINTER_PRINTTEXT_ESC, HttpMethod.POST, httpEntity, Void.class);
    }

    @Override
    public void printRevertedInvoice(File uniposXmlFile, Long invoiceNumber, HttpServletRequest request) {
        String deviceToken = RequestHandler.getCookieValue(request, "DeviceToken");
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");

        if(deviceToken == null || deviceToken.isEmpty() || authToken == null | authToken.isEmpty()) {
            return;
        }

        LinkedMultiValueMap<String, Object> mvm = new LinkedMultiValueMap<>();
        mvm.add("file", new FileSystemResource(uniposXmlFile.getPath()));
        mvm.add("reversedInvoiceNumber", invoiceNumber);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Cookie", "AuthToken=" + authToken+"; DeviceToken="+deviceToken);

        HttpEntity<MultiValueMap<String, Object>> httpRequest = new HttpEntity<>(mvm, headers);

        ResponseEntity responseEntity =  restTemplate.exchange(UrlContainer.BASEURL + UrlContainer.PRINTER_PRINT_REVERTED_INVOICE, HttpMethod.POST, httpRequest, Void.class);
        if(responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new UniposRemoteInterfaceException("Unable to reprint Invoice.");
        }
    }

    @Override
    public String getCurrentVersion() {
        return restTemplate.getForObject(UrlContainer.BASEURL + UrlContainer.PRINTER_CURRENT_VERSION, String.class);
    }
}
