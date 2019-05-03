//package unipos.pos.components.invoice;
//
//import com.google.gson.Gson;
//import org.dozer.DozerBeanMapper;
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentMatcher;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.internal.util.reflection.Whitebox;
//import org.modelmapper.ModelMapper;
//import unipos.common.container.RequestHandler;
//import unipos.common.mapping.LocalDateTimeConverter;
//import unipos.common.remote.core.LogRemoteInterface;
//import unipos.common.remote.data.DataRemoteInterface;
//import unipos.common.remote.data.model.company.Company;
//import unipos.common.remote.data.model.company.Store;
//import unipos.common.remote.printer.PrinterRemoteInterface;
//import unipos.common.remote.report.ReportRemoteInterface;
//import unipos.common.remote.socket.model.Worker;
//import unipos.common.remote.sync.SyncRemoteInterface;
//import unipos.common.remote.sync.model.Action;
//import unipos.common.remote.sync.model.Syncable;
//import unipos.common.remote.sync.model.Target;
//import unipos.pos.components.invoice.exception.InvoiceCreationException;
//import unipos.pos.components.invoice.model.*;
//import unipos.pos.components.order.Cashier;
//import unipos.pos.components.order.Order;
//import unipos.pos.components.order.OrderRepository;
//import unipos.pos.components.orderItem.model.*;
//import unipos.pos.components.orderItem.model.OrderItemVisitor.OrderItemVisitor;
//import unipos.pos.components.sequence.SequenceRepository;
//import unipos.pos.components.shared.GSonHolder;
//import unipos.pos.components.shared.ModelMapperGetter;
//import unipos.pos.shared.AbstractServiceTest;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.anyString;
//import static org.mockito.Mockito.*;
//
///**
// * Created by dominik on 04.09.15.
// */
//public class InvoiceServiceTest extends AbstractServiceTest {
//
//
//    @InjectMocks
//    InvoiceService invoiceService = new InvoiceServiceImpl();
//    @Mock
//    OrderRepository orderRepository;
//    @Mock
//    InvoiceRepository invoiceRepository;
//    @Mock
//    SequenceRepository sequenceRepository;
//    @Mock
//    DataRemoteInterface dataRemoteInterface;
//    @Mock
//    OrderItemVisitor visitor;
//    @Mock
//    PrinterRemoteInterface printerRemoteInterface;
//    @Mock
//    ReportRemoteInterface reportRemoteInterface;
//    @Mock
//    SyncRemoteInterface syncRemoteInterface;
//    @Mock
//    RequestHandler requestHandler;
//    @Mock
//    LogRemoteInterface logRemoteInterface;
//
//    Order order;
//    ModelMapper modelMapper = ModelMapperGetter.getModelMapperWithoutIdMapping();
//
//    ProductOrderItem productOrderItem;
//    ReversalOrderItem reversalOrderItem;
//    OrderItemDiscountOrderItem orderItemDiscountOrderItem;
//    OrderDiscountOrderItem orderDiscountOrderItem;
//    PaymentOrderItem paymentOrderItem;
//
//    @Override
//    @Before
//    public void setUp(){
//        Gson gson = GSonHolder.serializeDateGson();
//
//        productOrderItem = new ProductOrderItem();
//        productOrderItem.setLabel("Wiener Schnitzel");
//        productOrderItem.setOrderItemId("productOrderItemId");
//        productOrderItem.setProductNumber("productNumber1");
//        productOrderItem.setPosition(1);
//        productOrderItem.setTurnover(new BigDecimal("7.49"));
//        productOrderItem.setQuantity(1);
//        productOrderItem.setActionId("actionProductOrderItem");
//        productOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(5));
//        productOrderItem.setId("documentId");
//        productOrderItem.setServerReceiveTime(LocalDateTime.now());
//        productOrderItem.setOrderId("orderId");
//
//        reversalOrderItem = new ReversalOrderItem();
//        reversalOrderItem.setReason("Unbeabsichtigtes bonieren");
//        reversalOrderItem.setOrderItemId("reversalOrderItemId");
//        reversalOrderItem.setOrderId("orderId");
//        reversalOrderItem.setServerReceiveTime(LocalDateTime.now());
//        reversalOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(5));
//        reversalOrderItem.setId("documentId");
//        reversalOrderItem.setPosition(2);
//        reversalOrderItem.setActionId("actionReverseOrderItem");
//        reversalOrderItem.setReceiverOrderItem("orderItemDocumentId");
//
//
//
//        orderItemDiscountOrderItem = new OrderItemDiscountOrderItem();
//        orderItemDiscountOrderItem.setActionId("actionOrderItemDiscountOrderItem");
//        orderItemDiscountOrderItem.setDiscount(new BigDecimal("10"));
//        orderItemDiscountOrderItem.setDiscountId("discountId2");
//        orderItemDiscountOrderItem.setId("documentId");
//        orderItemDiscountOrderItem.setReceiverOrderItemId("productOrderItemId");
//        orderItemDiscountOrderItem.setOrderItemId("orderItemDiscountOrderItem");
//        orderItemDiscountOrderItem.setServerReceiveTime(LocalDateTime.now());
//        orderItemDiscountOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(3));
//        orderItemDiscountOrderItem.setPosition(3);
//        orderItemDiscountOrderItem.setOrderId("orderId");
//
//        orderDiscountOrderItem = new OrderDiscountOrderItem();
//        orderDiscountOrderItem.setActionId("actionOrderDiscountOrderItem");
//        orderDiscountOrderItem.setDiscountId("discountId");
//        orderDiscountOrderItem.setDiscount(new BigDecimal("10"));
//        orderDiscountOrderItem.setOrderItemId("orderDiscountOrderItemId");
//        orderDiscountOrderItem.setServerReceiveTime(LocalDateTime.now());
//        orderDiscountOrderItem.setReceiverOrderId("orderId");
//        orderDiscountOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(4));
//        orderDiscountOrderItem.setPosition(4);
//        orderDiscountOrderItem.setId("documentId");
//        orderDiscountOrderItem.setOrderId("orderId");
//
//        paymentOrderItem = new PaymentOrderItem();
//        paymentOrderItem.setOrderId("orderId");
//        paymentOrderItem.setOrderItemId("paymentOrderItemId");
//        paymentOrderItem.setPosition(5);
//        paymentOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(2));
//        paymentOrderItem.setServerReceiveTime(LocalDateTime.now());
//        paymentOrderItem.setTurnover(new BigDecimal("12.10"));
//        paymentOrderItem.setPaymentMethod("paymentMethodId");
//        paymentOrderItem.setId("documentId");
//
//        order = new Order();
//        order.setOpen(true);
//        order.setCreatorWorker(Worker.builder()
//                .deviceID("deviceId")
//                .storeID("storeId")
//                .userID("userId")
//                .build());
//        order.setClientCreationDate(LocalDateTime.now().minusMinutes(5));
//        order.setServerReceiveDate(LocalDateTime.now());
//        order.setId("orderId");
//        order.setCashier(Cashier.builder().name("Dominik").workerId("1").build());
//        order.setIsActive(true);
//        order.setOrderNumber(1L);
//        order.setOrderItems(new ArrayList<>(Arrays.asList(
//                productOrderItem,
//                reversalOrderItem,
//                orderItemDiscountOrderItem,
//                orderDiscountOrderItem,
//                paymentOrderItem
//        )));
//        order.setId("documentId");
//        order.setActiveWorkers(Arrays.asList(Worker.builder()
//                .deviceID("deviceId")
//                .storeID("storeId")
//                .userID("userId")
//                .build()));
//        super.setUp();
//
//        gson.toJson(order);
//    }
//
//    @Test
//    public void testCreateInvoiceFromOrder() throws Exception {
//
//        DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList("dozerMapping.xml"));
//        mapper.setCustomConverters(Arrays.asList(new LocalDateTimeConverter()));
//
//        Whitebox.setInternalState(invoiceService, "mapper", mapper);
//
//        ProductInvoiceItem productInvoiceItem = new ProductInvoiceItem();
//        productInvoiceItem.setTurnoverNet(new BigDecimal("8.32"));
//        productInvoiceItem.setQuantity(1);
//        productInvoiceItem.setProductNumber("productDocumentId");
//        productInvoiceItem.setOrderItemId("productOrderItemId");
//        productInvoiceItem.setLabel("label");
//        productInvoiceItem.setTurnoverGross(new BigDecimal("29.99"));
//        productInvoiceItem.setId(null);
//        productInvoiceItem.setTax(new BigDecimal("1.67"));
//        productInvoiceItem.setTaxRate(20);
//        productInvoiceItem.setPosition(1);
//
//        doNothing().when(syncRemoteInterface).syncChanges(any(Syncable.class), any(Target.class), any(Action.class));
//        when(orderRepository.findFirstByOrderId(anyString())).thenReturn(order);
//        when(sequenceRepository.getNextSequenceId("INVOICE"+LocalDateTime.now().getYear()+ "_" + String.valueOf(2))).thenReturn(1L);
//        when(invoiceRepository.save(any(Invoice.class))).thenReturn(null);
//        when(visitor.visit(any(ProductOrderItem.class))).thenReturn(productInvoiceItem);
//        when(visitor.visit(any(ReversalOrderItem.class))).thenReturn(modelMapper.map(reversalOrderItem, ReversalInvoiceItem.class));
//        when(visitor.visit(any(OrderItemDiscountOrderItem.class))).thenReturn(modelMapper.map(orderItemDiscountOrderItem, OrderItemDiscountInvoiceItem.class));
//        when(visitor.visit(any(OrderDiscountOrderItem.class))).thenReturn(modelMapper.map(orderDiscountOrderItem, OrderDiscountInvoiceItem.class));
//        when(visitor.visit(any(PaymentOrderItem.class))).thenReturn(modelMapper.map(paymentOrderItem, PaymentInvoiceItem.class));
//
//        invoiceService.createInvoiceFromOrder("documentId", Company.builder().build(), Store.builder().guid("storeGuid").storeId(2L).build(), "deviceId");
//
//        verify(orderRepository, times(1)).findFirstByOrderId(eq("documentId"));
//        verify(sequenceRepository, times(1)).getNextSequenceId("INVOICE"+LocalDateTime.now().getYear()+ "_" + String.valueOf(2));
//        verify(invoiceRepository).save(argThat(new ArgumentMatcher<Invoice>() {
//            @Override
//            public boolean matches(Object argument) {
//                Invoice invoice = (Invoice)argument;
//                if(
//                        invoice.getTurnoverGross().equals(new BigDecimal("9.99"))
//                        && invoice.getTurnoverNet().equals(new BigDecimal("8.33"))
//                        && invoice.getCreationDate() instanceof LocalDateTime
//                        && invoice.getInvoiceItems().size() == 5
//                        && invoice.getHash() != null && !invoice.getHash().isEmpty()
//                        && invoice.getInvoiceId() == 1L
//                        && invoice.getCashier() != null
//                ) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }));
//    }
//
//    @Test
//    public void testCreateInvoiceFromOrder2() throws Exception {
//
//        DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList("dozerMapping.xml"));
//        mapper.setCustomConverters(Arrays.asList(new LocalDateTimeConverter()));
//
//        Whitebox.setInternalState(invoiceService, "mapper", mapper);
//
//        ProductInvoiceItem productInvoiceItem = new ProductInvoiceItem();
//        productInvoiceItem.setTurnoverNet(new BigDecimal("83.33"));
//        productInvoiceItem.setQuantity(1);
//        productInvoiceItem.setOrderItemId("productOrderItemId");
//        productInvoiceItem.setTaxRate(20);
//        productInvoiceItem.setProductNumber("productDocumentId");
//        productInvoiceItem.setLabel("label");
//        productInvoiceItem.setTurnoverGross(new BigDecimal("100.00"));
//        productInvoiceItem.setId(null);
//        productInvoiceItem.setTax(new BigDecimal("16.67"));
//        productInvoiceItem.setPosition(1);
//
//        doNothing().when(syncRemoteInterface).syncChanges(any(Syncable.class), any(Target.class), any(Action.class));
//        when(orderRepository.findFirstByOrderId(anyString())).thenReturn(order);
//        when(sequenceRepository.getNextSequenceId("INVOICE"+LocalDateTime.now().getYear()+ "_" + String.valueOf(2))).thenReturn(1L);
//        when(invoiceRepository.save(any(Invoice.class))).thenReturn(null);
//        when(visitor.visit(any(ProductOrderItem.class))).thenReturn(productInvoiceItem);
//        when(visitor.visit(any(ReversalOrderItem.class))).thenReturn(modelMapper.map(reversalOrderItem, ReversalInvoiceItem.class));
//        when(visitor.visit(any(OrderItemDiscountOrderItem.class))).thenReturn(modelMapper.map(orderItemDiscountOrderItem, OrderItemDiscountInvoiceItem.class));
//        when(visitor.visit(any(OrderDiscountOrderItem.class))).thenReturn(modelMapper.map(orderDiscountOrderItem, OrderDiscountInvoiceItem.class));
//        when(visitor.visit(any(PaymentOrderItem.class))).thenReturn(modelMapper.map(paymentOrderItem, PaymentInvoiceItem.class));
//        doNothing().when(reportRemoteInterface).printInvoice(any(unipos.common.remote.pos.model.Invoice.class));
//
//        invoiceService.createInvoiceFromOrder("documentId", Company.builder().build(), Store.builder().guid("storeGuid").storeId(2L).build(), "deviceId");
//
//        verify(orderRepository, times(1)).findFirstByOrderId(eq("documentId"));
//        verify(sequenceRepository, times(1)).getNextSequenceId("INVOICE"+LocalDateTime.now().getYear()+ "_" + String.valueOf(2));
//        verify(invoiceRepository).save(argThat(new ArgumentMatcher<Invoice>() {
//            @Override
//            public boolean matches(Object argument) {
//                Invoice invoice = (Invoice)argument;
//                if(
//                        invoice.getTurnoverNet().equals(new BigDecimal("66.67"))
//                                && invoice.getTurnoverGross().equals(new BigDecimal("80.00"))
//                                && invoice.getCreationDate() instanceof LocalDateTime
//                                && invoice.getInvoiceItems().size() == 5
//                                && invoice.getHash() != null && !invoice.getHash().isEmpty()
//                                && invoice.getInvoiceId() == 1L
//                                && invoice.getCashier() != null
//                        ) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }));
//    }
//
//    @Test
//    public void testCreateInvoiceFromOrder3() throws Exception {
//
//        DozerBeanMapper mapper = new DozerBeanMapper(Arrays.asList("dozerMapping.xml"));
//        mapper.setCustomConverters(Arrays.asList(new LocalDateTimeConverter()));
//
//        Whitebox.setInternalState(invoiceService, "mapper", mapper);
//
//        ProductInvoiceItem productInvoiceItem = new ProductInvoiceItem();
//        productInvoiceItem.setTurnoverNet(new BigDecimal("305.67"));
//        productInvoiceItem.setQuantity(1);
//        productInvoiceItem.setOrderItemId("productOrderItemId");
//        productInvoiceItem.setTaxRate(20);
//        productInvoiceItem.setProductNumber("productDocumentId");
//        productInvoiceItem.setLabel("label");
//        productInvoiceItem.setTurnoverGross(new BigDecimal("366.80"));
//        productInvoiceItem.setId(null);
//        productInvoiceItem.setTax(new BigDecimal("61.13"));
//        productInvoiceItem.setPosition(1);
//
//        doNothing().when(syncRemoteInterface).syncChanges(any(Syncable.class), any(Target.class), any(Action.class));
//        when(orderRepository.findFirstByOrderId(anyString())).thenReturn(order);
//        when(sequenceRepository.getNextSequenceId("INVOICE"+LocalDateTime.now().getYear()+ "_" + String.valueOf(2))).thenReturn(1L);
//        when(invoiceRepository.save(any(Invoice.class))).thenReturn(null);
//        when(visitor.visit(any(ProductOrderItem.class))).thenReturn(productInvoiceItem);
//        when(visitor.visit(any(ReversalOrderItem.class))).thenReturn(modelMapper.map(reversalOrderItem, ReversalInvoiceItem.class));
//        when(visitor.visit(any(OrderItemDiscountOrderItem.class))).thenReturn(modelMapper.map(orderItemDiscountOrderItem, OrderItemDiscountInvoiceItem.class));
//        when(visitor.visit(any(OrderDiscountOrderItem.class))).thenReturn(modelMapper.map(orderDiscountOrderItem, OrderDiscountInvoiceItem.class));
//        when(visitor.visit(any(PaymentOrderItem.class))).thenReturn(modelMapper.map(paymentOrderItem, PaymentInvoiceItem.class));
//
//        invoiceService.createInvoiceFromOrder("documentId", Company.builder().build(), Store.builder().guid("storeGuid").storeId(2L).build(), "deviceId");
//
//        verify(orderRepository, times(1)).findFirstByOrderId(eq("documentId"));
//        verify(sequenceRepository, times(1)).getNextSequenceId("INVOICE"+LocalDateTime.now().getYear()+ "_" + String.valueOf(2));
//        verify(invoiceRepository).save(argThat(new ArgumentMatcher<Invoice>() {
//            @Override
//            public boolean matches(Object argument) {
//                Invoice invoice = (Invoice)argument;
//                if(
//                        invoice.getTurnoverNet().equals(new BigDecimal("289.00"))
//                                && invoice.getTurnoverGross().equals(new BigDecimal("346.80"))
//                                && invoice.getCreationDate() instanceof LocalDateTime
//                                && invoice.getInvoiceItems().size() == 5
//                                && invoice.getHash() != null && !invoice.getHash().isEmpty()
//                                && invoice.getInvoiceId() == 1L
//                                && invoice.getCashier() != null
//                        ) {
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        }));
//    }
//
///*    @Test(expected = InvoiceCreationException.class)
//    public void testCreateInvoiceFromOrderOrderItemIdIsNull() throws Exception {
//
//        ProductInvoiceItem productInvoiceItem = new ProductInvoiceItem();
//        productInvoiceItem.setTurnoverNet(new BigDecimal("305.67"));
//        productInvoiceItem.setQuantity(1);
//        productInvoiceItem.setOrderItemId(null);
//        productInvoiceItem.setTaxRate(20);
//        productInvoiceItem.setProductNumber("productDocumentId");
//        productInvoiceItem.setLabel("label");
//        productInvoiceItem.setTurnoverGross(new BigDecimal("366.80"));
//        productInvoiceItem.setId(null);
//        productInvoiceItem.setTax(new BigDecimal("61.13"));
//        productInvoiceItem.setPosition(1);
//
//        when(orderRepository.findFirstByOrderId(anyString())).thenReturn(order);
//        when(sequenceRepository.getNextSequenceId("INVOICE")).thenReturn(1L);
//        when(invoiceRepository.save(any(Invoice.class))).thenReturn(null);
//        when(visitor.visit(any(ProductOrderItem.class))).thenReturn(productInvoiceItem);
//        when(visitor.visit(any(ReversalOrderItem.class))).thenReturn(modelMapper.map(reversalOrderItem, ReversalInvoiceItem.class));
//        when(visitor.visit(any(OrderItemDiscountOrderItem.class))).thenReturn(modelMapper.map(orderItemDiscountOrderItem, OrderItemDiscountInvoiceItem.class));
//        when(visitor.visit(any(OrderDiscountOrderItem.class))).thenReturn(modelMapper.map(orderDiscountOrderItem, OrderDiscountInvoiceItem.class));
//        when(visitor.visit(any(PaymentOrderItem.class))).thenReturn(modelMapper.map(paymentOrderItem, PaymentInvoiceItem.class));
//
//        invoiceService.createInvoiceFromOrder("documentId");
//    }*/
//
//    @Test(expected = InvoiceCreationException.class)
//    public void testCreateInvoiceFromOrderReceiverOrderIdIsNull() throws Exception {
//
//        orderItemDiscountOrderItem.setReceiverOrderItemId(null);
//
//        ProductInvoiceItem productInvoiceItem = new ProductInvoiceItem();
//        productInvoiceItem.setTurnoverNet(new BigDecimal("305.67"));
//        productInvoiceItem.setQuantity(1);
//        productInvoiceItem.setOrderItemId("productOrderItemId");
//        productInvoiceItem.setTaxRate(20);
//        productInvoiceItem.setProductNumber("productDocumentId");
//        productInvoiceItem.setLabel("label");
//        productInvoiceItem.setTurnoverGross(new BigDecimal("366.80"));
//        productInvoiceItem.setId(null);
//        productInvoiceItem.setTax(new BigDecimal("61.13"));
//        productInvoiceItem.setPosition(1);
//
//        when(orderRepository.findFirstByOrderId(anyString())).thenReturn(order);
//        when(sequenceRepository.getNextSequenceId("INVOICE")).thenReturn(1L);
//        when(invoiceRepository.save(any(Invoice.class))).thenReturn(null);
//        when(visitor.visit(any(ProductOrderItem.class))).thenReturn(productInvoiceItem);
//        when(visitor.visit(any(ReversalOrderItem.class))).thenReturn(modelMapper.map(reversalOrderItem, ReversalInvoiceItem.class));
//        when(visitor.visit(any(OrderItemDiscountOrderItem.class))).thenReturn(modelMapper.map(orderItemDiscountOrderItem, OrderItemDiscountInvoiceItem.class));
//        when(visitor.visit(any(OrderDiscountOrderItem.class))).thenReturn(modelMapper.map(orderDiscountOrderItem, OrderDiscountInvoiceItem.class));
//        when(visitor.visit(any(PaymentOrderItem.class))).thenReturn(modelMapper.map(paymentOrderItem, PaymentInvoiceItem.class));
//
//        invoiceService.createInvoiceFromOrder("documentId", null,null, "deviceId");
//    }
//
//    @Test(expected = InvoiceCreationException.class)
//    public void testCreateInvoiceFromOrderNullInsideTheInvoiceLists() throws Exception {
//
//        when(orderRepository.findFirstByOrderId(anyString())).thenReturn(order);
//        when(sequenceRepository.getNextSequenceId("INVOICE")).thenReturn(1L);
//        when(invoiceRepository.save(any(Invoice.class))).thenReturn(null);
//        when(visitor.visit(any(ProductOrderItem.class))).thenReturn(null);
//        when(visitor.visit(any(ReversalOrderItem.class))).thenReturn(modelMapper.map(reversalOrderItem, ReversalInvoiceItem.class));
//        when(visitor.visit(any(OrderItemDiscountOrderItem.class))).thenReturn(modelMapper.map(orderItemDiscountOrderItem, OrderItemDiscountInvoiceItem.class));
//        when(visitor.visit(any(OrderDiscountOrderItem.class))).thenReturn(modelMapper.map(orderDiscountOrderItem, OrderDiscountInvoiceItem.class));
//        when(visitor.visit(any(PaymentOrderItem.class))).thenReturn(modelMapper.map(paymentOrderItem, PaymentInvoiceItem.class));
//
//        invoiceService.createInvoiceFromOrder("documentId", null,null, "deviceId");
//    }
//
//    @Test(expected = InvoiceCreationException.class)
//    public void testCreateInvoiceFromOrderWithoutAnyProductInvoiceItem() throws Exception {
//
//        order.getOrderItems().remove(0);
//
//        when(orderRepository.findFirstByOrderId(anyString())).thenReturn(order);
//        when(sequenceRepository.getNextSequenceId("INVOICE")).thenReturn(1L);
//        when(invoiceRepository.save(any(Invoice.class))).thenReturn(null);
//        when(visitor.visit(any(ReversalOrderItem.class))).thenReturn(modelMapper.map(reversalOrderItem, ReversalInvoiceItem.class));
//        when(visitor.visit(any(OrderItemDiscountOrderItem.class))).thenReturn(modelMapper.map(orderItemDiscountOrderItem, OrderItemDiscountInvoiceItem.class));
//        when(visitor.visit(any(OrderDiscountOrderItem.class))).thenReturn(modelMapper.map(orderDiscountOrderItem, OrderDiscountInvoiceItem.class));
//        when(visitor.visit(any(PaymentOrderItem.class))).thenReturn(modelMapper.map(paymentOrderItem, PaymentInvoiceItem.class));
//
//        invoiceService.createInvoiceFromOrder("documentId", null,null, "deviceId");
//    }
//
//    @Test
//    public void testCreateInvoiceWhereEveryPropIsNull() throws Exception {
//        when((orderRepository.findFirstByOrderId(anyString()))).thenReturn(Order.builder()
//                .activeWorkers(null)
//                .orderId(null)
//                .orderItems(null)
//                .serverReceiveDate(null)
//                .open(false)
//                .clientCreationDate(null)
//                .id(null)
//                .creatorWorker(null)
//                .isActive(false)
//                .orderNumber(null)
//                .build());
//
//        when(sequenceRepository.getNextSequenceId("INVOICE")).thenReturn(2L);
//        when(invoiceRepository.save(any(Invoice.class))).thenReturn(null);
//        invoiceService.createInvoiceFromOrder("LOL", null,null, "deviceId");
//    }
//
//
//}
