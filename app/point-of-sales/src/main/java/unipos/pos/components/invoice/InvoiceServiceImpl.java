package unipos.pos.components.invoice;

import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.User;
import unipos.common.remote.core.LogRemoteInterface;
import unipos.common.remote.core.model.LogDto;
import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.PaymentMethod;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.feedback.FeedbackRemoteInterface;
import unipos.common.remote.printer.PrinterRemoteInterface;
import unipos.common.remote.report.ReportRemoteInterface;
import unipos.common.remote.signature.SignatureRemoteInterface;
import unipos.common.remote.signature.model.SignatureResult;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.common.remote.socket.model.Workstation;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;
import unipos.licenseChecker.component.LicenseChecker;
import unipos.licenseChecker.component.LicenseVerification;
import unipos.licenseChecker.component.exception.LicenseErrorException;
import unipos.pos.components.cashbook.CashbookEntry;
import unipos.pos.components.cashbook.CashbookEntryService;
import unipos.pos.components.dailySettlement.*;
import unipos.pos.components.invoice.exception.AskJoyceForCrackException;
import unipos.pos.components.invoice.exception.InvoiceCreationException;
import unipos.pos.components.invoice.model.*;
import unipos.pos.components.invoice.model.exception.ReversalInvoiceCreationException;
import unipos.pos.components.invoice.model.reversalInvoice.ReversalInvoice;
import unipos.pos.components.invoice.model.reversalInvoice.ReversalInvoiceRepository;
import unipos.pos.components.order.Cashier;
import unipos.pos.components.order.Order;
import unipos.pos.components.order.OrderRepository;
import unipos.pos.components.order.tag.OrderTag;
import unipos.pos.components.orderItem.model.OrderItem;
import unipos.pos.components.orderItem.model.OrderItemVisitor.OrderItemVisitor;
import unipos.pos.components.sequence.SequenceRepository;
import unipos.pos.signature.SignatureResultRepository;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by dominik on 04.09.15.
 */

@Service
public class InvoiceServiceImpl implements InvoiceService {

    @Autowired
    OrderRepository orderRepository;
    @Autowired
    InvoiceRepository invoiceRepository;
    @Autowired
    SequenceRepository sequenceRepository;
    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    DailySettlementService dailySettlementService;
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    OrderItemVisitor visitor;
    @Autowired
    PrinterRemoteInterface printerRemoteInterface;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;
    @Autowired
    ReportRemoteInterface reportRemoteInterface;
    @Autowired
    Mapper mapper;
    @Autowired
    LogRemoteInterface logRemoteInterface;
    @Autowired
    SignatureRemoteInterface signatureRemoteInterface;
    @Autowired
    CashbookEntryService cashbookEntryService;
    @Autowired
    LicenseChecker licenseChecker;
    @Autowired
    ReversalInvoiceRepository reversalInvoiceRepository;
    @Autowired
    Environment environment;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    SignatureResultRepository signatureResultRepository;

    @Override
    public Invoice createInvoiceFromOrder(String orderId, String cashierUserId, HttpServletRequest request) throws InvoiceCreationException, SignatureException {

        String deviceToken = RequestHandler.getDeviceToken(request);
        Assert.notNull(deviceToken, "No Device Token found in the given Request");

        List<InvoiceItem> invoiceItems = new ArrayList<>();

        Order order = orderRepository.findFirstByOrderId(orderId);

        User cashier = authRemoteInterface.getUserByGuid(cashierUserId);
//        Workstation currentDevice = order.getCurrentDevice();
        Workstation currentDevice = socketRemoteInterface.findByDeviceId(deviceToken);

        Company company = dataRemoteInterface.getCompanyByGuid(cashier.getCompanyGuid());

        // LICENSE VERIFICATION
        if (false) {
            LicenseVerification licenseVerification = null;
            try {
                licenseVerification = licenseChecker.getLicenseInfos(company.getName());
            } catch (LicenseErrorException e) {
                LogDto log = LogDto.builder().dateTime(LocalDateTime.now()).level(LogDto.Level.ERROR).message(e.getMessage()).source(this.getClass().getSimpleName() + ":createInvoiceFromOder").build();
                log.addExceptionParameters(e);
                logRemoteInterface.log(log);
                throw new AskJoyceForCrackException(e.getMessage());
            } catch (BadPaddingException | IllegalBlockSizeException e) {
                LogDto log = LogDto.builder().dateTime(LocalDateTime.now()).level(LogDto.Level.ERROR).message(e.getMessage()).source(this.getClass().getSimpleName() + ":createInvoiceFromOder").build();
                log.addExceptionParameters(e);
                logRemoteInterface.log(log);
                throw new AskJoyceForCrackException("Not able to decrypt the licenseFile. Is your company name correct???");
            } catch (Exception e) {
                LogDto log = LogDto.builder().dateTime(LocalDateTime.now()).level(LogDto.Level.ERROR).message(e.getMessage()).source(this.getClass().getSimpleName() + ":createInvoiceFromOder").build();
                log.addExceptionParameters(e);
                logRemoteInterface.log(log);
                throw new AskJoyceForCrackException("No valid License found for the given Company name");
            }

            if (licenseVerification == null) {
                throw new AskJoyceForCrackException("No valid License found for the given Company name");
            }
        }

        Store store = dataRemoteInterface.getStoreByUserIdAndDeviceId(cashier.getGuid(), currentDevice.getDeviceId());

        boolean isSignatureForStoreEnabled = signatureResultRepository.countByInvoiceSignatureTypeAndStoreGuid(SignatureResult.Type.START, store.getGuid()) > 0L;

        if (isSignatureForStoreEnabled) {
            if (!signatureRemoteInterface.isSignatureModuleUpAndRunning()) {
                throw new SignatureException("SIGNATURE_MODULE_MISSING");
            }
            //Check if theres a sammelbeleg required and also if the signature device is available, before continueing
            if (signatureRemoteInterface.isSammelbelegRequiredAndSmartCardAvailable(request, store.getGuid())) {
                throw new SignatureException("SAMMELBELEG_REQUIRED");
            }
        }

        List<OrderItem> orderItemList = order.getOrderItems() != null ? order.getOrderItems() : new ArrayList<>();
        if (orderItemList.size() > 0) {

            for (OrderItem orderItem : orderItemList) {
                InvoiceItem invoiceItem = orderItem.accept(visitor);

                if (invoiceItem == null) {
                    throw new InvoiceCreationException("Mapper was not able to parse the orderItem: " + orderItem);
                }

                invoiceItems.add(invoiceItem);
            }

            //Remove all unnecessary stores from the company. Is just overhead
            if (company != null) {
                company.setStores(null);
                company.setUid("ATU00000000");
            }

            Invoice invoice = Invoice.builder()
                    .invoiceItems(invoiceItems)
                    .creationDate(LocalDateTime.now())
                    .guid(UUID.randomUUID().toString())
                    .company(company)
                    .orderId(orderId)
                    .store(store)
                    .deviceId(currentDevice.getDeviceId())
                    .signatureInvoiceType(isSignatureForStoreEnabled ? Invoice.SignatureInvoiceType.STANDARD : null)
                    .build();

            // Set the cashier to the passed user
            setInvoiceCashier(invoice, cashier.getGuid());
            setInvoiceTags(invoice, order.getOrderTags());

            //Create the invoice Sums
            invoice.calcInvoice();

            invoiceItems.stream().filter(x -> x instanceof PaymentInvoiceItem).map(x -> (PaymentInvoiceItem) x).filter(x -> x.getPaymentMethodGuid() != null && !x.getPaymentMethodGuid().isEmpty()).forEach(x -> {
                PaymentMethod paymentMethod = dataRemoteInterface.getPaymentMethodByGuid(x.getPaymentMethodGuid());
                if (paymentMethod != null && paymentMethod.getType() == PaymentMethod.Type.BAR && !invoice.getTurnoverGross().setScale(0, RoundingMode.HALF_UP).equals(BigDecimal.ZERO)) {
                    CashbookEntry cashbookEntry = CashbookEntry.builder()
                            .amount(invoice.getTurnoverGross())
                            .description("Rechnung wurde BAR bezahlt")
                            .reference(CashbookEntry.Reference.COMMERCIAL)
                            .type(CashbookEntry.Type.IN)
                            .storeGuid(store.getGuid())
                            .build();
                    cashbookEntryService.addCashbookEntry(cashbookEntry, store, cashier);
                }
            });


            Long sequenceId = sequenceRepository.getNextSequenceId("INVOICE" + invoice.getCreationDate().getYear() + "_" + String.valueOf(invoice.getStore().getStoreId()));
            invoice.setInvoiceId(sequenceId);

            invoice.generateHash();

            unipos.common.remote.pos.model.Invoice remoteInvoice = mapper.map(invoice, unipos.common.remote.pos.model.Invoice.class);

            ////SIGNATURESTUFF BEGIN
            //I still need to check the preconditions. Is there a signature Available, etc...

            List<PaymentMethod> paymentMethods = dataRemoteInterface.getPaymentMethods();

            if(signatureRemoteInterface.isSignatureForStoreEnabled(invoice.getStore().getGuid()) && invoice.containsBarumsatzPayment(paymentMethods)) {
                unipos.common.remote.pos.model.Invoice signedInvoice = signatureRemoteInterface.signInvoice(remoteInvoice);

                invoice.setUmsatzZaehler(signedInvoice.getUmsatzZaehler());
                invoice.setQrCode(signedInvoice.getQrCode());
                remoteInvoice.setUmsatzZaehler(signedInvoice.getUmsatzZaehler());
                remoteInvoice.setQrCode(signedInvoice.getQrCode());
            }
            ////SIGNATURESTUFF END

            invoiceRepository.save(invoice);

            order.setIsActive(false);
            orderRepository.save(order);

            try {
                FeedbackRemoteInterface feedbackRemoteInterface = new FeedbackRemoteInterface();
                feedbackRemoteInterface.createRating(invoice.getGuid(), invoice.getCompany().getGuid());
            }
            catch(Exception ex){
                // do nothing
            }

            try {
                syncRemoteInterface.syncChanges(invoice, Target.INVOICE, Action.CREATE);
            } catch (Exception e) {
                e.printStackTrace();
            }

            reportRemoteInterface.printInvoice(remoteInvoice, request);
            return invoice;
        } else {
            order.setIsActive(false);
            orderRepository.save(order);
            return null;
        }
    }

    private void setInvoiceCashier(Invoice invoice, String userId){
        User user = authRemoteInterface.getUserByGuid(userId);

        Cashier cashier = Cashier.builder()
                .name((user.getName() != null ? (user.getName() + " ") : "") + (user.getSurname() != null ? user.getSurname() : ""))
                .userId(String.valueOf(user.getUserId()))
                .userGuid(user.getGuid())
                .build();

        invoice.setCashier(cashier);
    }

    private void setInvoiceTags(Invoice invoice, List<OrderTag> orderTags){
        List<InvoiceTag> invoiceTags = new ArrayList<>();
        for(OrderTag orderTag : orderTags){
            InvoiceTag invoiceTag = InvoiceTag.builder().key(orderTag.getKey()).value(orderTag.getValue()).build();
            invoiceTags.add(invoiceTag);
        }

        invoice.setInvoiceTags(invoiceTags);
    }

    @Override
    public List<Invoice> findAll() {
        List<Invoice> invoices = invoiceRepository.findAll();

        invoices = invoices.stream().collect(Collectors.toList());
        return invoices;
    }

    @Override
    public Invoice findInvoice() {
        return invoiceRepository.findAll().get(0);
    }

    public List<Invoice> findByCreationDateBetween(LocalDateTime startDate, LocalDateTime endDate) {

        return invoiceRepository.findByCreationDateBetween(startDate, endDate);
    }

    public List<Invoice> findByCreationDate(LocalDateTime date) {
        LocalDateTime startDate = date.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = startDate.plusDays(1).minusNanos(1);

        return invoiceRepository.findByCreationDateBetween(startDate, endDate);
    }

    @Override
    public List<Invoice> findAllSinceLastClosedDailySettlement(String authToken, String deviceId) {
        Store store = dataRemoteInterface.getStoreByAuthtokenAndDeviceid(authToken, deviceId);

        unipos.pos.components.dailySettlement.DailySettlement lastDailySettlement = dailySettlementService.getLastClosedDailySettlementByStore(store.getGuid());
        LocalDateTime startDate = LocalDateTime.parse("1900-01-01T00:00:00");
        if(lastDailySettlement != null ){
            startDate = lastDailySettlement.getDateTime();
        }

        return findByCreationDateBetween(startDate, LocalDateTime.now()).stream().filter(x -> x.getStore().getGuid().equals(store.getGuid())).collect(Collectors.toList());
    }

    @Override
    public boolean isAlreadyTransformedIntoInvoice(String clientOrderId) {
        return invoiceRepository.countByOrderId(clientOrderId) > 0L;
    }

    @Override
    public Invoice revertInvoiceByGuid(String invoiceGuid, Company company, Store store, String deviceId, String authToken, HttpServletRequest request) throws IllegalArgumentException, ReversalInvoiceCreationException, SignatureException {
        boolean isSignatureForStoreEnabled = signatureResultRepository.countByInvoiceSignatureTypeAndStoreGuid(SignatureResult.Type.START, store.getGuid()) > 0L;

        if (isSignatureForStoreEnabled) {
            if (!signatureRemoteInterface.isSignatureModuleUpAndRunning()) {
                throw new SignatureException("The signature has already been enabled for the given store. But the signature Module is not up and running!");
            }
        }

        Invoice invoice = invoiceRepository.findByGuid(invoiceGuid);
        User cashier = authRemoteInterface.getAuthTokenByRequest(request).getUser();

        Assert.notNull(cashier, "No User found!");

        if (invoice == null) {
            throw new IllegalArgumentException("Unable to find an Invoice for the given InvoiceGuid!!!");
        }

        ReversalInvoice reversalInvoice;

        if (!invoice.isReverted()) {
            reversalInvoice = new ReversalInvoice();
            reversalInvoice.createReversalInvoiceFromInvoice(invoice);
            Long sequenceId = sequenceRepository.getNextSequenceId("INVOICE" + reversalInvoice.getCreationDate().getYear() + "_" + String.valueOf(reversalInvoice.getStore().getStoreId()));
            reversalInvoice.setInvoiceId(sequenceId);
            reversalInvoice.setDeviceId(deviceId);
            reversalInvoice.setSignatureInvoiceType(isSignatureForStoreEnabled ? Invoice.SignatureInvoiceType.STORNO : null);

            reversalInvoice.getInvoiceItems().stream().filter(x -> x instanceof PaymentInvoiceItem).map(x -> (PaymentInvoiceItem) x).filter(x -> x.getPaymentMethodGuid() != null && !x.getPaymentMethodGuid().isEmpty()).forEach(x -> {
                PaymentMethod paymentMethod = dataRemoteInterface.getPaymentMethodByGuid(x.getPaymentMethodGuid());
                if (paymentMethod != null && paymentMethod.getType() == PaymentMethod.Type.BAR && !invoice.getTurnoverGross().setScale(0, RoundingMode.HALF_UP).equals(BigDecimal.ZERO)) {
                    CashbookEntry cashbookEntry = CashbookEntry.builder()
                            .amount(invoice.getTurnoverGross())
                            .description("BAR-Rechnung wurde storniert")
                            .reference(CashbookEntry.Reference.COMMERCIAL)
                            .type(CashbookEntry.Type.OUT)
                            .storeGuid(store.getGuid())
                            .build();
                    cashbookEntryService.addCashbookEntry(cashbookEntry, store, cashier);
                }
            });

            ////SIGNATURESTUFF BEGIN
            //I still need to check the preconditions. Is there a signature Available, etc...

            List<PaymentMethod> paymentMethods = dataRemoteInterface.getPaymentMethods();

            if(signatureRemoteInterface.isSignatureForStoreEnabled(reversalInvoice.getStore().getGuid()) && invoice.containsBarumsatzPayment(paymentMethods)) {
                unipos.common.remote.pos.model.ReversalInvoice remoteReversalInvoice = mapper.map(reversalInvoice, unipos.common.remote.pos.model.ReversalInvoice.class);

                unipos.common.remote.pos.model.Invoice signedInvoice = signatureRemoteInterface.signInvoice(remoteReversalInvoice);

                reversalInvoice.setUmsatzZaehler(signedInvoice.getUmsatzZaehler());
                reversalInvoice.setQrCode(signedInvoice.getQrCode());
            }
            ////SIGNATURESTUFF END

            invoiceRepository.save(reversalInvoice);

            invoice.setReverted(true);

            invoiceRepository.save(invoice);
        } else {
            reversalInvoice = reversalInvoiceRepository.findByReversedInvoiceGuid(invoiceGuid);
        }

        if (reversalInvoice == null) {
            throw new ReversalInvoiceCreationException("Unable to create an ReversalInvoice for the given Invoice GUID");
        }

        unipos.common.remote.pos.model.ReversalInvoice remoteReversalInvoice = mapper.map(reversalInvoice, unipos.common.remote.pos.model.ReversalInvoice.class);

        reportRemoteInterface.revertInvoice(remoteReversalInvoice, request);
        invoice.getInvoiceItems().stream().filter(x -> x instanceof ProductInvoiceItem).map(x -> (ProductInvoiceItem)x).filter(x -> x.getProduct().getStockAmount() > -1).forEach(poi -> {
            poi.getProduct().setStockAmount(poi.quantity * -1);
            dataRemoteInterface.reduceStockAmountForProductGuid(poi.getProduct());
        });

        return reversalInvoice;
    }

    @Override
    public List<Invoice> findByCompanyGuid(String companyGuid) {
        return invoiceRepository.findByCompany_guid(companyGuid);
    }

    @Override
    public List<Invoice> findZeroInvoicesByCompanyGuid(String companyGuid) {
        List<Invoice> zeroInvoices = new ArrayList<>();
        zeroInvoices.addAll(invoiceRepository.findByCompany_guidAndSignatureInvoiceType(companyGuid, Invoice.SignatureInvoiceType.NULL));
        zeroInvoices.addAll(invoiceRepository.findByCompany_guidAndSignatureInvoiceType(companyGuid, Invoice.SignatureInvoiceType.START));
        zeroInvoices.addAll(invoiceRepository.findByCompany_guidAndSignatureInvoiceType(companyGuid, Invoice.SignatureInvoiceType.SCHLUSS));
        zeroInvoices.addAll(invoiceRepository.findByCompany_guidAndSignatureInvoiceType(companyGuid, Invoice.SignatureInvoiceType.MONATS));
        zeroInvoices.addAll(invoiceRepository.findByCompany_guidAndSignatureInvoiceType(companyGuid, Invoice.SignatureInvoiceType.JAHRES));
        return zeroInvoices;
    }

    @Override
    public List<Invoice> findByCompanyGuidAndSignatureType(String companyGuid, Invoice.SignatureInvoiceType signatureInvoiceType) {
        return invoiceRepository.findByCompany_guidAndSignatureInvoiceType(companyGuid, signatureInvoiceType);
    }

    @Override
    public List<TaxInvoiceItem> getTaxInvoiceItemsForOrder(String orderGuid) {
        List<InvoiceItem> invoiceItems = new ArrayList<>();
        Order order = orderRepository.findFirstByOrderId(orderGuid);

        List<OrderItem> orderItemList = order.getOrderItems() != null ? order.getOrderItems() : new ArrayList<>();

        if (orderItemList.size() > 0) {

            for (OrderItem orderItem : orderItemList) {
                InvoiceItem invoiceItem = orderItem.accept(visitor);

                if (invoiceItem == null) {
                    throw new InvoiceCreationException("Mapper was not able to parse the orderItem: " + orderItem);
                }

                invoiceItems.add(invoiceItem);
            }

            Invoice invoice = Invoice.builder()
                    .invoiceItems(invoiceItems)
                    .creationDate(LocalDateTime.now())
                    .guid(UUID.randomUUID().toString())
                    .orderId(orderGuid)
                    .build();

            //Create the invoice Sums
            invoice.calcInvoice();

            List<TaxInvoiceItem> taxInvoiceItems = invoice.getInvoiceItems().stream()
                    .filter(x -> x instanceof TaxInvoiceItem)
                    .map(x -> (TaxInvoiceItem)x)
                    .collect(Collectors.toList());

            return taxInvoiceItems;
        } else {
            return null;
        }
    }

    @Override
    public Invoice saveExternalInvoice(Invoice invoice, HttpServletRequest request) {
        Long sequenceId = sequenceRepository.getNextSequenceId("INVOICE" + invoice.getCreationDate().getYear() + "_" + String.valueOf(invoice.getStore().getStoreId()));
        invoice.setInvoiceId(sequenceId);

        invoice.generateHash();

        unipos.common.remote.pos.model.Invoice remoteInvoice = mapper.map(invoice, unipos.common.remote.pos.model.Invoice.class);

        invoiceRepository.save(invoice);

        try{
            reportRemoteInterface.printInvoice(remoteInvoice, request);
        }catch(Exception ex){

        }

        return invoice;
    }
}
