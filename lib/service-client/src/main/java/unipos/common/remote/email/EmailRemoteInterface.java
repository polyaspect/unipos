package unipos.common.remote.email;

import unipos.common.remote.pos.model.Invoice;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Thomas on 11.07.2016.
 */
public interface EmailRemoteInterface {

    String getCurrentVersion();
    void sendConfirmationEmail(Invoice invoice, HttpServletRequest request);
}
