package unipos.pos.signature;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.common.remote.signature.model.SignatureResult;

/**
 * Created by domin on 14.01.2017.
 */
public interface SignatureResultRepository extends MongoRepository<SignatureResult, String> {

    Long countByInvoiceSignatureTypeAndStoreGuid(SignatureResult.Type signatureInvoiceType, String storeGuid);
}
