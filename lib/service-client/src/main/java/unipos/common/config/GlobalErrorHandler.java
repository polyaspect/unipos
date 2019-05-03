package unipos.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;

/**
 * Created by domin on 23.04.2016.
 */
@ControllerAdvice
public class GlobalErrorHandler {

    @Autowired
    LogRemoteInterface logRemoteInterface;

    @ExceptionHandler(Exception.class)
    public void handleGeneralErrors(HttpServletResponse response, Exception ex) throws IOException {

        String errorClass = Arrays.stream(ex.getStackTrace()).filter(stackTraceElement -> stackTraceElement.getClassName().contains("unipos")).map(x -> x.getClassName() + "#" +x.getMethodName() + ":" + x.getLineNumber() + " ( " + x.getFileName() + " )" ).findFirst().orElse(null);

        response.sendError(500, ex.getMessage());
        LogDto logDto = LogDto.builder()
                .dateTime(LocalDateTime.now())
                .level(LogDto.Level.ERROR)
                .message(ex.getMessage())
                .source(errorClass)
                .build();
        logDto.addExceptionParameters(ex);
        logRemoteInterface.log(logDto);
    }
}
