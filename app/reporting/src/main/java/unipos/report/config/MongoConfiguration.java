package unipos.report.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@ComponentScan(basePackages = {"unipos.report.config"})
@PropertySource(value = { "classpath:application.properties" })
@EnableMongoRepositories(basePackages = "unipos.report.components")
public class MongoConfiguration extends AbstractMongoConfiguration
{
    @Override
    public String getDatabaseName() {
        return "unipos-report";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("127.0.0.1");
    }
}
