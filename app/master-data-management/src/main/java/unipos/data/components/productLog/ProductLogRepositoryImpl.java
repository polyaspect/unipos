package unipos.data.components.productLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import unipos.data.components.company.model.Store;
import unipos.data.components.product.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by dominik on 25.08.15.
 */
public class ProductLogRepositoryImpl implements ProductLogRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Product> adminListProducts() {
        List<ProductLog> productLogs = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        TypedAggregation<ProductLog> aggregation = Aggregation.newAggregation(ProductLog.class,
                project("_id", "productIdentifier", "date", "action", "deleted", "published", "product", "ignored"),
                match(Criteria.where("ignored").is(false)),
                sort(Sort.Direction.ASC, "productIdentifier"),
                sort(Sort.Direction.ASC, "date"),
                group("productIdentifier")
                        .last("_id").as("logId")
                        .last("productIdentifier").as("productIdentifier")
                        .last("date").as("date")
                        .last("action").as("action")
                        .last("deleted").as("deleted")
                        .last("published").as("published")
                        .last("product").as("product")
                        .last("ignored").as("ignored"));


        AggregationResults<ProductLogGroup> result = mongoTemplate.aggregate(aggregation, ProductLogGroup.class);
        products.addAll(result.getMappedResults().stream().filter(productLogGroup -> productLogGroup.getAction() != LogAction.DELETE).map(ProductLogGroup::getProduct).collect(Collectors.toList()));

        productLogs.forEach((x) -> products.add(x.getProduct()));

        return products;
    }

    @Override
    public Long setAllUnpublishedLogsToIgnored() {
        return new Long(mongoTemplate.updateMulti(Query.query(Criteria.where("published").is(false)), Update.update("ignored", true), ProductLog.class).getN());
    }

    @Override
    public List<Product> adminListProductsByStores(List<Store> stores) {

        List<String> storeGuids = stores.stream().map(Store::getGuid).collect(Collectors.toList());

        List<ProductLog> productLogs = new ArrayList<>();
        List<Product> products = new ArrayList<>();
        TypedAggregation<ProductLog> aggregation = Aggregation.newAggregation(ProductLog.class,
                project("_id", "productIdentifier", "date", "action", "deleted", "published", "product", "ignored"),
                match(Criteria.where("ignored").is(false)),
                sort(Sort.Direction.ASC, "productIdentifier"),
                sort(Sort.Direction.ASC, "date"),
                group("productIdentifier")
                        .last("_id").as("logId")
                        .last("productIdentifier").as("productIdentifier")
                        .last("date").as("date")
                        .last("action").as("action")
                        .last("deleted").as("deleted")
                        .last("published").as("published")
                        .last("product").as("product")
                        .last("ignored").as("ignored"),
                match(Criteria.where("product.stores").in(storeGuids)));


        AggregationResults<ProductLogGroup> result = mongoTemplate.aggregate(aggregation, ProductLogGroup.class);
        products.addAll(result.getMappedResults().stream().filter(productLogGroup -> productLogGroup.getAction() != LogAction.DELETE).map(ProductLogGroup::getProduct).collect(Collectors.toList()));

        productLogs.forEach((x) -> products.add(x.getProduct()));

        return products;
    }


}
