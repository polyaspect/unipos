package unipos.signature.config;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import unipos.authChecker.interceptors.AuthenticationManager;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by ggradnig on 2015-04-28
 */

public class DBInitializer
{
    private static Logger logger = LoggerFactory.getLogger(DBInitializer.class);

    @Autowired
    AuthenticationManager authenticationManager;

    private ExecutorService executorService;

    @PostConstruct
    public void init()
    {
        try {
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
}