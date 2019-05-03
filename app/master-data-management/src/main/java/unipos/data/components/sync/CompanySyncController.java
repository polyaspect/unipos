package unipos.data.components.sync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import unipos.data.components.category.Category;
import unipos.data.components.category.CategoryRepository;
import unipos.data.components.company.StoreRepository;
import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.CompanyRepository;
import unipos.data.components.company.model.Store;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateRepository;

import java.util.ArrayList;

/**
 * Created by dominik on 01.11.15.
 */
@RestController
@RequestMapping("/syncCompany")
public class CompanySyncController extends SyncController<Company> {

    @Autowired
    MongoOperations mongoOperations;
    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    StoreRepository storeRepository;

    @Override
    protected void deleteEntity(Company entity) {
        Company company = companyRepository.findFirstByGuid(entity.getGuid());
        companyRepository.delete(company.getId());
    }

    @Override
    public void saveEntity(Company entity) {
        mongoOperations.insert(entity);
    }

    @Override
    protected void updateEntity(Company log) {
        Company updatingEntity = companyRepository.findFirstByGuid(log.getGuid());
        updatingEntity.setName(log.getName());
        updatingEntity.setCommercialRegisterNumber(log.getCommercialRegisterNumber());
        updatingEntity.setStores(new ArrayList<>());
        log.getStores().forEach(x -> updatingEntity.getStores().add(storeRepository.findFirstByGuid(x.getGuid())));
        updatingEntity.setUid(log.getUid());
        companyRepository.save(updatingEntity);
    }

    @Override
    protected void updateSequenceNumber(Company entity) {
        //The Category does not have a sequence Number. So we don't have to do anything
    }
}
