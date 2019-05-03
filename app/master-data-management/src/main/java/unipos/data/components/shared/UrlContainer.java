package unipos.data.components.shared;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by dominik on 28.08.15.
 */
public class UrlContainer {
    public static final String BASEURL = "http://localhost:8080";
    public static final String ORDERS_CREATE_NEW_ORDER = "/orders/createNewOrder";
    public static final String SOCKET_SEND_TO_ALL = "/socket/sendToAll";
    public static final String AUTH_GET_USERNAME_BY_AUTH_TOKEN = "/auth/auth/getUsernameByAuthToken";
    public static final String SOCKET_CLIENT_INFORMATION = "/socket/clientInformation";
    public static final String SYNC_SYNCCHANGES = "/sync/syncChanges";
    public static final String SYNC_SYNCCHANGESACTION = "/sync/syncChangesAction";

    public static String baseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":8080";
    }
}
