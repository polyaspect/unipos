package unipos.report.components.financialReport;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.data.model.company.Store;
import unipos.common.remote.pos.PosRemoteInterface;
import unipos.common.remote.pos.model.*;
import unipos.common.remote.printer.model.ThermalPrinterLine;
import unipos.common.remote.printer.model.ThermalPrinterLineFormat;
import unipos.report.components.financialReportDay.FinancialReportDay;
import unipos.report.components.shared.helper.TaxHelper;
import unipos.report.components.thermalPrinterLineBuilder.Alignment;
import unipos.report.components.thermalPrinterLineBuilder.ThermalPrinterLineBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Created by Thomas on 16.01.2016.
 */
@Service
public class FinancialReportServiceImpl implements FinancialReportService {

    @Autowired
    PosRemoteInterface posRemoteInterface;
    @Autowired
    FinancialReport financialReport;

    private BigDecimal sumOfSales;



    @Override
    public FinancialReportDay generateFinancialReport(List<Invoice> invoices, Store store) {
        BigDecimal dailySales;
        List<PaymentInvoiceItem> paymentsList = new ArrayList<>();
        List<TaxHelper> taxList = new ArrayList<>();

        sumOfSales = new BigDecimal("0.00");

        dailySales = invoices.stream().map(Invoice::getTurnoverGross).reduce(new BigDecimal("0.00"), BigDecimal::add);
        invoices.stream().filter(invoice -> invoice.getInvoiceItems() != null && invoice.getInvoiceItems().size() > 0).forEach(invoice -> {
            invoice.getInvoiceItems().stream().filter(invoiceItem -> invoiceItem instanceof ProductInvoiceItem && !((ProductInvoiceItem) invoiceItem).isReversalApplied()).forEach(invoiceItem -> {
                ProductInvoiceItem productInvoiceItem = (ProductInvoiceItem) invoiceItem;
                sumOfSales = sumOfSales.add(productInvoiceItem.getPrice());
            });
            invoice.getInvoiceItems().stream().filter(invoiceItem -> invoiceItem instanceof TaxInvoiceItem).forEach(invoiceItem -> {
                TaxInvoiceItem taxInvoiceItem = (TaxInvoiceItem) invoiceItem;
                boolean result = taxList.stream().filter(taxHelper -> taxHelper.getTaxRate() == taxInvoiceItem.getTaxRate()).findFirst().map(taxHelper -> {
                    taxHelper.setBrutto(taxHelper.getBrutto().add(taxInvoiceItem.getAmountGross()));
                    taxHelper.setNetto(taxHelper.getNetto().add(taxInvoiceItem.getAmountNet()));
                    taxHelper.setMwst(taxHelper.getMwst().add(taxInvoiceItem.getAmountTax()));

                    return true;
                }).isPresent();
                if (!result) {
                    taxList.add(TaxHelper.builder().taxRate(taxInvoiceItem.getTaxRate()).brutto(taxInvoiceItem.getAmountGross()).netto(taxInvoiceItem.getAmountNet()).mwst(taxInvoiceItem.getAmountTax()).build());
                }
            });

            invoice.getInvoiceItems().stream().filter(invoiceItem -> invoiceItem instanceof PaymentInvoiceItem).forEach(invoiceItem -> {
                PaymentInvoiceItem paymentInvoiceItem = (PaymentInvoiceItem) invoiceItem;

                boolean result = paymentsList.stream().filter(payment -> payment.getLabel().equals(paymentInvoiceItem.getLabel())).findFirst().map(payment -> {
                    payment.setTurnover(payment.getTurnover().add(paymentInvoiceItem.getTurnover()));
                    return true;
                }).isPresent();

                if (!result) {
                    PaymentInvoiceItem payment = new PaymentInvoiceItem();
                    payment.setLabel(paymentInvoiceItem.getLabel());
                    payment.setTurnover(paymentInvoiceItem.getTurnover());
                    paymentsList.add(payment);
                }
            });

            invoice.getInvoiceItems().stream().filter(invoiceItem -> invoiceItem instanceof ChangeInvoiceItem).forEach(invoiceItem -> {
                ChangeInvoiceItem changeInvoiceItem = (ChangeInvoiceItem) invoiceItem;

                paymentsList.stream().filter(payment -> payment.getLabel().equals(changeInvoiceItem.getLabel())).findFirst().map(payment -> {
                    payment.setTurnover(payment.getTurnover().subtract(changeInvoiceItem.getTurnover()));
                    return true;
                });
            });
        });
        BigDecimal cashStatus = new BigDecimal("0.00");
        if (store != null){
            cashStatus = posRemoteInterface.getCurrendCashbookStatus(store);
        }
        return FinancialReportDay.builder().sumOfSales(sumOfSales).dailySales(dailySales).paymentsList(paymentsList).cashStatus(cashStatus).taxList(taxList).build();
    }

    @Override
    public List<ThermalPrinterLine> generateFinancialReportLines(FinancialReportDay financialReportDay, boolean monthly) {
        List<ThermalPrinterLine> thermalPrinterLines = new ArrayList<>();
        ThermalPrinterLineBuilder thermalPrinterLineBuilder = new ThermalPrinterLineBuilder(48);
        thermalPrinterLineBuilder.setAlignment(0, Alignment.CENTER);
        if(monthly){
            thermalPrinterLineBuilder.setContent("Monatsabschluss");
        }else{
            thermalPrinterLineBuilder.setContent("Tagesabschluss");
        }
        thermalPrinterLineBuilder.addFormat(ThermalPrinterLineFormat.DOUBLE_HEIGHT);
        thermalPrinterLineBuilder.addFormat(ThermalPrinterLineFormat.DOUBLE_WIDTH);

        thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());

        thermalPrinterLineBuilder.getFormats().clear();


        thermalPrinterLineBuilder.addColumn(1, 1);
        thermalPrinterLineBuilder.setAlignment(0, Alignment.LEFT);
        thermalPrinterLineBuilder.setAlignment(1, Alignment.RIGHT);

        if(financialReportDay.getCashStatus() != null){
            thermalPrinterLineBuilder.setColumnContent(0, "Bargeldstand:");
            thermalPrinterLineBuilder.setColumnContent(1, financialReportDay.getCashStatus().toString());
            thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());
        }

        thermalPrinterLineBuilder.setColumnContent(0, "Summe Waren:");
        thermalPrinterLineBuilder.setColumnContent(1, financialReportDay.getSumOfSales().toString());
        thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());

        thermalPrinterLineBuilder.setColumnContent(0, "Tageslosung:");
        thermalPrinterLineBuilder.setColumnContent(1, financialReportDay.getDailySales().toString());
        thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());

        if (financialReportDay.getPaymentsMap() != null) {
            Map paymentMap = financialReportDay.getPaymentsMap();
            Set set = paymentMap.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                thermalPrinterLineBuilder.setColumnContent(0, me.getKey().toString() + ":");
                thermalPrinterLineBuilder.setColumnContent(1, me.getValue().toString());
                thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());
            }
        }

        if (financialReportDay.getPaymentsList() != null && financialReportDay.getPaymentsList().size() > 0) {
            financialReportDay.getPaymentsList().forEach(paymentInvoiceItem -> {
                thermalPrinterLineBuilder.setColumnContent(0, paymentInvoiceItem.getLabel());
                thermalPrinterLineBuilder.setColumnContent(1, paymentInvoiceItem.getTurnover().toString());
                thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());
            });
        }


        financialReportDay.getTaxList().stream().sorted((t1, t2) -> Integer.compare(t1.getTaxRate(), t2.getTaxRate())).forEach(taxHelper -> {
            thermalPrinterLineBuilder.setColumnContent(0, "Brutto " + taxHelper.getTaxRate() + "%: ");
            thermalPrinterLineBuilder.setColumnContent(1, (taxHelper.getBrutto().toString()));
            thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());
            thermalPrinterLineBuilder.setColumnContent(0, "Netto " + taxHelper.getTaxRate() + "%: ");
            thermalPrinterLineBuilder.setColumnContent(1, (taxHelper.getNetto().toString()));
            thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());
            thermalPrinterLineBuilder.setColumnContent(0, "USt " + taxHelper.getTaxRate() + "%: ");
            thermalPrinterLineBuilder.setColumnContent(1, (taxHelper.getMwst().toString()));
            thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());
        });

        thermalPrinterLineBuilder.clearColumns();
        thermalPrinterLineBuilder.setContent("\n");
        thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());
        thermalPrinterLineBuilder.clearColumns();
        thermalPrinterLineBuilder.setAlignment(0, Alignment.RIGHT);
        thermalPrinterLineBuilder.setColumnContent(0, LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
        thermalPrinterLines.add(thermalPrinterLineBuilder.getLine());

        return thermalPrinterLines;
    }

    @Override
    public void generateFinancialJasperReportAsHtml(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletResponse response, boolean sum) throws JRException, IOException {

        financialReport.setSum(sum);
        financialReport.printReportAsHtml(startDate, endDate, storeGuid, response);

    }

    @Override
    public void generateFinancialJasperReportAsPdf(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletResponse response, boolean sum) throws JRException, IOException {

        financialReport.setSum(sum);
        financialReport.printReportAsPdf(startDate, endDate, storeGuid, response);

    }
}
