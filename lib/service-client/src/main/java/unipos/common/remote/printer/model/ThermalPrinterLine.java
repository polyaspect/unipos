package unipos.common.remote.printer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jolly on 05.03.2016.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ThermalPrinterLine {
    private String line;
    private List<ThermalPrinterLineFormat> formats = new ArrayList<>();
}
