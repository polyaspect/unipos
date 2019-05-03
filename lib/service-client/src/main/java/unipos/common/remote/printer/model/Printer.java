package unipos.common.remote.printer.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dominik on 09.09.15.
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Printer {

    @Id
    private String id;
    private Long printerId;
    private String name;
    private String typeName;
    private String logoPath;
    private String ipAddress;
    private List<ThermalPrinterLine> formats = new ArrayList<>();
    protected List<String> stores = new ArrayList<>();

}
