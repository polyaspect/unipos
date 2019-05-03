package unipos.common.container;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Dominik on 03.12.2015.
 */
public class UrlContainer {
    public static final String BASEURL = "http://localhost:8080";
    public static final String ORDERS_CREATE_NEW_ORDER = "/orders/createNewOrder";
    public static final String AUTH_ADD_RIGHTS = "/auth/rights/list";
    public static final String AUTH_ACCOUNT_LOGIN = "/auth/auth/account_login";
    public static final String AUTH_ADD_USERNAME_PASSWORD_AUTH_METHOD = "/auth/usernamePassword";
    public static final String AUTH_CHECK_USERNAME_PASSWORD = "/auth/usernamePassword/checkCredentials?username={username}&password={password}";
    public static final String AUTH_GET_USER_BY_GUID = "/auth/auth/getUserByUserGuid";
    public static final String AUTH_GET_USER_BY_USERNAME = "/auth/auth/getUserByUsername";
    public static final String AUTH_GET_USER_BY_USER_ID = "/auth/auth/getByUserId/{userId}";
    public static final String AUTH_GET_USERID_BY_AUTH_TOKEN = "/auth/auth/getUserIdByAuthToken";
    public static final String AUTH_GET_USERNAME_BY_AUTH_TOKEN = "/auth/auth/getUsernameByAuthToken";

    public static final String AUTH_CURRENT_VERSION = "/auth/getCurrentVersion";

    public static final String CLIENT_CONNECTED = "/socket/clientConnected";
    public static final String CORE_ADD_LOG = "/logs/addLog";
    public static final String CORE_ADD_NEW_UPDATING_TIME = "/timeTask/new";
    public static final String CORE_ADD_PERMISSIONS = "/modules/permissions";
    public static final String CORE_AUTOUPDATE_DELETE_BY_STORE_GUID = "/timeTask/deleteByGuid";
    public static final String CORE_FIND_ALL_LICENSEINFOS = "/licenseManagement";
    public static final String CORE_LICENSE_FILE_EXISTS = "/licenseManagement/doesLicenseFileExist";
    public static final String CORE_MODULE_STATUS = "/modules/status/{moduleName}";
    public static final String CORE_STARTED_MODULES = "/modules/started";

    public static final String CORE_CURRENT_VERSION = "/getCurrentVersion";

    public static final String CUSTOMER_ADD_CUSTOMER = "/customer/customer";
    public static final String CUSTOMER_FIND_BY_GUID = "/customer/customer/findCustomerByGuid";

    public static final String DATA_GET_COMPANY_BY_GUID = "/data/companies/guid";
    public static final String DATA_GET_CONTROLLER_PLACED_STORE = "data/stores/controllerPlacedStore";
    public static final String DATA_GET_PRODUCT_BY_DOCUMENTID = "/data/products/dbId";
    public static final String DATA_GET_PRODUCT_BY_PRODUCTIDENTIFIER = "/data/products/productNumber";
    public static final String DATA_GET_PRODUCT_BY_ATTRIBUTES = "/data/products/getByAttributes";
    public static final String DATA_GET_DISCOUT_BY_DOCUMENT_ID = "/data/discounts/dbId";
    public static final String DATA_GET_PAYMENTMETHODS = "/data/paymentMethods";
    public static final String DATA_GET_PAYMENTMETHOD_BY_GUID = "/data/paymentMethods/guid";
    public static final String DATA_GET_STORE_BY_AUTHTOKEN_AND_DEVICEID = "/data/stores/getByUserAndDevice";
    public static final String DATA_GET_STORE_BY_USERID_AND_DEVICEID = "/data/stores/getByUserIdAndDeviceId";
    public static final String DATA_GET_STORE_BY_CONTROLLER_PLACED = "/data/stores/getByControllerPlaced";
    public static final String DATA_GET_STORE_BY_GUID = "/data/stores/getByGuid";
    public static final String DATA_GET_STORE_BY_USER = "/data/stores/findByUser";
    public static final String DATA_GET_STORE_BY_USER_ID = "/data/stores/findByUserId";
    public static final String DATA_POST_REDUCT_STOCK_AMOUNT = "/data/products/reduceStockAmountForProductGuid";

    public static final String DATA_ADD_COMPANY = "/data/companies";

    public static final String DATA_CURRENT_VERSION = "/data/getCurrentVersion";

    public static final String DESIGN_CURRENT_VERSION = "/design/getCurrentVersion";
    public static final String EMAIL_SEND_CONFIRMATION_EMAIL = "/email/sendConfirmationEmail";

    public static final String EMAIL_CURRENT_VERSION = "email/getCurrentVersion";
    public static final String PRINTER_SOCKET = "/printer/printing/socket";
    public static final String PRINTER_GETINVOICETEXT = "/printer/printing/returnToPrintText";
    public static final String PRINTER_PRINT_REVERTED_INVOICE = "/printer/printing/printRevertedInvoice";
    public static final String PRINTER_PRINTTEXT = "/printer/printing/printText";
    public static final String PRINTER_PRINTTEXT_ESC = "/printer/printing/printTextEsc";

    public static final String PRINTER_REPRINT_INVOICE = "/printer/printing/reprintInvoice";
    public static final String PRINTER_CURRENT_VERSION = "/printer/getCurrentVersion";
    public static final String REPORT_PRINT_INVOICE = "/report/invoiceReports/generateInvoice";

    public static final String REPORT_REVERT_INVOICE = "/report/invoiceReports/printRevertedInvoice";
    public static final String REPORT_CURRENT_VERSION = "/report/getCurrentVersion";
    public static final String SOCKET_CLIENT_INFORMATION = "/socket/clientInformation";
    public static final String SOCKET_DEVICE_DEFAULT_PRINTER = "/socket/device/findDefaultPrinterOfDevice";
    public static final String SOCKET_DEVICE_DEVICEID = "/socket/device/deviceId";
    public static final String SOCKET_FIND_BY_STOREGUID = "/socket/device/storeGuid";
    public static final String SOCKET_SEND_TO_ALL = "/socket/sendToAll";
    public static final String SOCKET_SEND_TO_USER = "/socket/sendToUser";
    public static final String SOCKET_SEND_TO_DEVICE = "/socket/sendToDevice";
    public static final String SOCKET_DEVICE_ADDSTORETODEVICE = "/socket/device/addStoreToDevice";
    public static final String SOCKET_CURRENT_VERSION = "/socket/getCurrentVersion";

    public static final String SOCKET_GET_STORE_BY_DEVICE = "/socket/device/getStoreByDevice";
    public static final String SOCKET_SET_CASHIER_FOR_WORKSTATION = "/socket/device/setCashierForWorkstation";
    public static final String SYNC_SYNCCHANGES = "/sync/syncChanges";

    public static final String SYNC_SYNCCHANGESACTION = "/sync/syncChangesAction";
    public static final String SYNC_CURRENT_VERSION = "/sync/getCurrentVersion";
    public static final String POS_DAILYSETTLEMENT_IS_CREATION_ALLOWED = "/pos/dailySettlements/isActionAllowed";
    public static final String POS_CASHBOOK_CURRENT_CASH_STATUS = "/pos/cashbook/getCurrentCashStatus";
    public static final String POS_FIND_BY_CREATION_DATE_BETWEEN = "/pos/invoices/findByCreationDateBetween";
    public static final String POS_FIND_BY_CREATION_DATE = "/pos/invoices/findByCreationDate";
    public static final String POS_FIND_DAILYSETTLEMENTS_BY_STOREGUID_AND_DATETIME_BETWEEN = "/pos/dailySettlements/findDailySettlementsByStoreGuidAndDateTimeBetween";
    public static final String POS_FIND_LAST_CLOSED_DAILY_SETTLEMENT_BY_STORE_GUID = "/pos/dailySettlements/lastClosedByStoreGuid";
    public static final String POS_FIND_LAST_MONTHLY_CLOSED_DAILY_SETTLEMENT_BY_STORE_GUID = "/pos/dailySettlements/lastMonthlyClosedByStoreGuid";
    public static final String POS_TIMETASK_DELETE_BY_STORE_GUID = "/pos/timeTask/deleteByStoreGuid";
    public static final String POS_TIMETASK_ADD_DAILY_SETTLEMENT_DATETIME = "/pos/timeTask/addDailySettlementDateTime";

    public static final String POS_CURRENT_VERSION = "/pos/getCurrentVersion";
    public static final String POS_ORDER_GET_DEFAULT_CASHIER_ID = "/pos/orders/getDefaultCashierId";
    public static final String REPORT_EXECUTE_DAILYSETTLEMENT = "/report/dailySettlementReports/execute";
    public static final String REPORT_PREVIEW_FINANCIAL_REPORT = "/report/dailySettlementReports/previewFinancialReport";
    public static final String REPORT_PRINT_FINANCIAL_REPORT = "/report/financialReports/printFinancialReport";
    public static final String REPORT_PRINT_MONTHLY_REPORT = "/report/financialReports/printMonthlyReport";
    public static final String REPORT_PRINT_CASHBOOK_ENTRY = "/report/cashbookEntry/print";
    public static final String SIGNATURE_CREATE_START_INVOICE = "/signature/signatures/createStartInvoice";
    public static final String SIGNATURE_CREATE_ZERO_INVOICE = "/signature/signatures/createNullInvoiceWithoutAuthToken";
    public static final String SIGNATURE_SEND_DEP = "/signature/dep/exportAndSend";
    public static final String SIGNATURE_IS_SAMMELBELEG_REQUIRED = "/signature/signatures/isSammelbelegRequired";
    public static final String SIGNATURE_IS_SAMMELBELEG_REQUIRED_AND_SMARTCARD_AVAILABLE = "/signature/signatures/isSammelbelegRequiredAndDeviceAvailable";
    public static final String SIGNATURE_SIGN_INVOICE = "/signature/signatures/signInvoice";
    public static final String SIGNATURE_IS_SIGNATURE_ENABLED = "/signature/signatures/isSignatureEnabled/{storeGuid}";
    public static final String SIGNATURE_IS_SMARTCARD_AVAILABLE = "/signature/signatures/isSignatureDeviceAvailable";
    public static final String SIGNATURE_IS_UP_AND_RUNNING = "/signature/signatures/isUpAndRunning";
    public static final String POS_EXTERNAL_INVOICE = "/pos/invoices/externalInvoice";

//    public static final String REPORT_EXECUTE_DAILYSETTLEMNT_WITH_STORE = "/report/dailySettlementReports/execute";

    public static String baseUrl(HttpServletRequest request) {
        return request.getScheme() + "://" + request.getServerName() + ":8080";
    }
}