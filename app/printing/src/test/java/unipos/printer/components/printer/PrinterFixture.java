package unipos.printer.components.printer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.test.context.ContextConfiguration;
import unipos.printer.components.printer.model.NetworkPrinter;
import unipos.printer.components.printer.model.Printer;
import unipos.printer.test.config.Fixture;
import unipos.printer.test.config.MongoTestConfiguration;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

/**
 * @author ggradnig
 */

@ContextConfiguration(classes={MongoTestConfiguration.class})
public class PrinterFixture implements Fixture{

    @Autowired
    private MongoOperations mongoOperations;

    Printer printer1;

    public void initEntities() {
        printer1 = NetworkPrinter.builder().ipAddress("192.168.1.87").port(9100).connectionType(ConnectionType.NETWORK).build();
    }

    @Override
    public void setUp() {
        initEntities();
        mongoOperations.save(printer1);
    }

    @Override
    public void tearDown() {
        mongoOperations.findAllAndRemove(query(where("_id").ne("-1")), Printer.class);

    }
}
