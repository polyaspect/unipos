package unipos.data.components.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;
import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.CompanyRepository;
import unipos.data.components.company.model.Store;

import java.util.List;
import java.util.UUID;

/**
 * Created by dominik on 04.09.15.
 */

@Service
public class CompanyServiceImpl implements CompanyService {

    @Autowired
    CompanyRepository companyRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;

    @Override
    public void saveCompany(Company company) throws CompanySaveMoreThanOneException {
        if(company.getGuid() == null || company.getGuid().isEmpty()) {
            company.setGuid(UUID.randomUUID().toString());
        }
        companyRepository.save(company);
        try {
            syncRemoteInterface.syncChanges(company, Target.COMPANY, Action.CREATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteCompanyByMongoDbId(String mongoDbId) {
        companyRepository.delete(mongoDbId);
    }

    @Override
    public void updateCompany(Company company) throws CompanySaveMoreThanOneException {
        if(companyRepository.exists(company.getId())) {
            companyRepository.save(company);
            try {
                syncRemoteInterface.syncChanges(company, Target.COMPANY, Action.UPDATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new CompanySaveMoreThanOneException("The given entity is not existent inside the Database. You are trying to update a not existing entity");
        }
    }

    @Override
    public Company findFirstCompany() {
        List<Company> companies = companyRepository.findAll();
        if(companies.size() == 1) {
            return companies.get(0);
        }
        return null;
    }

    @Override
    public void deleteAllCompanies() {
        companyRepository.deleteAll();
    }

    @Override
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    @Override
    public void deleteByGuid(String guid) {
        companyRepository.deleteByGuid(guid);
    }

    @Override
    public Company findByGuid(String guid) {
        return companyRepository.findFirstByGuid(guid);
    }

    @Override
    public Company findByName(String companyName) {
        return companyRepository.findFirstByName(companyName);
    }
}
