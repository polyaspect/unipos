package unipos.data.components.discountLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import unipos.data.components.discount.Discount;
import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.components.paymentMethodLog.PaymentMethodLog;
import unipos.data.components.paymentMethodLog.PaymentMethodLogGroup;
import unipos.data.components.paymentMethodLog.PaymentMethodLogRepositoryCustom;
import unipos.data.components.productLog.LogAction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by dominik on 31.08.15.
 */
public class DiscountLogRepositoryImpl implements DiscountLogRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<Discount> adminListDiscounts() {
        List<DiscountLog> discountLogs = new ArrayList<>();
        List<Discount> discounts = new ArrayList<>();
        TypedAggregation<DiscountLog> aggregation = Aggregation.newAggregation(DiscountLog.class,
                project("_id", "discountIdentifier", "date", "Action", "deleted", "published", "discount", "ignored"),
                match(Criteria.where("ignored").is(false)),
                sort(Sort.Direction.ASC, "discountIdentifier"),
                sort(Sort.Direction.ASC, "date"),
                group("discountIdentifier")
                        .last("_id").as("logId")
                        .last("discountIdentifier").as("discountIdentifier")
                        .last("date").as("date")
                        .last("Action").as("Action")
                        .last("deleted").as("deleted")
                        .last("published").as("published")
                        .last("discount").as("discount")
                        .last("ignored").as("ignored"));



        AggregationResults<DiscountLogGroup> result = mongoTemplate.aggregate(aggregation, DiscountLogGroup.class);
        discounts.addAll(result.getMappedResults().stream().filter(discountLogGroup -> discountLogGroup.getAction() != LogAction.DELETE).map(DiscountLogGroup::getDiscount).collect(Collectors.toList()));

        discountLogs.forEach((x) -> discounts.add(x.getDiscount()));

        return discounts;
    }

    @Override
    public Long setAllUnpublishedLogsToIgnored() {
        return new Long(mongoTemplate.updateMulti(Query.query(Criteria.where("published").is(false)), Update.update("ignored", true), DiscountLog.class).getN());
    }
}
