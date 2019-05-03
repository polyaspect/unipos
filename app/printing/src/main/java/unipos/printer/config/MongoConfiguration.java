package unipos.printer.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import unipos.printer.config.web.WebConfiguration;

@Configuration
@PropertySource(value = { "classpath:application.properties" })
@EnableMongoRepositories(basePackages = "unipos.printer.components")
@Import(WebConfiguration.class)
public class MongoConfiguration extends AbstractMongoConfiguration
{
    @Override
    public String getDatabaseName() {
        return "unipos-printer";
    }

    @Override
    @Bean
    public Mongo mongo() throws Exception {
        return new MongoClient("127.0.0.1");
    }

    @Bean
    public Initializer initializer() {
        return new Initializer();
    }

    @Bean
    public ExceptionDatabaseLogger exceptionDatabaseLogger() {
        return new ExceptionDatabaseLogger();
    }
}
