package unipos.core.config;

import com.mongodb.Mongo;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import unipos.authChecker.interceptors.AuthenticationManager;
import unipos.core.components.artifact.Artifact;
import unipos.core.components.artifact.ArtifactService;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ggradnig on 2015-04-28
 */

@Component
public class DBInitializer
{
    private static Logger logger = LoggerFactory.getLogger(DBInitializer.class);

    @Autowired
    private ArtifactService artifactService;
    @Autowired
    @Qualifier("mongo")
    Mongo mongoCore;

    @Autowired
    @Qualifier("mongoStores")
    Mongo mongoStores;

    @Autowired
    private AuthenticationManager authenticationManager;

    private ExecutorService executorService;

    @PostConstruct
    public void init()
    {
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
    }

    @PreDestroy
    public void destoryMongoDbConnection() {
        mongoCore.close();
        mongoStores.close();
    }

    @PreDestroy
    public void beandestroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    void initDatabase()
    {

        logger.info("Initializing Database with sample data");
    }
}