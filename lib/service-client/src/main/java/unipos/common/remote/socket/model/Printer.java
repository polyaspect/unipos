package unipos.common.remote.socket.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by domin on 12.03.2016.
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Printer {

    private String printerGuid;
    private boolean defaultPrinter;
}
