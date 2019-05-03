package unipos.integritySafeGuard.domain

import groovy.transform.builder.Builder
import lombok.AllArgsConstructor
import lombok.NoArgsConstructor
import org.apache.commons.codec.binary.Base32
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

import java.time.LocalDateTime

import static java.util.stream.Collectors.joining

/**
 * Created by domin on 30.09.2016.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder(builderClassName = "SignatureResultBuilder")
@Document(collection = "signatureResults")
class SignatureResult {

    @Id
    String id
    String headerBase64Url;
    String payloadBase64Url;
    String signatureBase64Url;
    Type invoiceSignatureType;
    LocalDateTime creationDate;
    String storeGuid;
    String umsatzZaehlerGuid;

    String toJwsCompactRepresentation() {
        headerBase64Url + "." + payloadBase64Url + "." + signatureBase64Url
    }

    String toQrCodeRepresentation() {
        def decodedPayloadBytes = Base64.getUrlDecoder().decode(payloadBase64Url)
        String decodedPayload = new String(decodedPayloadBytes, "UTF-8")
        decodedPayload + "_" + Base64.getEncoder().encodeToString(Base64.getUrlDecoder().decode(signatureBase64Url))
    }

    String toOcrCodeRepresentation() {
        Base32 base32 = new Base32()

        def decodedPayloadBytes = Base64.getUrlDecoder().decode(payloadBase64Url)
        String decodedPayload = new String(decodedPayloadBytes, "UTF-8")

        def payloadParts = decodedPayload.tokenize('_').stream().filter({x -> !x.isEmpty()}).toArray() as String[]
        payloadParts[9] = base32.encodeToString(Base64.getDecoder().decode(payloadParts[9]))
        payloadParts[11] = base32.encodeToString(Base64.getDecoder().decode(payloadParts[11]))
        def list = new ArrayList<String>(Arrays.asList(payloadParts))
        list.add(base32.encodeToString(Base64.getUrlDecoder().decode(signatureBase64Url)))
        "_" + list.stream().collect(joining('_'))
    }

    public static SignatureResult getFromJwsString(String jwsString) {
        def jwsParts = jwsString.tokenize(".")
        if (jwsParts.size() != 3) {
            throw new IllegalArgumentException("No valid jwsString provided: " + jwsString)
        }

        try {
            Base64.getUrlDecoder().decode(jwsParts.get(0))
            Base64.getUrlDecoder().decode(jwsParts.get(1))
            Base64.getUrlDecoder().decode(jwsParts.get(2))
        } catch (Exception ignored) {
            throw new IllegalArgumentException("Not all parts of the given jwsString are Base64URL encoded: " + jwsString)
        }

        builder()
        .headerBase64Url(jwsParts.get(0))
        .payloadBase64Url(jwsParts.get(1))
        .signatureBase64Url(jwsParts.get(2))
        .build()
    }

    public static SignatureResult fromQrCodeRepresentation(String qrCode) {
        def qrParts = qrCode.tokenize(".")
        if (qrParts.size() != 2) {
            throw new IllegalArgumentException("No valid qrParts provided: " + qrParts)
        }

        builder()
                .payloadBase64Url(Base64.getUrlEncoder().encodeToString(qrParts.get(0).getBytes("UTF-8")))
                .signatureBase64Url(Base64.getUrlEncoder().encodeToString(Base64.getDecoder().decode(qrParts.get(1).getBytes("UTF-8"))))
                .build();
    }
}
