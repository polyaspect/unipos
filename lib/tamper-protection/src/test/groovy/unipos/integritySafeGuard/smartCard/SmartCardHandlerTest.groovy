package unipos.integritySafeGuard.smartCard

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import unipos.integritySafeGuard.SmardCardException
import unipos.integritySafeGuard.config.TestConfiguration
import unipos.integritySafeGuard.smartcard.SmartCardHandler
import unipos.integritySafeGuard.smartcard.SmartCardImpl
import unipos.integritySafeGuard.smartcard.SmartCardMock

/**
 * Created by domin on 29.12.2016.
 */
@ContextConfiguration(classes = TestConfiguration.class)
class SmartCardHandlerTest extends Specification {

    @Autowired
    SmartCardHandler smartCardHandler

    def "Test getAttachedCards"() {
        when:
        def result = smartCardHandler.attachedCards

        then:
        notThrown Exception
        result.size() >= 0
    }

    def "Test getAttachedSmartCards"() {
        when:
        def result = smartCardHandler.attachedSmartCards

        then:
        notThrown Exception
        result.size() >= 0
    }

    def "Test getSmartCardBySerialNo"() {
        given:
        def smartCardSerial = "670776051039"

        when:
        def result = smartCardHandler.getSmartCardBySerialNo(smartCardSerial)

        then:
        notThrown Exception
        result
        result instanceof SmartCardImpl || result instanceof SmartCardMock
    }

    def "Test getSmartCardBySerialNo NoSmartCardPresent"() {
        given:
        def smartCardSerialNo = "unknown"

        when:
        def result = smartCardHandler.getSmartCardBySerialNo(smartCardSerialNo)

        then:
        result instanceof SmartCardMock
        result.certificateSerialNr.toUpperCase() == smartCardSerialNo.toUpperCase()
    }

    def "Test getSmartCardBySerialNo NullSerialNo"() {
        given:
        def smartCardSerialNo = null

        when:
        def sc = smartCardHandler.getSmartCardBySerialNo(smartCardSerialNo)

        then:
        thrown NullPointerException
    }
}
