package unipos.data.components.product;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by Dominik on 23.07.2015.
 */
public interface ProductRepository extends MongoRepository<Product, String> {
    Long deleteByNumber(Long productNumber);

    Product findByProductIdentifier(Long productNumber);

    Product findByNumber(Long number);

    List<Product> findByName(String searchString);

    List<Product> findByNameLikeIgnoreCase(String searchString);

    List<Product> findByNumberOrNameLikeIgnoreCase(int number, String name);

    Product findFirstByOrderByNumberDesc();

    Long deleteByProductIdentifier(Long productIdentifier);

    Product findFirstByGuid(String guid);

    List<Product> findByStoresContains(String storeGuid);

    List<Product> findByStoresIn(List<String> storeGuid);

    List<Product> findByAttributesIn(List<String> attributes);
}
