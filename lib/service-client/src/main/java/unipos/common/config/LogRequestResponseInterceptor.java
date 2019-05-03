package unipos.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

/**
 * Created by dominik on 30.01.17.
 */
public class LogRequestResponseInterceptor implements ClientHttpRequestInterceptor {

    @Autowired
    LogRemoteInterface logRemoteInterface;

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);

        if (!response.getStatusCode().is2xxSuccessful()) {
            LogDto logDtoRequest = LogDto.builder()
                    .dateTime(LocalDateTime.now())
                    .level(LogDto.Level.ERROR)
                    .message("RestTemplate Error - Request")
                    .build();
            logDtoRequest.addParameter("URL", request.getURI().toString());
            logDtoRequest.addParameter("Method", request.getMethod().name());
            logDtoRequest.addParameter("Headers", request.getHeaders().toSingleValueMap().entrySet().stream().map(entry -> entry.getKey() + " - " + entry.getValue()).collect(Collectors.joining(", ")));
            logDtoRequest.addParameter("Request-Body", getRequestBody(body));
            logDtoRequest.addParameter("Status Code", String.valueOf(response.getStatusCode().value()));
            logDtoRequest.addParameter("Response-Body",getBodyString(response));
            logRemoteInterface.log(logDtoRequest);
        }

        return response;
    }

    private String getRequestBody(byte[] body) throws UnsupportedEncodingException {
        if (body != null && body.length > 0) {
            return (new String(body, "UTF-8"));
        } else {
            return null;
        }
    }

    private String getBodyString(ClientHttpResponse response) {
        try {
            if (response != null && response.getBody() != null) {// &&
                // isReadableResponse(response))
                // {
                StringBuilder inputStringBuilder = new StringBuilder();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getBody(), "UTF-8"));
                String line = bufferedReader.readLine();
                while (line != null) {
                    inputStringBuilder.append(line);
                    inputStringBuilder.append('\n');
                    line = bufferedReader.readLine();
                }
                return inputStringBuilder.toString();
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

}
