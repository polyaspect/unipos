package unipos.signature.components.signatureOption;

import unipos.common.remote.data.model.company.Store;

import java.util.List;

/**
 * Created by domin on 04.11.2016.
 */
public interface SignatureOptionService {

    void saveSignatureOption(SignatureOption signatureOption);

    List<SignatureOption> findAll();

    SignatureOption findByStoreGuid(String storeGuid);

    SignatureOption findById(String signatureOptionId);

    void deleteByStoreGuid(String storeGuid);
}
