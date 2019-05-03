package unipos.signature.components.signatureOption;

import org.spockframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.data.model.company.Store;
import unipos.integritySafeGuard.SmartCardUtils;
import unipos.integritySafeGuard.domain.RksSuite;

import java.util.List;

/**
 * Created by domin on 04.11.2016.
 */
@Service
public class SignatureOptionServiceImpl implements SignatureOptionService {

    @Autowired
    SignatureOptionRepository signatureOptionRepository;

    @Override
    public void saveSignatureOption(SignatureOption signatureOption) {
        //Zu allererst einmal überprüfen ob es sich um einen neuen Eintrag für diese Firma handelt
        Assert.notNull(signatureOption, "The signatureJob must not be null!");

        if (signatureOption.getTurnOverCounterLengthInBytes() == 0) {
            signatureOption.setTurnOverCounterLengthInBytes(5);
        }

        signatureOption.secretKey = SmartCardUtils.getSecretKeyByCustomPw(signatureOption.secretKeyPlainText);

        //we can always perform this delete, because neither we don't have any options, or we already want to overwrite it
        deleteByStoreGuid(signatureOption.storeGuid);
        signatureOptionRepository.save(signatureOption);
    }

    @Override
    public List<SignatureOption> findAll() {
        return signatureOptionRepository.findAll();
    }

    @Override
    public SignatureOption findByStoreGuid(String storeGuid) {
        return signatureOptionRepository.findFirstByStoreGuid(storeGuid);
    }

    @Override
    public SignatureOption findById(String signatureOptionId) {
        return signatureOptionRepository.findOne(signatureOptionId);
    }

    @Override
    public void deleteByStoreGuid(String storeGuid) {
        signatureOptionRepository.deleteByStoreGuid(storeGuid);
    }
}
