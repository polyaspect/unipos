package unipos.integritySafeGuard.domain

import groovy.transform.ToString
import org.springframework.util.Assert
import unipos.integritySafeGuard.SmartCardUtils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * This class represents a single Invoice that gets signed
 * Created by domin on 16.09.2016.
 */
@ToString(includeNames = true)
class SignatureInvoice {
    RksSuite rksSuite
    String kassaId
    String belegNummer
    LocalDateTime belegDatumUhrzeit
    BigDecimal betragSatzNormal
    BigDecimal betragSatzErmaessigt1
    BigDecimal betragSatzErmaessigt2
    BigDecimal betragSatzNull
    BigDecimal betragSatzBesonders
    String standUmsatzZaehlerEncrypted
    long standUmsatzZaehler
    String zertifikatSeriennummer
    String signaturVorrigerBeleg
    String signature
    Type signatureType

    public static SignatureInvoice getStartInvoice(SignatureJob signatureJob, LocalDateTime belegDatumUhrzeit) {
        SignatureInvoice signatureInvoice = new SignatureInvoice()
        signatureInvoice.rksSuite = signatureJob.rksSuite
        signatureInvoice.kassaId = signatureJob.kassaId
        signatureInvoice.belegNummer = signatureJob.belegNr
        signatureInvoice.belegDatumUhrzeit = belegDatumUhrzeit
        signatureInvoice.betragSatzNormal = new BigDecimal("0")
        signatureInvoice.betragSatzErmaessigt1 = new BigDecimal("0")
        signatureInvoice.betragSatzErmaessigt2 = new BigDecimal("0")
        signatureInvoice.betragSatzNull = new BigDecimal("0")
        signatureInvoice.betragSatzBesonders = new BigDecimal("0")
        signatureInvoice.standUmsatzZaehler = 0 as long
        signatureInvoice.signaturVorrigerBeleg = SmartCardUtils.getStartInvoiceHashValue(signatureJob)
        signatureInvoice.signatureType = Type.START

        signatureInvoice
    }

    public static SignatureInvoice getNullInvoice(SignatureJob signatureJob, LocalDateTime belegDatumUhrzeit, long standUmsatzZaehler, SignatureResult signatureResult, nullInvoiceType) {
        SignatureInvoice signatureInvoice = new SignatureInvoice()
        signatureInvoice.rksSuite = signatureJob.rksSuite
        signatureInvoice.kassaId = signatureJob.kassaId
        signatureInvoice.belegNummer = signatureJob.belegNr
        signatureInvoice.belegDatumUhrzeit = belegDatumUhrzeit
        signatureInvoice.betragSatzNormal = new BigDecimal("0")
        signatureInvoice.betragSatzErmaessigt1 = new BigDecimal("0")
        signatureInvoice.betragSatzErmaessigt2 = new BigDecimal("0")
        signatureInvoice.betragSatzNull = new BigDecimal("0")
        signatureInvoice.betragSatzBesonders = new BigDecimal("0")
        signatureInvoice.standUmsatzZaehler = standUmsatzZaehler
        signatureInvoice.signaturVorrigerBeleg = SmartCardUtils.getLetzterSignatureChainValue(signatureResult, signatureJob)
        signatureInvoice.signatureType = nullInvoiceType

        signatureInvoice
    }

    public static SignatureInvoice getInvoice(SignatureJob signatureJob, LocalDateTime belegDatumUhrzeit, BigDecimal belegSatzNormal, BigDecimal belegSatzErmaessigt1, BigDecimal belegSatzErmaessigt2, BigDecimal belegSatzNull, BigDecimal belegSatzBesonders ,long standUmsatzZaehler, SignatureResult signatureResult) {
        SignatureInvoice signatureInvoice = new SignatureInvoice()
        signatureInvoice.rksSuite = signatureJob.rksSuite
        signatureInvoice.kassaId = signatureJob.kassaId
        signatureInvoice.belegNummer = signatureJob.belegNr
        signatureInvoice.belegDatumUhrzeit = belegDatumUhrzeit
        signatureInvoice.betragSatzNormal = belegSatzNormal
        signatureInvoice.betragSatzErmaessigt1 = belegSatzErmaessigt1
        signatureInvoice.betragSatzErmaessigt2 = belegSatzErmaessigt2
        signatureInvoice.betragSatzNull = belegSatzNull
        signatureInvoice.betragSatzBesonders = belegSatzBesonders
        signatureInvoice.standUmsatzZaehler = standUmsatzZaehler
        signatureInvoice.signaturVorrigerBeleg = SmartCardUtils.getLetzterSignatureChainValue(signatureResult, signatureJob)
        signatureInvoice.signatureType = Type.STANDARD

        signatureInvoice
    }

    public static SignatureInvoice getTrainingsInvoice(SignatureJob signatureJob, LocalDateTime belegDatumUhrzeit, BigDecimal belegSatzNormal, BigDecimal belegSatzErmaessigt1, BigDecimal belegSatzErmaessigt2, BigDecimal belegSatzNull, BigDecimal belegSatzBesonders ,long standUmsatzZaehler, SignatureResult signatureResult) {
        SignatureInvoice signatureInvoice = new SignatureInvoice()
        signatureInvoice.rksSuite = signatureJob.rksSuite
        signatureInvoice.kassaId = signatureJob.kassaId
        signatureInvoice.belegNummer = signatureJob.belegNr
        signatureInvoice.belegDatumUhrzeit = belegDatumUhrzeit
        signatureInvoice.betragSatzNormal = belegSatzNormal
        signatureInvoice.betragSatzErmaessigt1 = belegSatzErmaessigt1
        signatureInvoice.betragSatzErmaessigt2 = belegSatzErmaessigt2
        signatureInvoice.betragSatzNull = belegSatzNull
        signatureInvoice.betragSatzBesonders = belegSatzBesonders
        signatureInvoice.standUmsatzZaehler = standUmsatzZaehler
        signatureInvoice.signaturVorrigerBeleg = SmartCardUtils.getLetzterSignatureChainValue(signatureResult, signatureJob)
        signatureInvoice.signatureType = Type.TRAINING

        signatureInvoice
    }

    public static SignatureInvoice getStornoInvoice(SignatureJob signatureJob, LocalDateTime belegDatumUhrzeit, BigDecimal belegSatzNormal, BigDecimal belegSatzErmaessigt1, BigDecimal belegSatzErmaessigt2, BigDecimal belegSatzNull, BigDecimal belegSatzBesonders ,long standUmsatzZaehler, SignatureResult signatureResult) {
        SignatureInvoice signatureInvoice = new SignatureInvoice()
        signatureInvoice.rksSuite = signatureJob.rksSuite
        signatureInvoice.kassaId = signatureJob.kassaId
        signatureInvoice.belegNummer = signatureJob.belegNr
        signatureInvoice.belegDatumUhrzeit = belegDatumUhrzeit
        signatureInvoice.betragSatzNormal = belegSatzNormal
        signatureInvoice.betragSatzErmaessigt1 = belegSatzErmaessigt1
        signatureInvoice.betragSatzErmaessigt2 = belegSatzErmaessigt2
        signatureInvoice.betragSatzNull = belegSatzNull
        signatureInvoice.betragSatzBesonders = belegSatzBesonders
        signatureInvoice.standUmsatzZaehler = standUmsatzZaehler
        signatureInvoice.signaturVorrigerBeleg = SmartCardUtils.getLetzterSignatureChainValue(signatureResult, signatureJob)
        signatureInvoice.signatureType = Type.STORNO

        signatureInvoice
    }


    public String compressDataString() {


        sprintf('_%1$s_%2$s_%3$s_%4$s_%5$s_%6$s_%7$s_%8$s_%9$s_%10$s_%11$s_%12$s',
        [
                rksSuite.suiteId,
                kassaId,
                belegNummer,
                belegDatumUhrzeit.withNano(0).format(DateTimeFormatter.ISO_DATE_TIME),
                betragSatzNormal.setScale(2).toString().replace(".",","),
                betragSatzErmaessigt1.setScale(2).toString().replace(".",","),
                betragSatzErmaessigt2.setScale(2).toString().replace(".",","),
                betragSatzNull.setScale(2).toString().replace(".",","),
                betragSatzBesonders.setScale(2).toString().replace(".",","),
                standUmsatzZaehlerEncrypted,
                zertifikatSeriennummer,
                signaturVorrigerBeleg
        ])
    }

    public boolean hasEncryptedUmsatzZaehler() {
        return standUmsatzZaehlerEncrypted != null && !standUmsatzZaehlerEncrypted.isEmpty()
    }

    public void validate() {
        Assert.notNull(kassaId, "You must provide a kassaId!")
        Assert.notNull(belegNummer, "You must provide a belegNr!")
    }
}

enum Type {
    STANDARD(false, "Standardbeleg"),
    STORNO(false, "Stornobeleg"),
    TRAINING(false, "Trainingsbeleg"),
    NULL(false, "Nullbeleg"),
    START(true, "Startbeleg"),
    SAMMEL(true, "Sammelbeleg"),
    SCHLUSS(false, "Schlussbeleg"),
    MONATS(false, "Monatsbeleg"),
    JAHRES(true, "Jahresbeleg")

    boolean signatureRequired;
    String name;

    Type(boolean signatureRequired, String name) {
        this.signatureRequired = signatureRequired
        this.name = name
    }
}
