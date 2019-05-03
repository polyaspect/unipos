package unipos.data.components.productLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import unipos.common.remote.sync.SyncRemoteInterface;
import unipos.common.remote.sync.model.Action;
import unipos.common.remote.sync.model.Target;
import unipos.data.components.company.StoreRepository;
import unipos.data.components.company.model.Store;
import unipos.data.components.exception.DataNotFoundException;
import unipos.data.components.product.Product;
import unipos.data.components.product.ProductRepository;

import java.util.*;

/**
 * Created by dominik on 19.08.15.
 */

@Service
public class ProductLogServiceImpl implements ProductLogService {

    @Autowired
    MongoTemplate mongoTemplate;
    @Autowired
    ProductLogRepository productLogRepository;
    @Autowired
    ProductRepository productRepository;
    @Autowired
    StoreRepository storeRepository;
    @Autowired
    SyncRemoteInterface syncRemoteInterface;

    @Override
    public List<Product> adminListProducts() {
        return productLogRepository.adminListProducts();
    }

    @Override
    public ProductLog getProductWithHighestProductNumber() {
        ProductLog lastProduct = productLogRepository.findFirstByIgnoredOrderByProduct_NumberDesc(false);
        return lastProduct;
    }

    @Override
    public boolean existsProductNumber(Long number) {
        if (productLogRepository.findFirstByProductIdentifierAndIgnoredOrderByDateDesc(number, false) == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void publishChanges() {
        List<ProductLog> unpublishedLogs;
        List<ProductLog> createdProducts = new ArrayList<>();
        List<ProductLog> updatedProducts = new ArrayList<>();
        List<ProductLog> deletedProducts = new ArrayList<>();

        List<ProductLog> logs = productLogRepository.findByPublishedAndIgnored(false, false);
        Collections.sort(logs, (x, y) -> (x.getProductIdentifier().compareTo(y.getProductIdentifier())));

        Map<Long, ProductLog> map = new HashMap<>();
        for (ProductLog productLog : logs) {
            if (!map.containsKey(productLog.getProductIdentifier())) {
                map.put(productLog.getProductIdentifier(), productLog);
            }
            if (map.get(productLog.getProductIdentifier()).getDate().compareTo(productLog.getDate()) < 0) {
                map.put(productLog.getProductIdentifier(), productLog);
            }
        }
        unpublishedLogs = new ArrayList<>(map.values());

        //First of all I need to fetch all unpublished changes and seperate them by their executed Action
        for (ProductLog productLog : unpublishedLogs) {
            switch (productLog.getAction()) {
                case DELETE:
                    deletedProducts.add(productLog);
                    break;
                case UPDATE:
                    updatedProducts.add(productLog);
                    break;
                case CREATE:
                    createdProducts.add(productLog);
                    break;
            }
        }

        //After that I need to update,create or delete the products in the products table.
        createdProducts.forEach((x) -> productRepository.save(x.getProduct()));

        for (ProductLog productLog : updatedProducts) {
            productRepository.deleteByProductIdentifier(productLog.getProductIdentifier());
            productRepository.save(productLog.getProduct());
        }

        deletedProducts.forEach((x) -> productRepository.deleteByProductIdentifier(x.getProductIdentifier()));

        for (ProductLog productLog : logs) {
            productLog.setPublished(true);
            productLogRepository.save(productLog);
            try {
                syncRemoteInterface.syncChanges(productLog, Target.PRODUCT, Action.valueOf(productLog.getAction().name().toUpperCase()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public void applyChanges() {
        List<ProductLog> unpublishedLogs;
        List<ProductLog> createdProducts = new ArrayList<>();
        List<ProductLog> updatedProducts = new ArrayList<>();
        List<ProductLog> deletedProducts = new ArrayList<>();

        List<ProductLog> logs = productLogRepository.findByPublishedAndIgnored(false, false);
        Collections.sort(logs, (x, y) -> (x.getProductIdentifier().compareTo(y.getProductIdentifier())));

        Map<Long, ProductLog> map = new HashMap<>();
        for (ProductLog productLog : logs) {
            if (!map.containsKey(productLog.getProductIdentifier())) {
                map.put(productLog.getProductIdentifier(), productLog);
            }
            if (map.get(productLog.getProductIdentifier()).getDate().compareTo(productLog.getDate()) < 0) {
                map.put(productLog.getProductIdentifier(), productLog);
            }
        }
        unpublishedLogs = new ArrayList<>(map.values());

        //First of all I need to fetch all unpublished changes and seperate them by their executed Action
        for (ProductLog productLog : unpublishedLogs) {
            switch (productLog.getAction()) {
                case DELETE:
                    deletedProducts.add(productLog);
                    break;
                case UPDATE:
                    updatedProducts.add(productLog);
                    break;
                case CREATE:
                    createdProducts.add(productLog);
                    break;
            }
        }

        //After that I need to update,create or delete the products in the products table.
        createdProducts.forEach((x) -> productRepository.save(x.getProduct()));

        for (ProductLog productLog : updatedProducts) {
            productRepository.deleteByProductIdentifier(productLog.getProductIdentifier());
            productRepository.save(productLog.getProduct());
        }

        deletedProducts.forEach((x) -> productRepository.deleteByProductIdentifier(x.getProductIdentifier()));

        for (ProductLog productLog : logs) {
            productLog.setPublished(true);
            productLogRepository.save(productLog);
        }
    }

    @Override
    public void resetChanges() {
        productLogRepository.setAllUnpublishedLogsToIgnored();
    }

    @Override
    public List<Product> listProductsByCompanyGuid(String companyGuid) throws DataNotFoundException {
        List<Store> stores = storeRepository.findByCompanyGuid(companyGuid);

        if (stores == null || stores.size() == 0) {
            throw new DataNotFoundException("No stores found for the given CompanyGuid. Is it correct???");
        }
        return productLogRepository.adminListProductsByStores(stores);
    }

    @Override
    public int getMaxSortOrder() {
        List<Product> data = adminListProducts();
        if (data.stream().max((x, y) -> Integer.compare(x.getSortOrder(), y.getSortOrder())).isPresent()) {
            return data.stream().max((x, y) -> Integer.compare(x.getSortOrder(), y.getSortOrder())).get().getSortOrder();
        }
        return 0;
    }
}
