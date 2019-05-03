package unipos.report.components.thermalPrinterLineBuilder;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import unipos.common.remote.printer.PrinterRemoteInterface;
import unipos.common.remote.printer.model.ThermalPrinterLine;
import unipos.common.remote.printer.model.ThermalPrinterLineFormat;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by Thomas on 11.01.2016.
 */
@Slf4j
@RestController
@RequestMapping(value = "/thermalPrinterReports", produces = "application/json")
public class ThermalPrinterReportController {

    @Autowired
    PrinterRemoteInterface printerRemoteInterface;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void test(HttpServletRequest request) {
        try {
            log.info("--------------generate Test Thermal Printer Report--------------");
            List<ThermalPrinterLine> escapeSequences = new ArrayList<>();
            ThermalPrinterLineBuilder builder = new ThermalPrinterLineBuilder(48);


            builder.setAlignment(0, Alignment.CENTER);
            builder.setContent("es soll funktionieren");
            builder.addFormat(ThermalPrinterLineFormat.DOUBLE_HEIGHT);
            builder.addFormat(ThermalPrinterLineFormat.DOUBLE_WIDTH);
            escapeSequences.add(builder.getLine());


            builder.addColumn(1, 2);
            builder.setColumnContent(0, "ich glaube");
            builder.setAlignment(0, Alignment.LEFT);
            builder.setColumnContent(1, "dass es geht");
            builder.setAlignment(1, Alignment.RIGHT);
            builder.addFormat(ThermalPrinterLineFormat.DOUBLE_HEIGHT);
           escapeSequences.add(builder.getLine());

            printerRemoteInterface.printTextWithEscSeq(escapeSequences, request);

            log.info("--------------DONE--------------");
        } catch (Exception e) {
            log.error("An error occurred during Thermal Printer Test-Report generation");
            log.error(e.getMessage());
        }
    }
}
