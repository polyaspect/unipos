package unipos.data.components.productLog;

import org.springframework.data.mongodb.repository.MongoRepository;
import unipos.data.components.product.Product;

import java.util.List;

/**
 * Created by dominik on 19.08.15.
 */
public interface ProductLogRepository extends MongoRepository<ProductLog, String>, ProductLogRepositoryCustom {
    ProductLog findFirstByIgnoredOrderByProduct_NumberDesc(boolean ignored);

    ProductLog findFirstByProductIdentifierAndIgnoredOrderByDateDesc(Long number, boolean ignored);

    List<ProductLog> findByPublishedAndIgnored(boolean published, boolean ignored);

    List<ProductLog> findByProduct_SortOrder(int sortOrder);

    List<ProductLog> findByProduct_SortOrderGreaterThan(int sortOrder);
}
