package unipos.report.components.shared;

import lombok.Data;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * Created by dominik on 25.09.15.
 */
@Data
public class ReportCompiledSingleton {

    JasperReport jr;
    JasperReport subOrderItemsCompiled;
    JasperReport subPaymentCompiled;
    JasperReport subTaxRatesCompiled;

    private ReportCompiledSingleton() {
    }

    private static class Holder {
        static final ReportCompiledSingleton INSTANCE = new ReportCompiledSingleton();
    }

    public static ReportCompiledSingleton getInstance() {
        return Holder.INSTANCE;
    }

}
