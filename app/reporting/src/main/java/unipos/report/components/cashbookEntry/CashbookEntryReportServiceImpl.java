package unipos.report.components.cashbookEntry;

import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.pos.model.*;
import unipos.common.remote.printer.model.ThermalPrinterLine;
import unipos.common.remote.printer.model.ThermalPrinterLineFormat;
import unipos.report.components.financialReport.FinancialReport;
import unipos.report.components.financialReportDay.FinancialReportDay;
import unipos.report.components.shared.helper.TaxHelper;
import unipos.report.components.thermalPrinterLineBuilder.Alignment;
import unipos.report.components.thermalPrinterLineBuilder.ThermalPrinterLineBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Thomas on 16.01.2016.
 */
@Service
public class CashbookEntryReportServiceImpl implements CashbookEntryReportService {

    @Override
    public List<ThermalPrinterLine> generateCashbookEntryLines(CashbookEntry cashbookEntry) {
        List<ThermalPrinterLine> thermalPrinterLines = new ArrayList<>();
        ThermalPrinterLineBuilder thermalPrinterLineBuilder = new ThermalPrinterLineBuilder(48);
        thermalPrinterLineBuilder.setAlignment(0, Alignment.CENTER);
        if(cashbookEntry.getType() == CashbookEntry.Type.OUT){
            thermalPrinterLineBuilder.setContent("Ausgang");
        }else{
            thermalPrinterLineBuilder.setContent("Eingang");
        }
        thermalPrinterLineBuilder.addFormat(ThermalPrinterLineFormat.DOUBLE_HEIGHT);
        thermalPrinterLineBuilder.addFormat(ThermalPrinterLineFormat.DOUBLE_WIDTH);

        thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());

        thermalPrinterLineBuilder.getFormats().clear();

        thermalPrinterLineBuilder.addColumn(1, 1);
        thermalPrinterLineBuilder.setAlignment(0, Alignment.LEFT);
        thermalPrinterLineBuilder.setAlignment(1, Alignment.RIGHT);

        thermalPrinterLineBuilder.setColumnContent(0, "Betrag:");
        thermalPrinterLineBuilder.setColumnContent(1, cashbookEntry.getAmount().toString());
        thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());

        thermalPrinterLineBuilder.setColumnContent(0, "Grund:");
        thermalPrinterLineBuilder.setColumnContent(1,  cashbookEntry.getDescription().toString());
        thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());

        thermalPrinterLineBuilder.clearColumns();
        thermalPrinterLineBuilder.setContent("\n");
        thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());
        thermalPrinterLineBuilder.clearColumns();
        thermalPrinterLineBuilder.setAlignment(0, Alignment.RIGHT);
        thermalPrinterLineBuilder.setColumnContent(0, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());

        return thermalPrinterLines;
    }
}
