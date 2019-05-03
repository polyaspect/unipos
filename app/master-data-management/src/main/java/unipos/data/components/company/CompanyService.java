package unipos.data.components.company;

import unipos.data.components.company.model.Company;

import java.util.List;

/**
 * Created by dominik on 04.09.15.
 */
public interface CompanyService {

    /**
     * This method saves a company Entity in the database. This method should also check, that there is an attempt
     * to store more than one entity inside the database. If it's so, it should throw an Exception
     *
     * @param company the company to store inside the database
     * @throws CompanySaveMoreThanOneException if you try to save more than one Company inside the database
     */
    void saveCompany(Company company) throws CompanySaveMoreThanOneException;

    /**
     * This method deletes a given company entity
     *
     * @param mongoDbId the company you want to remove DocumentId
     */
    void deleteCompanyByMongoDbId(String mongoDbId);

    /**
     * This method gets the stored entity and tries to update the entity. If the DocumentId of the stored Entity is not equal the
     * Document-ID of the storing entity, an {CompanySaveMoreThanOneException} is thrown.
     *
     * @param company the company entity you try to update
     * @throws CompanySaveMoreThanOneException if you try to update an entity, that is not existing inside the database
     */
    void updateCompany(Company company) throws CompanySaveMoreThanOneException;

    /**
     * We only store one Company Instance inside the Data Module. So this method should return the only one saved company instance
     * from the Database.
     *
     * @return the Company Instance from the database
     */
    Company findFirstCompany();

    /**
     * Delete all persisted Company-Entities inside the Database. In fact, this method should always delete one entity
     */
    void deleteAllCompanies();

    List<Company> findAll();

    void deleteByGuid(String guid);

    Company findByGuid(String guid);

    Company findByName(String companyName);
}
