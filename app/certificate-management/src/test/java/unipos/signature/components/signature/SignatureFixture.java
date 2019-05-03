package unipos.signature.components.signature;

import unipos.common.remote.data.model.company.Address;
import unipos.common.remote.data.model.company.Company;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.data.model.product.Category;
import unipos.common.remote.data.model.product.Product;
import unipos.common.remote.data.model.product.TaxRate;
import unipos.common.remote.pos.model.*;
import unipos.integritySafeGuard.smartcard.SmartCard;
import unipos.integritySafeGuard.smartcard.SmartCardImpl;
import unipos.integritySafeGuard.smartcard.SmartCardMock;
import unipos.signature.config.Fixture;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * Created by domin on 22.12.2016.
 */
public class SignatureFixture implements Fixture {

    public Invoice invoice;
    public SmartCard smartCard;
    public ProductInvoiceItem p1t20 = new ProductInvoiceItem();
    public ProductInvoiceItem p2t20 = new ProductInvoiceItem();
    public ProductInvoiceItem p3t10 = new ProductInvoiceItem();
    public TaxInvoiceItem t1t20 = new TaxInvoiceItem();
    public TaxInvoiceItem t2t10 = new TaxInvoiceItem();
    public PaymentInvoiceItem bar100 = new PaymentInvoiceItem();
    public ChangeInvoiceItem changeBar = new ChangeInvoiceItem();


    @Override
    public void setUp() {
        //TaxItem1
        t1t20.setAmountGross(new BigDecimal("15.00"));
        t1t20.setAmountNet(new BigDecimal("13.00"));
        t1t20.setAmountTax(new BigDecimal("2.00"));
        t1t20.setTaxRate(20);
        t1t20.setTaxRateId("taxGuid");

        //TaxItem2
        t2t10.setAmountGross(new BigDecimal("50.00"));
        t2t10.setAmountNet(new BigDecimal("45.00"));
        t2t10.setAmountTax(new BigDecimal("5.00"));
        t2t10.setTaxRate(10);
        t2t10.setTaxRateId("taxGuid2");

        //PaymentInvoiceItem 1
        bar100.setBarumsatz(true);
        bar100.setLabel("Bar");
        bar100.setPaymentMethod("paymentMethodGuid");
        bar100.setPaymentMethodGuid("paymentMethodGuid");
        bar100.setTurnover(new BigDecimal("100"));

        changeBar.setTurnover(new BigDecimal("50"));
        changeBar.setLabel("Bar");

        //ProductInvoiceItem 1
        p1t20.setTaxRate(20);
        p1t20.setTax(new BigDecimal("2.00"));
        p1t20.setDiscounts(new ArrayList<>());
        p1t20.setLabel("Würstel");
        p1t20.setOrderItemId("orderItemId");
        p1t20.setPrice(new BigDecimal("10.00"));
        p1t20.setProduct(Product.builder()
                .guid("wuerstelId")
                .attributes(new ArrayList<>())
                .category(Category.builder()
                        .name("Nahrung")
                        .taxRate(new TaxRate())
                        .build())
                .customPriceInputAllowed(false)
                .description("Würstel mit irgendeiner Soße")
                .number(1L)
                .price(new BigDecimal("10.00"))
                .productIdentifier(1L)
                .stockAmount(100)
                .stores(Collections.singletonList("storeGuid"))
                .build());
        p1t20.setQuantity(1);
        p1t20.setReversalApplied(false);
        p1t20.setTurnoverGross(new BigDecimal("10.00"));
        p1t20.setTurnoverNet(new BigDecimal("8.00"));
        p1t20.setPosition(1);


        //Product 2
        p2t20.setTaxRate(20);
        p2t20.setTax(new BigDecimal("1.00"));
        p2t20.setDiscounts(new ArrayList<>());
        p2t20.setLabel("Wurstsemmel");
        p2t20.setOrderItemId("orderItemId2");
        p2t20.setPrice(new BigDecimal("5.00"));
        p2t20.setProduct(Product.builder()
                .guid("wuerstelId")
                .attributes(new ArrayList<>())
                .category(Category.builder()
                        .name("Nahrung")
                        .taxRate(new TaxRate())
                        .build())
                .customPriceInputAllowed(false)
                .description("Semmel mit irgendeiner Wurst")
                .number(1L)
                .price(new BigDecimal("5.00"))
                .productIdentifier(2L)
                .stockAmount(50)
                .stores(Collections.singletonList("storeGuid"))
                .build());
        p2t20.setQuantity(1);
        p2t20.setReversalApplied(false);
        p2t20.setTurnoverGross(new BigDecimal("5.00"));
        p2t20.setTurnoverNet(new BigDecimal("4.00"));
        p2t20.setPosition(2);

        //Product 2
        p3t10.setTaxRate(10);
        p3t10.setTax(new BigDecimal("5.00"));
        p3t10.setDiscounts(new ArrayList<>());
        p3t10.setLabel("Wurstsemmel");
        p3t10.setOrderItemId("orderItemId2");
        p3t10.setPrice(new BigDecimal("50.00"));
        p3t10.setProduct(Product.builder()
                .guid("Fallout 4")
                .attributes(new ArrayList<>())
                .category(Category.builder()
                        .name("Games")
                        .taxRate(new TaxRate())
                        .build())
                .customPriceInputAllowed(false)
                .description("Fallout 4 STEAM KEY")
                .number(1L)
                .price(new BigDecimal("50.00"))
                .productIdentifier(3L)
                .stockAmount(30)
                .stores(Collections.singletonList("storeGuid"))
                .build());
        p3t10.setQuantity(1);
        p3t10.setReversalApplied(false);
        p3t10.setTurnoverGross(new BigDecimal("50.00"));
        p3t10.setTurnoverNet(new BigDecimal("45.00"));
        p3t10.setPosition(3);

        //Basic Invoice Setup
        invoice = new Invoice();
        invoice.setTurnoverGross(new BigDecimal("15.00"));
        invoice.setTurnoverNet(new BigDecimal("12.00"));
        invoice.setCashier(Cashier.builder()
                .name("Dominik")
                .userId("1")
                .build());
        invoice.setCompany(Company.builder()
                .name("Unipos IT Solutions")
                .commercialRegisterNumber("AT123456789")
                .guid("storeGuid")
                .stores(Collections.singletonList(Store.builder()
                        .address(Address.builder()
                                .city("Wien")
                                .country("Österreich")
                                .postCode(1130)
                                .street("Lafitegasse 26C")
                                .build())
                        .closeHour(LocalDateTime.of(2016, 1, 1, 23, 0, 0))
                        .companyGuid("storeGuid")
                        .controllerStore(true)
                        .email("dominik.schiener@unipos.at")
                        .guid("storeGuid")
                        .storeId(1L)
                        .build()))
                .uid("AT987654321")
                .build());
        invoice.setCreationDate(LocalDateTime.of(2016,12,22,5,11,24));
        invoice.setDeviceId("deviceId");
        invoice.setGuid("guid");
        invoice.setInvoiceId(1L);
        invoice.setInvoiceType(Invoice.InvoiceType.invoice);
        invoice.setStore(Store.builder()
                .address(Address.builder()
                        .city("Wien")
                        .country("Österreich")
                        .postCode(1130)
                        .street("Lafitegasse 26C")
                        .build())
                .closeHour(LocalDateTime.of(2016, 1, 1, 23, 0, 0))
                .companyGuid("storeGuid")
                .controllerStore(true)
                .email("dominik.schiener@unipos.at")
                .guid("storeGuid")
                .storeId(1L)
                .build());
        invoice.setInvoiceItems(Arrays.asList(p1t20, p2t20, t1t20));

        //SmartCard Setup
        smartCard = new SmartCardMock("12-34-56-78-90-12");
    }

    public Invoice getInvoice1() {
        Invoice invoice1 = new Invoice();
        invoice1.setTurnoverGross(new BigDecimal("15.00"));
        invoice1.setTurnoverNet(new BigDecimal("12.00"));
        invoice1.setCashier(Cashier.builder()
                .name("Dominik")
                .userId("1")
                .build());
        invoice1.setCompany(Company.builder()
                .name("Unipos IT Solutions")
                .commercialRegisterNumber("AT123456789")
                .guid("storeGuid")
                .stores(Collections.singletonList(Store.builder()
                        .address(Address.builder()
                                .city("Wien")
                                .country("Österreich")
                                .postCode(1130)
                                .street("Lafitegasse 26C")
                                .build())
                        .closeHour(LocalDateTime.of(2016, 1, 1, 23, 0, 0))
                        .companyGuid("storeGuid")
                        .controllerStore(true)
                        .email("dominik.schiener@unipos.at")
                        .guid("storeGuid")
                        .storeId(1L)
                        .build()))
                .uid("AT987654321")
                .build());
        invoice1.setCreationDate(LocalDateTime.of(2016,12,22,5,11,24));
        invoice1.setDeviceId("deviceId");
        invoice1.setGuid("guid");
        invoice1.setInvoiceId(1L);
        invoice1.setInvoiceType(Invoice.InvoiceType.invoice);
        invoice1.setStore(Store.builder()
                .address(Address.builder()
                        .city("Wien")
                        .country("Österreich")
                        .postCode(1130)
                        .street("Lafitegasse 26C")
                        .build())
                .closeHour(LocalDateTime.of(2016, 1, 1, 23, 0, 0))
                .companyGuid("storeGuid")
                .controllerStore(true)
                .email("dominik.schiener@unipos.at")
                .guid("storeGuid")
                .storeId(1L)
                .build());
        invoice1.setInvoiceItems(Arrays.asList(p1t20, p2t20, p3t10, t1t20, t2t10, bar100, changeBar));

        return invoice1;
    }

    @Override
    public void tearDown() {

    }
}
