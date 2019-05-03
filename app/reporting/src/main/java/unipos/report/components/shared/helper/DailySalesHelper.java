package unipos.report.components.shared.helper;

import lombok.Builder;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Created by Thomas on 05.01.2016.
 */
@Data
@Builder
public class DailySalesHelper {

    private LocalDate date;
    private BigDecimal mwst0;
    private BigDecimal net0;
    private BigDecimal mwst10;
    private BigDecimal net10;
    private BigDecimal mwst13;
    private BigDecimal net13;
    private BigDecimal mwst20;
    private BigDecimal net20;
    private BigDecimal sum;

    public DailySalesHelper() {
        this.date = LocalDate.now();
        this.mwst0 = new BigDecimal("0.00");
        this.net0 = new BigDecimal("0.00");
        this.mwst10 = new BigDecimal("0.00");
        this.net10 = new BigDecimal("0.00");
        this.mwst13 = new BigDecimal("0.00");
        this.net13 = new BigDecimal("0.00");
        this.mwst20 = new BigDecimal("0.00");
        this.net20 = new BigDecimal("0.00");
        this.sum = new BigDecimal("0.00");
    };

    public DailySalesHelper(LocalDate date, BigDecimal net0, BigDecimal mwst0, BigDecimal mwst10, BigDecimal net10, BigDecimal mwst13, BigDecimal net13, BigDecimal mwst20, BigDecimal net20, BigDecimal sum) {
        this.date = date;
        this.mwst0 = mwst0;
        this.net0 = net0;
        this.mwst10 = mwst10;
        this.net10 = net10;
        this.mwst13 = mwst13;
        this.net13 = net13;
        this.mwst20 = mwst20;
        this.net20 = net20;
        this.sum = sum;
    }
}
