package unipos.signature.components.dep;

import org.spockframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.integritySafeGuard.domain.SignatureResult;
import unipos.signature.components.signatureOption.SignatureOption;
import unipos.signature.components.signatureOption.SignatureOptionRepository;
import unipos.signature.components.signatureResult.SignatureResultRepository;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * Created by domin on 23.01.2017.
 */

@Service
class DepServiceImpl implements DepService {

    @Autowired
    SignatureResultRepository signatureResultRepository;
    @Autowired
    SignatureOptionRepository signatureOptionRepository;

    @Override
    public DepExportRoot getExportEntriesForStoreGuid(String storeGuid) throws UnsupportedEncodingException {

        SignatureOption signatureOption = signatureOptionRepository.findFirstByStoreGuid(storeGuid);
        Assert.notNull(signatureOptionRepository, "No valid signatureOption found for the given storeGuid:" + storeGuid);

        List<SignatureResult> signatureResults = signatureResultRepository.findByStoreGuid(storeGuid);

        List<DepEntry> depEntries = new ArrayList<>();

        DepEntry currentDepEntry = new DepEntry();

        for (SignatureResult signatureResult : signatureResults) {
            boolean isAusgefallen = signatureResult.getSignatureBase64Url().equals(Base64.getUrlEncoder().withoutPadding().encodeToString("Sicherheitseinrichtung ausgefallen".getBytes("UTF-8")));

            if (currentDepEntry.getJwsKompakt().size() == 0) {
                currentDepEntry.setSignatureinrichtungAusgefallen(isAusgefallen);
                currentDepEntry.getJwsKompakt().add(signatureResult.toJwsCompactRepresentation());
                currentDepEntry.setSignaturZertifikat(isAusgefallen ? "" : signatureOption.getSignatureCertificateDer());
                continue;
            }

            if (currentDepEntry.isSignatureinrichtungAusgefallen() == isAusgefallen) {
                currentDepEntry.getJwsKompakt().add(signatureResult.toJwsCompactRepresentation());
            } else {
                depEntries.add(currentDepEntry);
                currentDepEntry = new DepEntry();
                currentDepEntry.getJwsKompakt().add(signatureResult.toJwsCompactRepresentation());
                currentDepEntry.setSignatureinrichtungAusgefallen(isAusgefallen);
                currentDepEntry.setSignaturZertifikat(isAusgefallen ? "" : signatureOption.getSignatureCertificateDer());
            }
        }

        if (currentDepEntry.getJwsKompakt().size() > 0) {
            depEntries.add(currentDepEntry);
        }

        return DepExportRoot.builder().belegeGruppe(depEntries).build();
    }
}
