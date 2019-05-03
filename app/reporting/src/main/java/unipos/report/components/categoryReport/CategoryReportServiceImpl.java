package unipos.report.components.categoryReport;

import unipos.common.remote.data.DataRemoteInterface;
import unipos.common.remote.data.model.product.Category;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.data.model.product.Product;
import unipos.common.remote.pos.model.Invoice;
import unipos.common.remote.pos.model.ProductInvoiceItem;
import unipos.report.components.categoryReportDay.CategoryReportDay;
import unipos.report.components.shared.helper.CategoryReportHelper;
import unipos.report.components.shared.helper.TaxHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Thomas on 19.12.2015.
 */
@Service
public class CategoryReportServiceImpl implements CategoryReportService {

    @Autowired
    DataRemoteInterface dataRemoteInterface;
    @Autowired
    CategoryReport categoryReport;

    @Override
    public void generateCategoryReportAsPdf(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletResponse response) throws JRException, IOException {
        categoryReport.printReportAsPdf(startDate, endDate, storeGuid, response);
    }

    @Override
    public void generateCategoryReportAsHtml(LocalDate startDate, LocalDate endDate, String storeGuid, HttpServletResponse response) throws JRException, IOException {
        categoryReport.printReportAsHtml(startDate, endDate, storeGuid, response);
    }

    @Override
    public CategoryReportDay getCategoryReportEntries(List<Invoice> invoices) {
        List<CategoryReportHelper> categories = new ArrayList<>();
        List<TaxHelper> turnoverTaxes = new ArrayList<>();
        invoices.stream().filter(invoice -> invoice.getInvoiceItems() != null && invoice.getInvoiceItems().size() > 0).forEach(invoice -> invoice.getInvoiceItems().stream().filter(invoiceItem -> invoiceItem instanceof ProductInvoiceItem && !((ProductInvoiceItem) invoiceItem).isReversalApplied()).forEach(invoiceItem -> {
            ProductInvoiceItem productInvoiceItem = (ProductInvoiceItem) invoiceItem;
            Product product = dataRemoteInterface.getProductByProductIdentifier(productInvoiceItem.getProductNumber());

            boolean result = categories.stream().filter(categoryReportHelper -> categoryReportHelper.getCategory().equals(product != null ? product.getCategory(): Category.builder().name("Keine Kategorie").build())).findFirst().map(categoryReportHelper -> {
                categoryReportHelper.setTurnover(productInvoiceItem.getTurnoverGross().add(categoryReportHelper.getTurnover()));
                categoryReportHelper.setTurnoverNet(productInvoiceItem.getTurnoverNet().add(categoryReportHelper.getTurnoverNet()));
                categoryReportHelper.setTurnoverTax(productInvoiceItem.getTax().add(categoryReportHelper.getTurnoverTax()));
                categoryReportHelper.setQuantity(categoryReportHelper.getQuantity().add(new BigDecimal(productInvoiceItem.getQuantity())));

                return true;
            }).isPresent();
            if (!result) {
                categories.add(CategoryReportHelper.builder().turnover(productInvoiceItem.getTurnoverGross()).category(product != null ? product.getCategory() : Category.builder().name("Keine Kategorie").build()).quantity(new BigDecimal(productInvoiceItem.getQuantity())).turnoverNet(productInvoiceItem.getTurnoverNet()).turnoverTax(productInvoiceItem.getTax()).build());
            }

            boolean taxResult = turnoverTaxes.stream().filter(taxHelper -> taxHelper.getTaxRate() == productInvoiceItem.getTaxRate()).findFirst().map(taxHelper -> {
                taxHelper.setBrutto(taxHelper.getBrutto().add(productInvoiceItem.getTurnoverGross()));
                taxHelper.setNetto(taxHelper.getNetto().add(productInvoiceItem.getTurnoverNet()));
                taxHelper.setMwst(taxHelper.getMwst().add(productInvoiceItem.getTax()));

                return true;
            }).isPresent();
            if (!taxResult) {
                turnoverTaxes.add(TaxHelper.builder().taxRate(productInvoiceItem.getTaxRate()).brutto(productInvoiceItem.getTurnoverGross()).netto(productInvoiceItem.getTurnoverNet()).mwst(productInvoiceItem.getTax()).build());
            }
        }));

        BigDecimal sum = categories.stream().map(CategoryReportHelper::getTurnover).reduce(new BigDecimal("0.0"), BigDecimal::add);
        categories.forEach(categoryReportHelper -> {
            categoryReportHelper.setContribution(categoryReportHelper.getTurnover().divide(sum, 6, BigDecimal.ROUND_HALF_UP));
        });

        return CategoryReportDay.builder().categories(categories).turnoverTaxes(turnoverTaxes).build();
    }
}
