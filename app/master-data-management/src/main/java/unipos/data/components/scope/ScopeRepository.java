package unipos.data.components.scope;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.data.components.company.model.Store;

import java.util.List;

/**
 * Created by Dominik on 27.12.2015.
 */
public interface ScopeRepository extends MongoRepository<Scope, String>{
    List<Scope> findByStore(Store store);
    Long deleteByStore(Store store);
}
