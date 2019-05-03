package unipos.signature.components.umsatzZaehler;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by domin on 15.12.2016.
 */
public interface UmsatzZaehlerRepository extends MongoRepository<UmsatzZaehler, String>, UmsatzZaehlerRepositoryCustom {

    public UmsatzZaehler findFirstByStoreGuidOrderByCreationDateTimeDesc(String companyId);

    Long deleteByGuid(String guid);
}
