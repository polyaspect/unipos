package unipos.data.components.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;
import unipos.data.components.taxRates.TaxRate;
import unipos.data.components.taxRates.TaxRateRepository;
import unipos.common.remote.sync.SyncRemoteInterface;

import java.util.List;
import java.util.UUID;

/**
 * Created by dominik on 29.07.15.
 */

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    TaxRateRepository taxRateRepository;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;

    @Override
    public List<Category> findAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category findCategoryByMongoDbId(String mongoId) throws IllegalArgumentException {
        if(categoryRepository.exists(mongoId)) {
            return categoryRepository.findOne(mongoId);
        } else {
            throw new IllegalArgumentException("Category with the given Mongo-Db ID not existent in the DB");
        }
    }

    @Override
    public List<Category> findCategoryByName(String name) throws IllegalArgumentException{
        List<Category> categories = categoryRepository.findByNameLikeIgnoreCase(name);

        if(categories.size() > 0) {
            return categories;
        } else {
            throw new IllegalArgumentException("No Category with the Given name or Name-Part found");
        }
    }

    @Override
    public void deleteCategoryByMongoId(String categoryId) throws IllegalArgumentException{
        if(categoryRepository.exists(categoryId)) {
            categoryRepository.delete(categoryId);
        } else {
            throw new IllegalArgumentException("Entity with the given Mongo-DB ID not existing in the DB");
        }
    }

    @Override
    public Category saveCategory(Category category) throws IllegalArgumentException{
        if(category.getGuid() == null || category.getGuid().isEmpty()) {
            category.setGuid(UUID.randomUUID().toString());
        }
        if(category.getTaxRate() != null) {
            category.setTaxRate(taxRateRepository.findOne(category.getTaxRate().getId()));
            category.setGuid(UUID.randomUUID().toString());
            categoryRepository.save(category);
            try {
                syncRemoteInterface.syncChanges(category, Target.CATEGORY, Action.CREATE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(category.getId() == null) {
            throw new IllegalArgumentException("Category Entity not saved");
        } else {
            return category;
        }
    }

    public void deleteAllCategories() {
        categoryRepository.deleteAll();
    }

    @Override
    public void deleteByGUID(String guid) {
        Category toDeleteCategory = categoryRepository.findFirstByGuid(guid);
        categoryRepository.deleteByGuid(guid);
        try {
            syncRemoteInterface.syncChanges(toDeleteCategory, Target.CATEGORY, Action.DELETE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateCategory(Category category) {
        TaxRate taxRate = taxRateRepository.findOne(category.getTaxRate().getId());
        category.setTaxRate(taxRate);
        categoryRepository.save(category);

        try {
            syncRemoteInterface.syncChanges(category, Target.CATEGORY, Action.UPDATE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
