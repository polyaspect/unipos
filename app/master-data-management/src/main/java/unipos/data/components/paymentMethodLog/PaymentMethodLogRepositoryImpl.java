package unipos.data.components.paymentMethodLog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import unipos.data.components.paymentMethod.PaymentMethod;
import unipos.data.components.productLog.LogAction;
import unipos.data.components.productLog.ProductLog;
import unipos.data.components.productLog.ProductLogGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by dominik on 31.08.15.
 */
public class PaymentMethodLogRepositoryImpl implements PaymentMethodLogRepositoryCustom {

    @Autowired
    MongoTemplate mongoTemplate;

    @Override
    public List<PaymentMethod> adminListPaymentMethods() {
        List<PaymentMethodLog> paymentMethodLogs = new ArrayList<>();
        List<PaymentMethod> paymentMethods = new ArrayList<>();
        TypedAggregation<PaymentMethodLog> aggregation = Aggregation.newAggregation(PaymentMethodLog.class,
                project("_id", "paymentMethodIdentifier", "date", "Action", "deleted", "published", "paymentMethod", "ignored"),
                match(Criteria.where("ignored").is(false)),
                sort(Sort.Direction.ASC, "paymentMethodIdentifier"),
                sort(Sort.Direction.ASC, "date"),
                group("paymentMethodIdentifier")
                        .last("_id").as("logId")
                        .last("paymentMethodIdentifier").as("paymentMethodIdentifier")
                        .last("date").as("date")
                        .last("Action").as("Action")
                        .last("deleted").as("deleted")
                        .last("published").as("published")
                        .last("paymentMethod").as("paymentMethod")
                        .last("ignored").as("ignored"));



        AggregationResults<PaymentMethodLogGroup> result = mongoTemplate.aggregate(aggregation, PaymentMethodLogGroup.class);
        paymentMethods.addAll(result.getMappedResults().stream().filter(paymentMethodLogGroup -> paymentMethodLogGroup.getAction() != LogAction.DELETE).map(PaymentMethodLogGroup::getPaymentMethod).collect(Collectors.toList()));

        paymentMethodLogs.forEach((x) -> paymentMethods.add(x.getPaymentMethod()));

        return paymentMethods;
    }

    @Override
    public Long setAllUnpublishedLogsToIgnored() {
        return new Long(mongoTemplate.updateMulti(Query.query(Criteria.where("published").is(false)), Update.update("ignored", true), PaymentMethodLog.class).getN());
    }
}
