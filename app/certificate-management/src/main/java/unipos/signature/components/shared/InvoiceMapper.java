package unipos.signature.components.shared;

import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.Tax;
import unipos.integritySafeGuard.domain.SignatureInvoice;
import unipos.integritySafeGuard.domain.SignatureResult;
import unipos.signature.components.signatureOption.SignatureOption;

import java.math.BigDecimal;

/**
 * Created by domin on 01.12.2016.
 */
public class InvoiceMapper {

    public static SignatureInvoice getDefaultSignatureInvoice(Invoice invoice, SignatureOption signatureOption, SignatureResult signatureResult) {
        BigDecimal betragNormal = new BigDecimal("0.00");
        BigDecimal betragErmaessigt1= new BigDecimal("0.00");
        BigDecimal betragErmaessigt2= new BigDecimal("0.00");
        BigDecimal betragNull= new BigDecimal("0.00");
        BigDecimal betragBesonders = new BigDecimal("0.00");

        for(Tax tax : invoice.getInvoicesTaxStuff()) {
            switch (tax.getTaxRate()) {
                case 20:
                    betragNormal = betragNormal.add(tax.getTaxAmount());
                    break;
                case 10:
                    betragErmaessigt1 = betragErmaessigt1.add(tax.getTaxAmount());
                    break;
                case 13:
                    betragErmaessigt2 = betragErmaessigt2.add(tax.getTaxAmount());
                    break;
                case 0:
                    betragNull = betragNull.add(tax.getTaxAmount());
                    break;
                default:
                    betragBesonders = betragBesonders.add(tax.getTaxAmount());
                    break;
            }
        }

        SignatureInvoice signatureInvoice = SignatureInvoice.getInvoice(signatureOption.signatureJob(String.valueOf(invoice.getInvoiceId())),
                invoice.getCreationDate(),
                betragNormal,
                betragErmaessigt1,
                betragErmaessigt2,
                betragNull,
                betragBesonders,
                invoice.getUmsatzZaehler(),
                signatureResult
                );

        return signatureInvoice;
    }
}
