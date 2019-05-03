package unipos.pos.config;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "unipos.pos.signature", mongoTemplateRef = "signatureTemplate")
public class MongoConfigurationSignature
{
    protected String getDatabaseName() {
        return "unipos-signature";
    }

    @Bean
    public Mongo mongoSignatures() throws Exception {
        return new MongoClient("127.0.0.1");
    }

    @Bean
    public MongoTemplate signatureTemplate() throws Exception {
        return new MongoTemplate(mongoSignatures(), getDatabaseName());
    }
}
