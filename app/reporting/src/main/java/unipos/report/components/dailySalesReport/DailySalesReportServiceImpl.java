package unipos.report.components.dailySalesReport;

import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Service;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ProductInvoiceItem;
import unipos.report.components.shared.helper.DailySalesHelper;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Thomas on 30.12.2015.
 */
@Service
public class DailySalesReportServiceImpl implements DailySalesReportService {

    @Autowired
    DailySalesReport dailySalesReport;

    @Override
    public void generateDailySalesReportAsPdf(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {

        dailySalesReport.printReportAsPdf(startDate, endDate, storeGuid, response);
    }

    @Override
    public void generateDailySalesReportAsHtml(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletRequest request, HttpServletResponse response) throws JRException, IOException {
        dailySalesReport.printReportAsHtml(startDate, endDate, storeGuid, response);
    }

    public DailySalesHelper getDailySales(List<Invoice> invoiceList) {
        DailySalesHelper dailySalesHelper = new DailySalesHelper();

        invoiceList.stream().filter(invoice -> invoice.getInvoiceItems() != null && invoice.getInvoiceItems().size() > 0).forEach(invoice -> invoice.getInvoiceItems().stream().filter(invoiceItem -> invoiceItem instanceof ProductInvoiceItem && !((ProductInvoiceItem) invoiceItem).isReversalApplied()).forEach(invoiceItem -> {
            dailySalesHelper.setSum(((ProductInvoiceItem) invoiceItem).getTurnoverGross().add(dailySalesHelper.getSum()));

            if (((ProductInvoiceItem) invoiceItem).getTaxRate() == 20) {
                dailySalesHelper.setNet20(((ProductInvoiceItem) invoiceItem).getTurnoverNet().add((dailySalesHelper.getNet20() != null) ? dailySalesHelper.getNet20() : new BigDecimal(0.0)));

                dailySalesHelper.setMwst20(((ProductInvoiceItem) invoiceItem).getTax().add((dailySalesHelper.getMwst20() != null) ? dailySalesHelper.getMwst20() : new BigDecimal(0.0)));
            }
            if (((ProductInvoiceItem) invoiceItem).getTaxRate() == 13) {
                dailySalesHelper.setNet13(((ProductInvoiceItem) invoiceItem).getTurnoverNet().add((dailySalesHelper.getNet13() != null) ? dailySalesHelper.getNet13() : new BigDecimal(0.0)));

                dailySalesHelper.setMwst13(((ProductInvoiceItem) invoiceItem).getTax().add((dailySalesHelper.getMwst13() != null) ? dailySalesHelper.getMwst13() : new BigDecimal(0.0)));
            }
            if (((ProductInvoiceItem) invoiceItem).getTaxRate() == 10) {
                dailySalesHelper.setNet10(((ProductInvoiceItem) invoiceItem).getTurnoverNet().add((dailySalesHelper.getNet10() != null) ? dailySalesHelper.getNet10() : new BigDecimal(0.0)));

                dailySalesHelper.setMwst10(((ProductInvoiceItem) invoiceItem).getTax().add((dailySalesHelper.getMwst10() != null) ? dailySalesHelper.getMwst10() : new BigDecimal(0.0)));
            }
            if (((ProductInvoiceItem) invoiceItem).getTaxRate() == 0) {
                dailySalesHelper.setNet0(((ProductInvoiceItem) invoiceItem).getTurnoverNet().add((dailySalesHelper.getNet0() != null) ? dailySalesHelper.getNet0() : new BigDecimal(0.0)));
            }
        }));
        return dailySalesHelper;
    }
}
