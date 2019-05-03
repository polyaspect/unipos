package unipos.core.components.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Created by Gradnig on 07.11.2015.
 */

@RestController
@RequestMapping(value = "/logs", produces = MediaType.APPLICATION_JSON_VALUE)
@Component
public class LogController {
    @Autowired
    private LogService logService;

    @RequestMapping(value = "/addLog", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void addLog(@RequestBody Log log) {
        log.setReceivedDateTime(LocalDateTime.now());
        logService.addLog(log);
    }

    @RequestMapping(value = "/showLogs", method = RequestMethod.GET)
    public List<Log> showLogs(@RequestParam(required = false)
                                      Integer size,
                              @RequestParam(required = false)
                                      Integer page,
                              @RequestParam(required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                      LocalDateTime startDate,
                              @RequestParam(required = false)
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                          LocalDateTime endDate) {
        if (size == null || page == null || startDate == null || endDate == null) {
            //By default just show the logs of the last week. NOT ALL JOYCE!!!
            startDate = LocalDateTime.now().minusWeeks(1).toLocalDate().atStartOfDay();
            endDate = LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay();
        }

        PageRequest pageRequest = new PageRequest(page, size, new Sort(new Sort.Order(Sort.Direction.DESC, "dateTime")));
        return logService.findByDateBetween(startDate.toLocalDate().atStartOfDay(), endDate.plusDays(1).toLocalDate().atStartOfDay(), pageRequest);
    }
}
