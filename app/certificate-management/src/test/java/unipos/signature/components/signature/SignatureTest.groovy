package unipos.signature.components.signature

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import unipos.signature.config.MongoTestConfiguration

/**
 * Created by domin on 31.12.2016.
 */
@ContextConfiguration(classes = MongoTestConfiguration.class)
class SignatureTest extends Specification {

    @Autowired
    SignatureFixture signatureFixture;

    def "Test Create Json"() {
        given:
        ObjectMapper mapper = new ObjectMapper()

        when:
        def result = mapper.writeValueAsString(signatureFixture.getInvoice1())
        println result

        then:
        result
    }
}
