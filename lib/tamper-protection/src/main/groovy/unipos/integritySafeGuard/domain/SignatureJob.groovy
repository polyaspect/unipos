package unipos.integritySafeGuard.domain

import lombok.AllArgsConstructor
import lombok.Builder
import lombok.Data
import lombok.NoArgsConstructor

import javax.crypto.SecretKey

/**
 * Holds all the Meta-Infos about the SignatureProcess
 * Created by domin on 30.09.2016.
 */
@groovy.transform.builder.Builder
class SignatureJob {

    String kassaId;
    String belegNr;
    SecretKey secretKey;
    RksSuite rksSuite;
    String pin;
    int turnOverCounterLengthInBytes
    boolean signatureDeviceAvailable = true

    public int getTurnOverCounterLengthInBytes() {
        return turnOverCounterLengthInBytes >= 5 && turnOverCounterLengthInBytes <= 8 ? turnOverCounterLengthInBytes : 5
    }

    String getKassaId() {
        return kassaId
    }

    void setKassaId(String kassaId) {
        this.kassaId = kassaId
    }

    String getBelegNr() {
        return belegNr
    }

    void setBelegNr(String belegNr) {
        this.belegNr = belegNr
    }

    SecretKey getSecretKey() {
        return secretKey
    }

    void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey
    }

    RksSuite getRksSuite() {
        return rksSuite
    }

    void setRksSuite(RksSuite rksSuite) {
        this.rksSuite = rksSuite
    }

    void setTurnOverCounterLengthInBytes(int turnOverCounterLengthInBytes) {
        this.turnOverCounterLengthInBytes = turnOverCounterLengthInBytes
    }

    boolean getSignatureDeviceAvailable() {
        return signatureDeviceAvailable
    }

    void setSignatureDeviceAvailable(boolean signatureDeviceAvailable) {
        this.signatureDeviceAvailable = signatureDeviceAvailable
    }

    String getPin() {
        return pin
    }

    void setPin(String pin) {
        this.pin = pin
    }
}
