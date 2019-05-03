package unipos.design.config;

import com.mongodb.Mongo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * Created by ggradnig on 2015-04-28
 */

@Component
public class DBInitializer
{
    private static Logger logger = LoggerFactory.getLogger(DBInitializer.class);

    @Autowired
    Mongo mongo;

    @PostConstruct
    public void init()
    {
        try {
            initDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PreDestroy
    public void destoryMongoDbConnection() {
        mongo.close();
    }

    void initDatabase()
    {
    }
}