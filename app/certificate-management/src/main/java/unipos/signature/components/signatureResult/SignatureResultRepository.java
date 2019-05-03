package unipos.signature.components.signatureResult;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.integritySafeGuard.domain.SignatureResult;
import unipos.integritySafeGuard.domain.Type;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by domin on 13.10.2016.
 */
public interface SignatureResultRepository extends MongoRepository<SignatureResult, String> {

    Long countByInvoiceSignatureTypeAndStoreGuid(Type signatureInvoiceType, String storeGuid);

    /**
     * @return the total number of stored signatureResults
     */
    Long countBy();

    SignatureResult findFirstByStoreGuidOrderByCreationDateDesc(String storeGuid);

    List<SignatureResult> findByStoreGuid(String storeGuid);
}
