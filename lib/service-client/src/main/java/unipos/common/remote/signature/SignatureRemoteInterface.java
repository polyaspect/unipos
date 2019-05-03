package unipos.common.remote.signature;

import com.fasterxml.jackson.core.JsonProcessingException;
import unipos.common.remote.pos.model.Invoice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by domin on 23.12.2016.
 */
public interface SignatureRemoteInterface {

    Invoice createStartInvoice(Invoice invoice);

    Invoice signInvoice(Invoice invoice);

    boolean isSignatureForStoreEnabled(String storeGuid);

    boolean isSignatureModuleUpAndRunning();

    boolean isSammelbelegRequired(HttpServletRequest request);

    boolean isSmartCardAvailable(HttpServletRequest request);

    boolean isSammelbelegRequiredAndSmartCardAvailable(HttpServletRequest request, String storeGuid);

    Invoice createZeroInvoice(String storeGuid, Invoice.SignatureInvoiceType monats);

    void sendDep(String storeGuid);
}
