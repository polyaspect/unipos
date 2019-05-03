package unipos.integritySafeGuard.domain

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import spock.lang.Specification
import unipos.integritySafeGuard.SmartCardFixture
import unipos.integritySafeGuard.config.TestConfiguration

import java.time.LocalDateTime

/**
 * Testing of the SignatureInvoice Class (Unit - Tests)
 * Created by domin on 16.09.2016.
 */
@ContextConfiguration(classes = TestConfiguration.class)
class SignatureInvoiceTest extends Specification {

    private SignatureInvoice signatureInvoice

    @Autowired
    SmartCardFixture smartCardFixture

    def setup() {
        signatureInvoice = new SignatureInvoice()
        signatureInvoice.rksSuite = RksSuite.R1_AT2
        signatureInvoice.kassaId = "3"
        signatureInvoice.belegNummer = "5096"
        signatureInvoice.belegDatumUhrzeit = LocalDateTime.of(2016,9,16,9,57,10)
        signatureInvoice.betragSatzNormal = new BigDecimal("5.99")
        signatureInvoice.betragSatzErmaessigt1 = new BigDecimal("1.99")
        signatureInvoice.betragSatzErmaessigt2 = new BigDecimal("0")
        signatureInvoice.betragSatzNull = new BigDecimal("0")
        signatureInvoice.betragSatzBesonders = new BigDecimal("0")
        signatureInvoice.standUmsatzZaehlerEncrypted = "ABCD"
        signatureInvoice.zertifikatSeriennummer = "dCBA"
        signatureInvoice.signaturVorrigerBeleg = "74a6df5a4sgg65g4asdf"
    }

    def "Test getCompressedString"() {
        when:
        def result = signatureInvoice.compressDataString()

        then:
        result == "_R1-AT2_3_5096_2016-09-16T09:57:10_5,99_1,99_0,00_0,00_0,00_ABCD_dCBA_74a6df5a4sgg65g4asdf"
    }

    def "Test getStartInvoice"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        SignatureJob signatureJob = smartCardFixture.signatureJob

        when:
        def result = SignatureInvoice.getStartInvoice(signatureJob, now)

        then:
        result.rksSuite == RksSuite.R1_AT2
        result.belegDatumUhrzeit == now
        result.betragSatzNormal == new BigDecimal("0")
        result.betragSatzErmaessigt1 == new BigDecimal("0")
        result.betragSatzErmaessigt2 == new BigDecimal("0")
        result.betragSatzNull == new BigDecimal("0")
        result.betragSatzBesonders == new BigDecimal("0")
        result.standUmsatzZaehler == 0l
        result.standUmsatzZaehlerEncrypted == null
        result.zertifikatSeriennummer == null
        result.signaturVorrigerBeleg == "a4ayc/80/OE=" //Base64 representation of the KassaID
        result.signatureType == Type.START
    }

    def "Test getNullInvoice"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        SignatureJob signatureJob = smartCardFixture.signatureJob

        SignatureResult signatureResult = SignatureResult.builder()
                .headerBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString('{"alg":"ES256"}'.getBytes("UTF-8")))
                .payloadBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString("aydhnfjkasdbnfkjagbsdfjasdf".getBytes("UTF-8")))
                .signatureBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString("aajsdhflhkasdgfkajsdfas".getBytes("UTF-8")))
                .build()

        when:
        def result = SignatureInvoice.getNullInvoice(signatureJob, now, 10000 as long, signatureResult, Type.NULL)

        then:
        result.rksSuite == RksSuite.R1_AT2
        result.belegDatumUhrzeit == now
        result.betragSatzNormal == new BigDecimal("0")
        result.betragSatzErmaessigt1 == new BigDecimal("0")
        result.betragSatzErmaessigt2 == new BigDecimal("0")
        result.betragSatzNull == new BigDecimal("0")
        result.betragSatzBesonders == new BigDecimal("0")
        result.standUmsatzZaehler == 10000 as long
        result.standUmsatzZaehlerEncrypted == null
        result.zertifikatSeriennummer == null
        result.signaturVorrigerBeleg == "jelVbNH1XnM=" //Base64 representation of the KassaID
        result.signatureType == Type.NULL
    }

    def "Test getTrainingInvoice"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        SignatureJob signatureJob = smartCardFixture.signatureJob
        SignatureResult signatureResult = SignatureResult.builder()
                .headerBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString('{"alg":"ES256"}'.getBytes("UTF-8")))
                .payloadBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString("aydhnfjkasdbnfkjagbsdfjasdf".getBytes("UTF-8")))
                .signatureBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString("aajsdhflhkasdgfkajsdfas".getBytes("UTF-8")))
                .build()

        when:
        def result = SignatureInvoice.getTrainingsInvoice(
                signatureJob,
                now,
                new BigDecimal("100"),
                new BigDecimal("19.99"),
                new BigDecimal("0"),
                new BigDecimal("0"),
                new BigDecimal("0"),
                99999 as long,
                signatureResult
        )

        then:
        result.rksSuite == RksSuite.R1_AT2
        result.belegDatumUhrzeit == now
        result.betragSatzNormal == new BigDecimal("100.00")
        result.betragSatzErmaessigt1 == new BigDecimal("19.99")
        result.betragSatzErmaessigt2 == new BigDecimal("0")
        result.betragSatzNull == new BigDecimal("0")
        result.betragSatzBesonders == new BigDecimal("0")
        result.standUmsatzZaehler == 99999 as long
        result.standUmsatzZaehlerEncrypted == null
        result.zertifikatSeriennummer == null
        result.signaturVorrigerBeleg == "jelVbNH1XnM=" //Base64 representation of the KassaID
        result.signatureType == Type.TRAINING
    }

    def "Test getStandardInvoice"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        SignatureJob signatureJob = smartCardFixture.signatureJob
        SignatureResult signatureResult = SignatureResult.builder()
                .headerBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString('{"alg":"ES256"}'.getBytes("UTF-8")))
                .payloadBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString("aydhnfjkasdbnfkjagbsdfjasdf".getBytes("UTF-8")))
                .signatureBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString("aajsdhflhkasdgfkajsdfas".getBytes("UTF-8")))
                .build()

        when:
        def result = SignatureInvoice.getInvoice(
                signatureJob,
                now,
                new BigDecimal("9.99"),
                new BigDecimal("100"),
                new BigDecimal("0"),
                new BigDecimal("-5"),
                new BigDecimal("0"),
                123456 as long,
                signatureResult
        )

        then:
        result.rksSuite == RksSuite.R1_AT2
        result.belegDatumUhrzeit == now
        result.betragSatzNormal == new BigDecimal("9.99")
        result.betragSatzErmaessigt1 == new BigDecimal("100.00")
        result.betragSatzErmaessigt2 == new BigDecimal("0")
        result.betragSatzNull == new BigDecimal("-5")
        result.betragSatzBesonders == new BigDecimal("0")
        result.standUmsatzZaehler == 123456 as long
        result.standUmsatzZaehlerEncrypted == null
        result.zertifikatSeriennummer == null
        result.signaturVorrigerBeleg == "jelVbNH1XnM=" //Base64 representation of the KassaID
        result.signatureType == Type.STANDARD
    }

    def "Test getStornoInvoice"() {
        given:
        LocalDateTime now = LocalDateTime.now()
        SignatureJob signatureJob = smartCardFixture.signatureJob
        SignatureResult signatureResult = SignatureResult.builder()
                .headerBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString('{"alg":"ES256"}'.getBytes("UTF-8")))
                .payloadBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString("aydhnfjkasdbnfkjagbsdfjasdf".getBytes("UTF-8")))
                .signatureBase64Url(Base64.getUrlEncoder().withoutPadding().encodeToString("aajsdhflhkasdgfkajsdfas".getBytes("UTF-8")))
                .build()

        when:
        def result = SignatureInvoice.getStornoInvoice(
                signatureJob,
                now,
                new BigDecimal("-1.99"),
                new BigDecimal("0"),
                new BigDecimal("0"),
                new BigDecimal("-10"),
                new BigDecimal("0"),
                897654231 as long,
                signatureResult
        )

        then:
        result.rksSuite == RksSuite.R1_AT2
        result.belegDatumUhrzeit == now
        result.betragSatzNormal == new BigDecimal("-1.99")
        result.betragSatzErmaessigt1 == new BigDecimal("0")
        result.betragSatzErmaessigt2 == new BigDecimal("0")
        result.betragSatzNull == new BigDecimal("-10.00")
        result.betragSatzBesonders == new BigDecimal("0")
        result.standUmsatzZaehler == 897654231 as long
        result.standUmsatzZaehlerEncrypted == null
        result.zertifikatSeriennummer == null
        result.signaturVorrigerBeleg == "jelVbNH1XnM=" //Base64 representation of the KassaID
        result.signatureType == Type.STORNO
    }
}
