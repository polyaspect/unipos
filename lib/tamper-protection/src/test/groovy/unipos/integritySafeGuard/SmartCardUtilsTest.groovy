package unipos.integritySafeGuard

import org.spockframework.util.Assert
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import unipos.integritySafeGuard.config.TestConfiguration
import unipos.integritySafeGuard.domain.RksSuite
import unipos.integritySafeGuard.domain.SignatureJob
import unipos.integritySafeGuard.domain.SignatureResult

import javax.crypto.spec.SecretKeySpec

/**
 * Created by domin on 30.09.2016.
 */
@ContextConfiguration(classes = TestConfiguration.class)
class SmartCardUtilsTest extends Specification{

    @Autowired
    SmartCardFixture smartCardFixture

    def "Test hashData"() {
        given:
        def bytes = [1,2,3,4,5,6] as byte[]

        when:
        def result = SmartCardUtils.hashData(bytes, smartCardFixture.signatureJob.rksSuite)

        then:
        result
        result.length == 256/8 as int
    }


    def "Test generateRandomAes256Key _ Valid"() {
        when:
        def key = SmartCardUtils.getRandomAes256Key()

        then:
        Assert.notNull(key)
    }

    def "Test getSecretKeyByCustomPw _ Valid"() {
        given:
        String customPw = "DasIstEinCustomPw"

        when:
        def key = SmartCardUtils.getSecretKeyByCustomPw(customPw)

        then:
        key
        key instanceof SecretKeySpec
        key.encoded.length == 32
    }

    def "Test getSecretKeyByCustomPw _ NullPW"() {
        given:
        String customPw = null

        when:
        def key = SmartCardUtils.getSecretKeyByCustomPw(customPw)

        then:
        thrown IllegalArgumentException
    }

    def "Test getSecretKeyByCustomPw _ EmptryPwString"() {
        given:
        String customPw = ""

        when:
        def key = SmartCardUtils.getSecretKeyByCustomPw(customPw)

        then:
        thrown IllegalArgumentException
    }

    def "Test encryptStandUmsatzZaehler _ Valid"() {
        given:
        def standUmsatzZaehler = 10000 as long

        when:
        def result = SmartCardUtils.encryptStandUmsatzZaehler(standUmsatzZaehler, smartCardFixture.signatureJob)

        then:
        result
        println result
    }

    def "Test encryptStandUmsatzZaehler _ NullArguments"() {
        when:
        SmartCardUtils.encryptStandUmsatzZaehler(0, null)

        then:
        thrown IllegalArgumentException
    }

    def "Test decryptStandUmsatzZaehler _ Valid"() {
        given:
        def standUmsatzZaehler = 10000 as long
        def encryptedValue = SmartCardUtils.encryptStandUmsatzZaehler(standUmsatzZaehler, smartCardFixture.signatureJob)

        when:
        def decryptedValue = SmartCardUtils.decryptStandUmsatzZaehler(encryptedValue, smartCardFixture.signatureJob)

        then:
        encryptedValue
        decryptedValue
        decryptedValue == standUmsatzZaehler
    }

    def "Test decryptStandUmsatzZaehler _ InvalidDecryptionKey"() {
        given:
        def standUmsatzZaehler = 10000 as long
        def encryptedValue = SmartCardUtils.encryptStandUmsatzZaehler(standUmsatzZaehler, smartCardFixture.signatureJob)

        when:
        def signatureJob = smartCardFixture.signatureJob

        signatureJob.secretKey = SmartCardUtils.getRandomAes256Key()
        def decryptedValue = SmartCardUtils.decryptStandUmsatzZaehler(encryptedValue, smartCardFixture.signatureJob)

        then:
        encryptedValue
        decryptedValue
        decryptedValue != standUmsatzZaehler
    }

    def "Test decryptStandUmsatzZaehler _ encryptedUmsatzZaehler _ IsNull"() {
        when:
        SmartCardUtils.decryptStandUmsatzZaehler(null, smartCardFixture.signatureJob)

        then:
        thrown IllegalArgumentException
    }

    def "Test decryptStandUmsatzZaehler _ signatureJob _ IsNull"() {
        when:
        SmartCardUtils.decryptStandUmsatzZaehler(Base64.getEncoder().encodeToString("asdf".getBytes("UTF-8")), null)

        then:
        thrown IllegalArgumentException
    }

    def "Test getLetzterSignatureChainValue _ Valid"() {
        given:
        SignatureResult signatureResult = SignatureResult.builder()
                .headerBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString('{"alg":"ES256"}'.getBytes("UTF-8")))
                .payloadBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString("aydhnfjkasdbnfkjagbsdfjasdf".getBytes("UTF-8")))
                .signatureBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString("aajsdhflhkasdgfkajsdfas".getBytes("UTF-8")))
                .build()

        def signatureJob = smartCardFixture.signatureJob

        when:
        def result = SmartCardUtils.getLetzterSignatureChainValue(signatureResult, signatureJob)

        then:
        result
        result == "jelVbNH1XnM="
        Base64.getDecoder().decode(result).length == signatureJob.rksSuite.numberOfBytesExtractedFromPrevSigHash
    }

    def "Test getStartInvoiceHashValue _ Valid"() {
        when:
        def result = SmartCardUtils.getStartInvoiceHashValue(smartCardFixture.signatureJob)

        then:
        result
        result == "a4ayc/80/OE="
    }

    def "Test calc checksum"() {
        given:
        String aesKey = "cWhay3H4asTvRzXzXGZQ3KyBEu9BZaIxl8J+L4Bhr5A="
        RksSuite rksSuite = RksSuite.R1_AT1

        when:
        String result = SmartCardUtils.calcCheckSumForKey(aesKey, rksSuite)

        then:
        result == "qx6p"
    }
}
