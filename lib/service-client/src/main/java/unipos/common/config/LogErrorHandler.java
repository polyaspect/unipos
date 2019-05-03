package unipos.common.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.ErrorHandler;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import unipos.common.container.UrlContainer;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 11.01.2016.
 */
@Slf4j
public class LogErrorHandler implements ResponseErrorHandler {

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return !response.getStatusCode().is2xxSuccessful();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        Thread thread = Thread.currentThread();
        List<StackTraceElement> stackTraceElementList = Arrays.asList(thread.getStackTrace());
        LogDto logDto;
        /**
         * we dont't need to log this error, because is always happens, if there's no sync module
         */
        if(stackTraceElementList.stream().anyMatch(x -> (x.getClassName().contains("unipos.common.remote.sync.SyncRemoteInterfaceImpl") && x.getMethodName().equals("syncChanges")) || (x.getClassName().contains("org.springframework.web.client.RestTemplate")))) {
            return;
        }
        logDto = LogDto.builder()
                .source("RestTemplate Error")
                .message(response.getStatusText() + "_" + String.valueOf(response.getRawStatusCode()))
                .dateTime(LocalDateTime.now())
                .level(LogDto.Level.ERROR)
                .build();
        logDto.addParameter("HttpStatusCode", String.valueOf(response.getRawStatusCode()));
        logDto.addParameter("HttpStatusText", response.getStatusText());
        logDto.addParameter("ExceptionStackTrace", Arrays.asList(thread.getStackTrace()).stream().map(x -> x.toString()).collect(Collectors.joining("\n")));
        restTemplate.postForObject(UrlContainer.BASEURL + UrlContainer.CORE_ADD_LOG, logDto, Void.class);
    }
}
