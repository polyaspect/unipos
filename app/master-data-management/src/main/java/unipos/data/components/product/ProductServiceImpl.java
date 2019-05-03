package unipos.data.components.product;

import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.data.components.category.CategoryRepository;
import unipos.data.components.company.CompanyService;
import unipos.data.components.company.StoreService;
import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.Store;
import unipos.data.components.exception.DataNotFoundException;
import unipos.data.components.productLog.LogAction;
import unipos.data.components.productLog.ProductLog;
import unipos.data.components.productLog.ProductLogRepository;
import unipos.data.components.productLog.ProductLogService;
import unipos.data.components.sequence.ProductLogSequenceRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Created by Dominik on 23.07.2015.
 */

@Service
public class ProductServiceImpl implements ProductService {

    public static final String PRODUCT_SEQ_KEY = "productLogSeqKey";
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ProductLogRepository productLogRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CompanyService companyService;

    @Autowired
    ProductLogSequenceRepository productLogSequenceRepository;
    @Autowired
    StoreService storeService;

    @Autowired
    ProductLogService productLogService;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    SocketRemoteInterface socketRemoteInterface;

    @Override
    public void setSortOrderAutomatically(ProductLog productLog) {
        int index = productLog.getProduct().getSortOrder();

        final int fIndex = index;
        List<Product> productList = productLogRepository.adminListProducts().stream().filter(x -> x.getSortOrder() >= fIndex).sorted((x, y) -> x.getSortOrder() - y.getSortOrder()).collect(Collectors.toList());

        int maxIndex = -1 ;
        if (productList.stream().max((x, y) -> Integer.compare(x.getSortOrder(), y.getSortOrder())).isPresent()) {
            maxIndex = productList.stream().max((x, y) -> Integer.compare(x.getSortOrder(), y.getSortOrder())).get().getSortOrder();
        }

        if (maxIndex == -1 )
            return;

        for (Product product : productList) {
            if (index > maxIndex) {
                break;
            }
            int finalIndex = index;
            List<Product> productWithSameSortOrder = productList.stream().filter(x -> x.getSortOrder() == finalIndex).collect(Collectors.toList());
            if (productWithSameSortOrder.size() > 1) {
                if (productLog.getProduct().getGuid().equals(product.getGuid())) {
                    continue;
                }
                product.setSortOrder(product.getSortOrder() + 1);
                if (product.getCategory() != null) {
                    product.setCategory(categoryRepository.findOne(product.getCategory().getId()));
                }
                ProductLog productLogToSave = ProductLog.newProductLogFromProduct(product);
                productLogToSave.setAction(LogAction.UPDATE);
                productLogToSave.setDate(LocalDateTime.now());
                productLogRepository.save(productLogToSave);
            } else if (productWithSameSortOrder.size() == 0) {
                break;
            }
            index++;
        }
    }


    @Override
    public void setSortOrderToNumber() {
        List<Product> productList = productLogRepository.adminListProducts().stream().collect(Collectors.toList());

        for (Product product : productList) {
            product.setSortOrder(product.getNumber().intValue());
            ProductLog productLogToSave = ProductLog.newProductLogFromProduct(product);
            productLogToSave.setAction(LogAction.UPDATE);
            productLogToSave.setDate(LocalDateTime.now());
            productLogRepository.save(productLogToSave);
        }
    }

    /**
     * This method doesn't directly persist a Product Entity inside the Products-Table.
     * Instead, it just creates a new Log entry, and marks the newly created entry as "Dirty" (that means published ==false)
     * The Entity gets just stored inside the Product Collection, if this Dirty-Flag gets removed --> The Changed are published to all.
     */
    @Override
    public void saveProduct(Product product) {
        if (product != null) {
            Long productIdentifier = productLogSequenceRepository.getNextSequenceId(PRODUCT_SEQ_KEY);
            if (product.getCategory() != null) {
                product.setCategory(categoryRepository.findOne(product.getCategory().getId()));
            }
            product.setProductIdentifier(productIdentifier);
            product.setGuid(UUID.randomUUID().toString());

            ProductLog productLog = ProductLog.newProductLogFromProduct(product);
            productLog.setAction(LogAction.CREATE);
            productLog.setDate(LocalDateTime.now());
            productLogRepository.save(productLog);
            setSortOrderAutomatically(productLog);
        } else {
            throw new IllegalArgumentException("The Attribute \"product\" is NULL!");
        }
    }

    /**
     * Tis Method is practicaly only used by the Database Initializer
     *
     * @param products
     */
    public void saveProducts(Product... products) {
        if (products != null) {
            for (Product product : products) {
                saveProduct(product);
            }
        } else {
            throw new IllegalArgumentException("The Attribute \"products\" is NULL!");
        }

    }

    /**
     * This method doesn't directly update a Product Entity inside the Products-Table.
     * Instead, it just creates a new Log entry, and marks the newly created entry as "Dirty" (that means published == false)
     * The Entity gets just stored inside the Product Collection, if this Dirty-Flag gets removed --> The Changed are published to all.
     */
    @Override
    public void updateProduct(Product product) {
        if (product != null) {
            if (product.getCategory() != null) {
                product.setCategory(categoryRepository.findOne(product.getCategory().getId()));
            }
            ProductLog productLog = ProductLog.newProductLogFromProduct(product);
            productLog.setAction(LogAction.UPDATE);
            productLog.setDate(LocalDateTime.now());
            productLogRepository.save(productLog);
            setSortOrderAutomatically(productLog);
        }
    }

    @Override
    public void updateStores(Product product) {
        if (product != null) {

            Product oldProduct = productLogRepository.findFirstByProductIdentifierAndIgnoredOrderByDateDesc(product.getProductIdentifier(), false).getProduct();

            oldProduct.setStores(product.getStores());
            ProductLog productLog = ProductLog.newProductLogFromProduct(oldProduct);
            productLog.setAction(LogAction.UPDATE);
            productLog.setDate(LocalDateTime.now());
            productLogRepository.save(productLog);
        }
    }

    @Override
    public List<Product> listProducts() {
        List<Product> products = productRepository.findAll();
        return products;
    }

    @Override
    public List<Product> listProductsByStore(Store store) {
        List<Product> products = productRepository.findByStoresContains(store.getGuid());
        return products;
    }

    @Override
    public List<Product> listProductsByCompany(Company company) {
        List<String> stores = company.getStores().stream().map(Store::getGuid).collect(Collectors.toList());
        return productRepository.findByStoresIn(stores);
    }

    @Override
    public List<Product> listProductsByCompanyGuid(String companyGuid) throws DataNotFoundException {
        Company company = companyService.findByGuid(companyGuid);

        if (company == null) {
            throw new DataNotFoundException("Unable to retrieve a company for the given GUID");
        }

        return listProductsByCompany(company);
    }

    /**
     * A real deletion is impossible. Because of this, the delete method does also just create a new
     * ProductLog Entry, that describes, that the Product really was marked for deletion.
     * Inside the Product Table, the changes also get only applied, if the changes get flushed
     *
     * @param product
     */
    @Override
    public void deleteProduct(Product product) {
        if (product != null) {
            if (product.getCategory() != null) {
                product.setCategory(categoryRepository.findOne(product.getCategory().getId()));
            }
            ProductLog productLog = ProductLog.newProductLogFromProduct(product);
            productLog.setAction(LogAction.DELETE);
            productLog.setDeleted(true);
            productLog.setDate(LocalDateTime.now());
            productLogRepository.save(productLog);
        }
    }

    /**
     * A real deletion is impossible. Because of this, the delete method does also just create a new
     * ProductLog Entry, that describes, that the Product really was marked for deletion.
     * Inside the Product Table, the changes also get only applied, if the changes get flushed
     *
     * @param id
     */
    @Override
    public void deleteProduct(String id) {
        if (productRepository.exists(id)) {
            Product product = productRepository.findOne(id);
            ProductLog productLog = ProductLog.newProductLogFromProduct(product);
            productLog.setAction(LogAction.DELETE);
            productLog.setDate(LocalDateTime.now());
            productLog.setDeleted(true);
            productLogRepository.save(productLog);
        } else {
            throw new IllegalArgumentException("The Product with the given Mongo-ID was not found");
        }
    }

    @Override
    public void deleteProductByProductNumber(Long productIdentifier) {
        if (productLogRepository.findFirstByProductIdentifierAndIgnoredOrderByDateDesc(productIdentifier, false) != null) {
            ProductLog productLog = productLogRepository.findFirstByProductIdentifierAndIgnoredOrderByDateDesc(productIdentifier, false);
            productLog.set_id(null);
            productLog.setAction(LogAction.DELETE);
            productLog.setDeleted(true);
            productLog.setPublished(false);
            productLog.setDate(LocalDateTime.now());
            productLogRepository.save(productLog);
        } else {
            throw new IllegalArgumentException("The Product with the given ProductNumber was not found");
        }
    }

    @Override
    public void deleteAllProducts() {
        productRepository.deleteAll();
    }

    @Override
    public Product findProductById(String id) {
        Product product = productRepository.findOne(id);
        if (product != null) {
            return productRepository.findOne(id);
        } else {
            throw new IllegalArgumentException("Product not found in the Database");
        }
    }

    @Override
    public Product findProductByProductIdentifier(int number) {
        Product product = productRepository.findByProductIdentifier((long) number);
        if (product != null) {
            return product;
        } else {
            throw new IllegalArgumentException("Product not found in the Database");
        }
    }

    @Override
    public List<Product> findProductByNumberOrName(String searchString) {
        int searchStringAsInt;
        List<Product> products;
        try {
            searchStringAsInt = Integer.parseInt(searchString);
            products = productRepository.findByNumberOrNameLikeIgnoreCase(searchStringAsInt, searchString);
        } catch (NumberFormatException e) {
            products = productRepository.findByNameLikeIgnoreCase(searchString);
        }

        return products;
    }

    @Override
    public List<String> getAllAtributes() {
        return productLogRepository.adminListProducts().stream().flatMap(product -> product.getAttributes().stream()).distinct().collect(Collectors.toList());
    }

    @Override
    public List<Product> getByAtributes(List<String> attributes) {
        return productRepository.findByAttributesIn(attributes);
    }

    @Override
    public void reduceStockAmountForProductGuid(Product product) {
        Product productFromDb = findProductById(product.getId());
        if ((productFromDb.getStockAmount() - product.getStockAmount()) > 0) {
            productFromDb.setStockAmount(productFromDb.getStockAmount() - product.getStockAmount());
        } else {
            productFromDb.setStockAmount(0);
        }
        productRepository.save(productFromDb);
        socketRemoteInterface.sendToAll("/topic/stockAmountChanged", "rofl");
    }
}
