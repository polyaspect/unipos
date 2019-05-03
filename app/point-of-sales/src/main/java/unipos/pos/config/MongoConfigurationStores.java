package unipos.pos.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowire;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "unipos.pos.special", mongoTemplateRef = "storeTemplate")
public class MongoConfigurationStores
{
    protected String getDatabaseName() {
        return "unipos-data";
    }

    @Bean
    public Mongo mongoStores() throws Exception {
        return new MongoClient("127.0.0.1");
    }

    @Bean
    public MongoTemplate storeTemplate() throws Exception {
        return new MongoTemplate(mongoStores(), getDatabaseName());
    }
}
