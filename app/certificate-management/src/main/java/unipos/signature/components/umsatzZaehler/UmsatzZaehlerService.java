package unipos.signature.components.umsatzZaehler;

/**
 * Created by domin on 15.12.2016.
 */
public interface UmsatzZaehlerService {

    UmsatzZaehler getLatestUmsatzZaehlerForStoreGuid(String storeGuid);

    void saveUmsatzZaehler(UmsatzZaehler umsatzZaehler);

    void deleteByGuid(String guid);
}
