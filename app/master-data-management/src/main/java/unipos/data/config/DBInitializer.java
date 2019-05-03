package unipos.data.config;

import com.mongodb.Mongo;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import unipos.authChecker.interceptors.AuthenticationManager;
import unipos.data.components.category.CategoryService;
import unipos.data.components.company.CompanyService;
import unipos.data.components.company.StoreService;
import unipos.data.components.company.model.Company;
import unipos.data.components.paymentMethod.PaymentMethodService;
import unipos.data.components.product.ProductService;
import unipos.data.components.productLog.ProductLogRepository;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateCategory;
import unipos.data.components.taxRates.TaxRateRepository;
import unipos.data.components.taxRates.TaxRateService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ggradnig on 2015-04-28
 */

public class DBInitializer
{
    private static Logger logger = LoggerFactory.getLogger(DBInitializer.class);

    @Autowired
    StoreService storeService;
    @Autowired
    Mongo mongo;
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    CompanyService companyService;
    @Autowired
    TaxRateRepository taxRateRepository;

    private ExecutorService executorService;

    @PostConstruct
    public void init()
    {
        try {
            initDatabase();
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
                        logger.error("error: ", e);
                    }
                }
            });
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destroyMongoDbConnection() {
        mongo.close();
    }

    @PreDestroy
    public void beanDestroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    void initDatabase()
    {
        Company serviceCompany = companyService.findByName("Service");
        if(serviceCompany == null){
            serviceCompany = new Company();
            serviceCompany.setGuid("00000000-5cd3-4066-8775-8709e13da9bf");
            serviceCompany.setCommercialRegisterNumber("service");
            serviceCompany.setName("Service");
            serviceCompany.setUid("service");

            companyService.saveCompany(serviceCompany);
        }

        TaxRate taxRate20 = taxRateRepository.findFirstByPercentage(20);
        if(taxRate20 == null){
            taxRate20 = new TaxRate();
            taxRate20.setName("20%");
            taxRate20.setDescription("Normalsteuersatz");
            taxRate20.setGuid("58169f93-07e7-4b74-a538-b8c7c2920b76");
            taxRate20.setPercentage(20);
            taxRate20.setTaxRateCategory(TaxRateCategory.NORMAL);

            taxRateRepository.save(taxRate20);
        }

        TaxRate taxRate10 = taxRateRepository.findFirstByPercentage(10);
        if(taxRate10 == null){
            taxRate10 = new TaxRate();
            taxRate10.setName("10%");
            taxRate10.setDescription("Ermäßigter Steuersatz");
            taxRate10.setGuid("7b1d365a-1451-4b8f-aea7-f61db5bde664");
            taxRate10.setPercentage(10);
            taxRate10.setTaxRateCategory(TaxRateCategory.DISCOUNT);

            taxRateRepository.save(taxRate10);
        }

        TaxRate taxRate0 = taxRateRepository.findFirstByPercentage(0);
        if(taxRate0 == null){
            taxRate0 = new TaxRate();
            taxRate0.setName("0%");
            taxRate0.setDescription("Normalsteuersatz");
            taxRate0.setGuid("11b75e9b-5bce-45c4-ae39-a638a8d5a07a");
            taxRate0.setPercentage(0);
            taxRate0.setTaxRateCategory(TaxRateCategory.NONE);

            taxRateRepository.save(taxRate0);
        }
    }
}