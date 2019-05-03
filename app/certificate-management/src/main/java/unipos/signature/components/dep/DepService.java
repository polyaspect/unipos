package unipos.signature.components.dep;

import java.io.UnsupportedEncodingException;

/**
 * Created by domin on 23.01.2017.
 */
public interface DepService {

    DepExportRoot getExportEntriesForStoreGuid(String storeGuid) throws UnsupportedEncodingException;
}