package unipos.signature.components.dep;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 23.01.2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DepExportRoot {

    private List<DepEntry> belegeGruppe = new ArrayList<>();


    @JsonProperty("Belege-Gruppe")
    public List<DepEntry> getBelegeGruppe() {
        return belegeGruppe;
    }

    public void setBelegeGruppe(List<DepEntry> belegeGruppe) {
        this.belegeGruppe = belegeGruppe;
    }
}
