package unipos.data.components.category;

import java.util.List;

/**
 * Created by dominik on 29.07.15.
 */
public interface CategoryService {
    List<Category> findAllCategories();

    Category findCategoryByMongoDbId(String mongoId);

    List<Category> findCategoryByName(String name);

    void deleteCategoryByMongoId(String categoryId);

    Category saveCategory(Category category);

    public void deleteAllCategories();

    void deleteByGUID(String guid);

    void updateCategory(Category category);
}
