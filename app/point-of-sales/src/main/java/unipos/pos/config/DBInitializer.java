package unipos.pos.config;

import com.mongodb.Mongo;
import lombok.extern.java.Log;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.modelmapper.ModelMapper;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Component;
import unipos.authChecker.interceptors.AuthenticationManager;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.data.mongodb.core.query.Criteria.where;

/**
 * Created by ggradnig on 2015-04-28
 */

public class DBInitializer
{
    private static Logger logger = LoggerFactory.getLogger(DBInitializer.class);

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    @Qualifier("mongo")
    Mongo mongo;
    @Autowired
    @Qualifier("mongoStores")
    Mongo mongoStores;
    @Autowired
    @Qualifier("mongoSignatures")
    Mongo mongoSignatures;
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    AuthenticationManager authenticationManager;

    private ExecutorService executorService;

    @PostConstruct
    public void init()
    {
        try {
//            initDatabase();
            BasicThreadFactory factory = new BasicThreadFactory.Builder()
                    .namingPattern("myspringbean-thread-%d").build();

            executorService =  Executors.newSingleThreadExecutor(factory);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // do something
                        authenticationManager.propagateAvailablePermissions();
                    } catch (Exception e) {
                    }
                }
            });
            executorService.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @PreDestroy
    public void beanDestroy() {
        if (executorService != null) {
            executorService.shutdownNow();
        }
    }

    @PreDestroy
    public void destoryMongoDbConnection() throws SchedulerException {
        mongo.close();
        mongoStores.close();
        mongoSignatures.close();
        schedulerFactoryBean.destroy();
    }

    void initDatabase()
    {

//        Order order;
//
//        ProductOrderItem productOrderItem;
//        ReversalOrderItem reversalOrderItem;
//        OrderItemDiscountOrderItem orderItemDiscountOrderItem;
//        OrderDiscountOrderItem orderDiscountOrderItem;
//        PaymentOrderItem paymentOrderItem;
//
//        logger.info("Initializing Database with sample data");
//
//        mongoTemplate.insert(SequenceId.builder()
//                .id("ORDER")
//                .seq(0L)
//                .build());
//
//        mongoTemplate.insert(SequenceId.builder()
//                .id("INVOICE")
//                .seq(0L)
//                .build());
//
//        productOrderItem = new ProductOrderItem();
//        productOrderItem.setLabel("Wiener Schnitzel");
//        productOrderItem.setOrderItemId("productOrderItemId");
//        productOrderItem.setProductNumber("productNumber1");
//        productOrderItem.setPosition(1);
//        productOrderItem.setTurnover(new BigDecimal("9.99"));
//        productOrderItem.setQuantity(1);
//        productOrderItem.setActionId("actionProductOrderItem");
//        productOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(5));
//        productOrderItem.setId("documentId");
//        productOrderItem.setServerReceiveTime(LocalDateTime.now());
//        productOrderItem.setOrderId("orderId");
//
//        reversalOrderItem = new ReversalOrderItem();
//        reversalOrderItem.setReason("TestReversal0Euro");
//        reversalOrderItem.setOrderItemId("reversalOrderItemId");
//        reversalOrderItem.setOrderId("orderId");
//        reversalOrderItem.setServerReceiveTime(LocalDateTime.now());
//        reversalOrderItem.setReversalAmount(new BigDecimal("0.00"));
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
//        orderItemDiscountOrderItem.setDiscount(new BigDecimal("1"));
//        orderItemDiscountOrderItem.setDiscountId("discountId2");
//        orderItemDiscountOrderItem.setId("documentId");
//        orderItemDiscountOrderItem.setReceiverOrderItemId("productOrderItemId");
//        orderItemDiscountOrderItem.setOrderItemId("orderItemDiscountOrderItem");
//        orderItemDiscountOrderItem.setServerReceiveTime(LocalDateTime.now());
//        orderItemDiscountOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(3));
//        orderItemDiscountOrderItem.setPosition(3);
//        orderItemDiscountOrderItem.setLabel("1 Euro Rabatt");
//        orderItemDiscountOrderItem.setOrderId("orderId");
//
//        orderDiscountOrderItem = new OrderDiscountOrderItem();
//        orderDiscountOrderItem.setActionId("actionOrderDiscountOrderItem");
//        orderDiscountOrderItem.setDiscountId("discountId");
//        orderDiscountOrderItem.setDiscount(new BigDecimal("1"));
//        orderDiscountOrderItem.setOrderItemId("orderDiscountOrderItemId");
//        orderDiscountOrderItem.setServerReceiveTime(LocalDateTime.now());
//        orderDiscountOrderItem.setReceiverOrderId("orderId");
//        orderDiscountOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(4));
//        orderDiscountOrderItem.setPosition(4);
//        orderDiscountOrderItem.setId("documentId");
//        orderDiscountOrderItem.setLabel("1 Euro Rabatt");
//        orderDiscountOrderItem.setOrderId("orderId");
//
//        paymentOrderItem = new PaymentOrderItem();
//        paymentOrderItem.setOrderId("orderId");
//        paymentOrderItem.setOrderItemId("paymentOrderItemId");
//        paymentOrderItem.setPosition(5);
//        paymentOrderItem.setClientCreationDate(LocalDateTime.now().minusMinutes(2));
//        paymentOrderItem.setServerReceiveTime(LocalDateTime.now());
//        paymentOrderItem.setTurnover(new BigDecimal("7.99"));
//        paymentOrderItem.setPaymentMethod("paymentMethodId");
//        paymentOrderItem.setId("documentId");
//
//
//        order = new Order();
//        order.setOpen(true);
//        order.setCreatorWorker(Worker.builder()
//                .deviceID("deviceId")
//                .storeID("storeId")
//                .userID("userId")
//                .build());
//        order.setCashier(Cashier.builder()
//                .workerId("1")
//                .name("Dominik")
//                .build());
//        order.setClientCreationDate(LocalDateTime.now().minusMinutes(5));
//        order.setServerReceiveDate(LocalDateTime.now());
//        order.setId("orderId");
//        order.setIsActive(true);
//        order.setOrderNumber(1L);
//        order.setOrderId("joyceOrderId");
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
//
//        mongoTemplate.save(order);
//
//        OrderItemDiscountInvoiceItem orderItemDiscountInvoiceItem = ModelMapperGetter.getModelMapperWithoutIdMapping().map(orderItemDiscountOrderItem, OrderItemDiscountInvoiceItem.class);
//        OrderDiscountInvoiceItem orderDiscountInvoiceItem = ModelMapperGetter.getModelMapperWithoutIdMapping().map(orderDiscountOrderItem, OrderDiscountInvoiceItem.class);
//        PaymentInvoiceItem paymentInvoiceItem = ModelMapperGetter.getModelMapperWithoutIdMapping().map(paymentOrderItem, PaymentInvoiceItem.class);
//        ProductInvoiceItem productInvoiceItem = ModelMapperGetter.getModelMapperWithoutIdMapping().map(productOrderItem, ProductInvoiceItem.class);
//        ReversalInvoiceItem reversalInvoiceItem = ModelMapperGetter.getModelMapperWithoutIdMapping().map(reversalOrderItem, ReversalInvoiceItem.class);
//
//        productInvoiceItem.setTaxRate(20);
//        productInvoiceItem.calcTaxAndGross();
//        orderItemDiscountInvoiceItem.generateHash();
//        orderDiscountInvoiceItem.generateHash();
//        paymentInvoiceItem.generateHash();
//        productInvoiceItem.generateHash();
//        reversalInvoiceItem.generateHash();
//
//        Invoice invoice = Invoice.builder()
//                .cashier(order.getCashier())
//                .invoiceItems(Arrays.asList(
//                        productInvoiceItem,
//                        reversalInvoiceItem,
//                        orderItemDiscountInvoiceItem,
//                        orderDiscountInvoiceItem,
//                        paymentInvoiceItem
//                ))
//                .turnoverGross(new BigDecimal("7.99"))
//                .turnoverNet(new BigDecimal("6.66"))
//                .creationDate(LocalDateTime.now())
//                .hash("hash")
//                .invoiceId(1L)
//                .build();
//
//        mongoTemplate.save(invoice);
    }
}