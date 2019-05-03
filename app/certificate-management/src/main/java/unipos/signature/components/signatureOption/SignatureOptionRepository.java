package unipos.signature.components.signatureOption;

import org.springframework.data.mongodb.repository.MongoRepository;

import javax.smartcardio.Card;

/**
 * Created by domin on 04.11.2016.
 */
public interface SignatureOptionRepository extends MongoRepository<SignatureOption, String> {
    SignatureOption findFirstByStoreGuid(String storeGuid);

    Long deleteByStoreGuid(String storeGuid);

    Long countByStoreGuid(String storeGuid);

    SignatureOption findFirstByCrtSerialNo(String crtSerialNo);
}
