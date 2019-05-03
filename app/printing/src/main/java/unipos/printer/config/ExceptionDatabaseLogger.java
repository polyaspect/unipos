package unipos.printer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * Created by domin on 19.02.2016.
 */

@ControllerAdvice
public class ExceptionDatabaseLogger {

    @Autowired
    LogRemoteInterface logRemoteInterface;

    @ExceptionHandler(Exception.class)
    public void handleException(Exception e) {
        LogDto logDto = null;
        logDto = LogDto.builder()
                .dateTime(LocalDateTime.now())
                .source("Default Exception Interceptor")
                .level(LogDto.Level.ERROR)
                .message(e.getMessage())
                .build();
        logDto.addExceptionParameters(e);

    }
}
