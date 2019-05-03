package unipos.auth.config;

import com.mongodb.Mongo;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import unipos.auth.components.right.RightRepository;
import unipos.auth.components.sequence.SequenceId;
import unipos.auth.components.sequence.SequenceRepository;
import unipos.auth.components.sequence.SequenceTable;
import unipos.auth.components.user.User;
import unipos.auth.components.user.UserRepository;
import unipos.auth.components.user.UserService;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPin;
import unipos.auth.components.user.mitarbeiteridPin.MitarbeiteridPinService;
import unipos.auth.components.user.usernamePassword.UsernamePassword;
import unipos.auth.components.user.usernamePassword.UsernamePasswordRepository;
import unipos.auth.components.user.usernamePassword.UsernamePasswordService;
import unipos.authChecker.interceptors.AuthenticationManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by ggradnig on 2015-04-28
 */

// ToDo: REVIEW STATE: NOT REVIEWED

@Component
public class DBInitializer
{
    private static Logger logger = LoggerFactory.getLogger(DBInitializer.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsernamePasswordService usernamePasswordService;

    @Autowired
    RightRepository rightRepository;
    @Autowired
    SequenceRepository sequenceRepository;
    @Autowired
    MongoTemplate mongoTemplate;
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

    void initDatabase()
    {
        User existingServiceUser = userRepository.findByNameAndSurname("Service", "User");
        if(existingServiceUser == null){
            User serviceUser = new User("Service", "User", true, sequenceRepository.getNextSequenceId(SequenceTable.USER), "00000000-5cd3-4066-8775-8709e13da9bf");
            serviceUser.setGuid("00000000-580a-46e2-9d2a-590fd403910e");

            UsernamePassword serviceUserCredentials = new UsernamePassword("service", null, serviceUser, UUID.randomUUID().toString());

            serviceUserCredentials.setPasswordHash("e625af7c24f16315741b38b0228b11959d29ceb682f42ed1aa9ffef6c5661e11");
            serviceUserCredentials.setPasswordSalt("bdba2d9a-a00e-43e8-bd17-5ec08d182cf6");

            logger.info("Creating Service User");

            userRepository.save(serviceUser);
            usernamePasswordService.saveCredentials(serviceUserCredentials);
        }
    }
}
