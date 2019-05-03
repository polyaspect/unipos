package unipos.data.components.category;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import unipos.data.components.product.Product;

import java.util.List;

/**
 * Created by dominik on 29.07.15.
 */
public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByNameLikeIgnoreCase(String name);

    Category findFirstByGuid(String guid);

    Long deleteByGuid(String guid);
}
