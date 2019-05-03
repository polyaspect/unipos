package unipos.core.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
@EnableMongoRepositories(basePackages = "unipos.core.components")
@Import(MongoConfigurationStores.class)
public class MongoConfiguration extends AbstractMongoConfiguration
{
    @Override
    public String getDatabaseName() {
        return "unipos-core";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("127.0.0.1");
    }
}
