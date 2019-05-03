package unipos.printer.config;

import com.mongodb.Mongo;
import jssc.SerialPortException;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import unipos.authChecker.interceptors.AuthenticationManager;
import unipos.printer.components.entity.Entity;
import unipos.printer.components.entity.EntityService;
import unipos.printer.components.printer.ConnectionType;
import unipos.printer.components.printer.PrinterService;
import unipos.printer.components.printer.model.NetworkPrinter;
import unipos.printer.components.printer.model.Printer;
import unipos.printer.components.printer.model.SerialPrinter;
import unipos.printer.components.printer.model.UsbPrinter;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by ggradnig on 2015-04-28
 */

@Component
public class Initializer
{
    private static Logger logger = LoggerFactory.getLogger(Initializer.class);

    @Autowired
    private EntityService entityService;
    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    PrinterService printerService;
    @Autowired
    Mongo mongo;

    @Autowired
    AuthenticationManager authenticationManager;

    private ExecutorService executorService;

    @PostConstruct
    public void init()
    {
        try {
            //initDatabase();
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
        mongoTemplate.findAllAndRemove(Query.query(where("_id").ne("-1")), Entity.class);
        mongoTemplate.findAllAndRemove(Query.query(where("_id").ne("-1")), UsbPrinter.class);
        mongoTemplate.findAllAndRemove(Query.query(where("_id").ne("-1")), SerialPrinter.class);
        mongoTemplate.findAllAndRemove(Query.query(where("_id").ne("-1")), NetworkPrinter.class);

        /*Printer printer1 = NetworkPrinter.builder()
                .name("Thekendrucker")
                .typeName("Epson")
                .ipAddress("192.168.0.10")
                .connectionType(ConnectionType.NETWORK)
                .build();

        Printer printer2 = NetworkPrinter.builder()
                .name("Kassendrucker")
                .typeName("HP")
                .ipAddress("192.168.0.11")
                .connectionType(ConnectionType.NETWORK)
                .build();

        Printer networkPrinter = NetworkPrinter.builder()
                .name("NetworkPrinter@Home")
                .defaultPrinter(true)
                .ipAddress("192.168.69.168")
                .port(9100)
                .connectionType(ConnectionType.NETWORK)
                .build();*/
        UsbPrinter usbPrinter = new UsbPrinter();
        usbPrinter.setProductId((short)0x811e);
        usbPrinter.setVendorId((short)0x0fe6);
        usbPrinter.setDefaultPrinter(false);
        usbPrinter.setName("USB-Printer@Home");
        usbPrinter.setTypeName("Rongta RP-80");


        NetworkPrinter networkPrinter = new NetworkPrinter();
        networkPrinter.setName("NetworkPrinter@Home");
        networkPrinter.setTypeName("Rongta RP-80");
        networkPrinter.setDefaultPrinter(true);
        networkPrinter.setIpAddress("192.168.0.166");
        networkPrinter.setPort(9100);
        networkPrinter.setConnectionType(ConnectionType.NETWORK);

        SerialPrinter serialPrinter = new SerialPrinter();
        serialPrinter.setComPort("/dev/ttyUSB0");
        serialPrinter.setDefaultPrinter(false);
        serialPrinter.setName("SerialPrinter@Home");
        serialPrinter.setTypeName("Rongta RP-80");
        Arrays.asList(networkPrinter, serialPrinter, usbPrinter).forEach((x) -> mongoTemplate.save(x));
    }

    void initPrinter() throws SerialPortException, IOException {
        try {
            Printer printer = printerService.findDefaultPrinter();

            if (printer != null) {
                printer.openConnection();
                printer.initPrinter();
                printer.initLogo();
                printer.closeConnection();
            }
        } catch (IOException e) {
//            e.printStackTrace();
        }
    }
}