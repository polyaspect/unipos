package unipos.socket.config;

import com.mongodb.Mongo;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import unipos.authChecker.interceptors.AuthenticationManager;
import unipos.socket.components.workstation.Workstation;
import unipos.socket.components.workstation.WorkstationRepository;
import unipos.socket.components.workstation.WorkstationService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ggradnig on 2015-04-28
 */

public class DBInitializer
{
    private static Logger logger = LoggerFactory.getLogger(DBInitializer.class);

    @Autowired
    private WorkstationService workstationService;
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
    }
}