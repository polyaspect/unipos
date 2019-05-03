package unipos.data.components.productLog;

import unipos.data.components.company.model.Store;
import unipos.data.components.product.Product;

import java.util.List;

/**
 * Created by dominik on 25.08.15.
 */
public interface ProductLogRepositoryCustom {

    List<Product> adminListProducts();

    Long setAllUnpublishedLogsToIgnored();

    List<Product> adminListProductsByStores(List<Store> stores);
}
