package unipos.signature.config;

import com.github.fakemongo.Fongo;
import com.mongodb.Mongo;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by ggradnig on 2015-05-02
 */

@Configuration
@Import(FixtureConfiguration.class)
@PropertySource("classpath:application.properties")
@EnableMongoRepositories(basePackages = "unipos.signature.components")
public class MongoTestConfiguration extends AbstractMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        return "unipos-signature";
    }

    @Override
    @Bean
    public Mongo mongo() {
        // uses fongo for in-memory tests
        return new Fongo("db").getMongo();
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        return new MongoTemplate(mongo(), getDatabaseName());
    }
}
