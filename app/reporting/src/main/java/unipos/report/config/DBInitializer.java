package unipos.report.config;

import com.mongodb.Mongo;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unipos.authChecker.interceptors.AuthenticationManager;
import unipos.report.components.shared.ReportCompiledSingleton;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.File;
import java.net.URISyntaxException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ggradnig on 2015-04-28
 */

@Component
public class DBInitializer
{
    private static Logger logger = LoggerFactory.getLogger(DBInitializer.class);

    private ReportCompiledSingleton reportCompiledSingleton = ReportCompiledSingleton.getInstance();

    @Autowired
    Mongo mongo;

    @Autowired
    AuthenticationManager authenticationManager;

    private ExecutorService executorService;

    @PostConstruct
    public void init()
    {
        try {
            initDatabase();
            initReports();
            BasicThreadFactory factory = new BasicThreadFactory.Builder()
                    .namingPattern("myspringbean-thread-%d").build();

            executorService =  Executors.newSingleThreadExecutor(factory);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // do something
                        authenticationManager.propagateAvailablePermissions();
                    } catch (Exception e) {
                    }
                }
            });
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PreDestroy
    public void beanDestroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    @PreDestroy
    public void destoryMongoDbConnection() {
        mongo.close();
    }

    private void initReports() throws JRException, URISyntaxException {
        reportCompiledSingleton.setJr((JasperReport) JRLoader.loadObject(new File(this.getClass().getResource("/Blank_A4.jasper").getFile())));//JasperCompileManager.compileReport(masterInputStream);
        reportCompiledSingleton.setSubOrderItemsCompiled((JasperReport) JRLoader.loadObject(new File(this.getClass().getResource("/sub_OrderItems.jasper").getFile())));//JasperCompileManager.compileReport(subOrderItemsInputStream);
        reportCompiledSingleton.setSubPaymentCompiled((JasperReport) JRLoader.loadObject(new File(this.getClass().getResource("/sub_Payment.jasper").getFile())));//JasperCompileManager.compileReport(subPaymentItemsInputStream);
        reportCompiledSingleton.setSubTaxRatesCompiled((JasperReport) JRLoader.loadObject(new File(this.getClass().getResource("/sub_taxRates.jasper").getFile())));//JasperCompileManager.compileReport(subTaxRatesItemsInputStream);
    }

    void initDatabase()
    {
    }
}