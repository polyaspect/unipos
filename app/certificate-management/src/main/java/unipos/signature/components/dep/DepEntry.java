package unipos.signature.components.dep;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by domin on 23.01.2017.
 */
public class DepEntry {


    private List<String> jwsKompakt = new ArrayList<>();
    private String signaturZertifikat;
    private List<String> zertifizierungsstellen = new ArrayList<>();
    private boolean isSignatureinrichtungAusgefallen = false;

    @JsonProperty("Belege-kompakt")
    public List<String> getJwsKompakt() {
        return jwsKompakt;
    }

    @JsonProperty("Signaturzertifikat")
    public String getSignaturZertifikat() {
        return signaturZertifikat != null ? signaturZertifikat : "";
    }

    @JsonProperty("Zertifizierungsstellen")
    public List<String> getZertifizierungsstellen() {
        return zertifizierungsstellen;
    }

    public void setJwsKompakt(List<String> jwsKompakt) {
        this.jwsKompakt = jwsKompakt;
    }

    public void setSignaturZertifikat(String signaturZertifikat) {
        this.signaturZertifikat = signaturZertifikat;
    }

    public void setZertifizierungsstellen(List<String> zertifizierungsstellen) {
        this.zertifizierungsstellen = zertifizierungsstellen;
    }

    @JsonIgnore
    public boolean isSignatureinrichtungAusgefallen() {
        return isSignatureinrichtungAusgefallen;
    }

    public void setSignatureinrichtungAusgefallen(boolean signatureinrichtungAusgefallen) {
        isSignatureinrichtungAusgefallen = signatureinrichtungAusgefallen;
    }
}
