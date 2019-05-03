package unipos.data.components.company.model;

import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by dominik on 04.09.15.
 */
public interface CompanyRepository extends MongoRepository<Company, String> {
    Company findFirstByGuid(String guid);

    Company findFirstByStores_guid(String guid);

    Long deleteByGuid(String guid);

    Company findFirstByName(String name);
}
