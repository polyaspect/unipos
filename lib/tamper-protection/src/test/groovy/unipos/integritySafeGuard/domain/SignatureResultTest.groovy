package unipos.integritySafeGuard.domain

import spock.lang.Specification

/**
 * Testing of the Signature Result class
 * Created by domin on 30.09.2016.
 */
class SignatureResultTest extends Specification {

    def "Test ToJwsCompactString _ ValidData"() {
        given:
        def signatureResult = SignatureResult.builder()
                .headerBase64Url("{\"alg\":\"ES256\"}")
                .payloadBase64Url("DasIstEinTestString")
                .signatureBase64Url("adkjfoasjfasiofhaewopfkjwopfjaIOUÖGHRGJAEÖGAEG")
                .build()

        when:
        def result = signatureResult.toJwsCompactRepresentation()

        then:
        result == "{\"alg\":\"ES256\"}.DasIstEinTestString.adkjfoasjfasiofhaewopfkjwopfjaIOUÖGHRGJAEÖGAEG"
    }

    def "Test ToQrCodeRepresentation _ Valid"() {
        given:
        def signatureResult = SignatureResult.builder()
                .headerBase64Url("eyJhbGciOiJFUzI1NiJ9")
                .payloadBase64Url("X1IxLUFUMF9ERU1PLUNBU0gtQk9YNTI0XzM2NjU5Nl8yMDE1LTEyLTE3VDExOjIzOjQ0XzAsMDBfMCwwMF8zLDY0Xy0yLDYwXzEsNzlfVkZKQl80N2JlNzM3Y2IxZjZkMWYxX1p2TnhKdzZhMUE0PQ")
                .signatureBase64Url("J7YC28zquHfHzMpx02TqElbXOTSgXQu5JAA9Xu1Xzzu5p8eUYT-sgmyhzRps5nYyEp5Yh8ATIa9130zmuiACHw")
                .build()

        when:
        def result = signatureResult.toQrCodeRepresentation()

        then:
        result == "_R1-AT0_DEMO-CASH-BOX524_366596_2015-12-17T11:23:44_0,00_0,00_3,64_-2,60_1,79_VFJB_47be737cb1f6d1f1_ZvNxJw6a1A4=_J7YC28zquHfHzMpx02TqElbXOTSgXQu5JAA9Xu1Xzzu5p8eUYT+sgmyhzRps5nYyEp5Yh8ATIa9130zmuiACHw=="
    }

    def "Test ToOcrCodeRepresentation _ Valid"() {
        given:
        def signatureResult = SignatureResult.builder()
                .headerBase64Url("eyJhbGciOiJFUzI1NiJ9")
                .payloadBase64Url("X1IxLUFUMF9ERU1PLUNBU0gtQk9YNTI0XzM2NjYxMF8yMDE1LTEyLTE3VDExOjIzOjQ0XzAsMDBfMCwwMF8wLDAwXzAsMDBfMCwwMF9EbmdmaHBnaEt1OD1fNDdiZTczN2NiMWY2ZDFmMV9oQXJoZENUU2piTT0")
                .signatureBase64Url("Ubz6l2-TFt80EWAf6CTr_Xocpgn9UhOhSvlGMqQsML1LxieEE7bWqB5Y6HAwaC0NSA50swrGHxKs_UOGPS1SJA")
                .build()

        when:
        def result = signatureResult.toOcrCodeRepresentation()

        then:
        result == "_R1-AT0_DEMO-CASH-BOX524_366610_2015-12-17T11:23:44_0,00_0,00_0,00_0,00_0,00_BZ4B7BUYEEVO6===_47be737cb1f6d1f1_QQFOC5BE2KG3G===_KG6PVF3PSMLN6NARMAP6QJHL7V5BZJQJ7VJBHIKK7FDDFJBMGC6UXRRHQQJ3NVVIDZMOQ4BQNAWQ2SAOOSZQVRQ7CKWP2Q4GHUWVEJA="
    }

    def "Test getFromJwsString _ Valid"() {
        given:
        def jwsString = "eyJhbGciOiJFUzI1NiJ9.X1IxLUFUMF9ERU1PLUNBU0gtQk9YNTI0XzM2NjYxMF8yMDE1LTEyLTE3VDExOjIzOjQ0XzAsMDBfMCwwMF8wLDAwXzAsMDBfMCwwMF9EbmdmaHBnaEt1OD1fNDdiZTczN2NiMWY2ZDFmMV9oQXJoZENUU2piTT0.Ubz6l2-TFt80EWAf6CTr_Xocpgn9UhOhSvlGMqQsML1LxieEE7bWqB5Y6HAwaC0NSA50swrGHxKs_UOGPS1SJA"

        when:
        def result = SignatureResult.getFromJwsString(jwsString)

        then:
        result
        result.headerBase64Url
        result.payloadBase64Url
        result.signatureBase64Url
    }

    def "Test "(){
        given:
        def sigRes = SignatureResult.builder()
                .payloadBase64Url("X1IxLUFUMV9yazAwMDFfMV8yMDE3LTA2LTIxVDE3OjAwOjIyXzAsMDBfMCwwMF8wLDAwXzAsMDBfMCwwMF90cGJoREwwPV8zOTVGQkM1Ql9HcEVJWXlsVVJMZz0=")
                .signatureBase64Url("G0onG8pXy0OdIF2wK0Gqe0dOumLX4WCZj6r-JbnD7AKsI-Vy-02zXd8FXcZvQwobR_3IlJH9_zoYcjlTAUTyvQ==")
        .build()

        when:
        def result = sigRes.toQrCodeRepresentation()

        then:
        result
    }

    def "Test2"(){
        given:
        def qrString = "_R1-AT1_rk0001_1_2017-06-21T17:00:22_0,00_0,00_0,00_0,00_0,00_tpbhDL0=_395FBC5B_GpEIYylURLg=.G0onG8pXy0OdIF2wK0Gqe0dOumLX4WCZj6r+JbnD7AKsI+Vy+02zXd8FXcZvQwobR/3IlJH9/zoYcjlTAUTyvQ=="

        when:
        def result = SignatureResult.fromQrCodeRepresentation(qrString)

        then:
        result
    }

    def "Test getFromJwsString _ Not three parts"() {
        given:
        def incompleteJwsString = "eyJhbGciOiJFUzI1NiJ9.Ubz6l2-TFt80EWAf6CTr_Xocpgn9UhOhSvlGMqQsML1LxieEE7bWqB5Y6HAwaC0NSA50swrGHxKs_UOGPS1SJA"

        when:
        SignatureResult.getFromJwsString(incompleteJwsString)

        then:
        thrown IllegalArgumentException
    }

    def "Test getFromJwsString _ Invalid Base64Url String"() {
        given:
        def notBase64UrlStringButThreeParts = "asfdasdfasdfasdfa.asdfasdfasdf.asdfasdfasdf"

        when:
        SignatureResult.getFromJwsString(notBase64UrlStringButThreeParts)

        then:
        thrown IllegalArgumentException
    }
}