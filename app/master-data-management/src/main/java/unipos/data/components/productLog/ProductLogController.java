package unipos.data.components.productLog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wordnik.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.TypedAggregation;
import org.springframework.data.mongodb.core.mapreduce.GroupBy;
import org.springframework.data.mongodb.core.mapreduce.GroupByResults;
import org.springframework.http.HttpStatus;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.data.components.exception.DataNotFoundException;
import unipos.data.components.product.Product;
import unipos.data.components.product.ProductService;
import unipos.data.components.shared.UrlContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * Created by dominik on 24.08.15.
 */

//This Controller handles all the methods, that need a special handling, because the admin is working on  it, and every task needs to be logged

@RestController
@RequestMapping("/adminProducts")
public class ProductLogController {

    @Autowired
    ProductService productService;
    @Autowired
    SocketRemoteInterface socketRemoteInterface;
    @Autowired
    ProductLogService productLogService;
    RestTemplate restTemplate = new RestTemplate();
    Gson gson = new GsonBuilder().serializeNulls().create();

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Product> listAll() {
        return productLogService.adminListProducts();
    }

    @RequestMapping(value = "/companyGuid/{companyGuid}", method = RequestMethod.GET)
    public List<Product> listProductsByCompanyGuid(@PathVariable("companyGuid") String companyGuid, HttpServletResponse response) throws IOException {
        if(companyGuid == null || companyGuid.isEmpty()) {
            response.sendError(400, "The given companyGuid is empty");
            return null;
        }

        List<Product> products = null;

        try {
            products = productLogService.listProductsByCompanyGuid(companyGuid);
        } catch (DataNotFoundException e) {
            response.sendError(500, e.getMessage());
            return null;
        }

        return products;
    }

    //Get the Next Available ProductNumber
    @RequestMapping(value = "/highestProductNumberProduct", method = RequestMethod.GET)
    @ApiOperation(value = "Get the Product from the DB with the highest ProductNumber")
    public ProductLog getNextProductNumber() {
        ProductLog product = productLogService.getProductWithHighestProductNumber();
        if (product == null) {
            //I create a new Product with the ID 0, so that the GUI gets an Element with the ID 0 --> It should write the 1 as next Number
            return ProductLog.newProductLogFromProduct(new Product(null, 0L, "", "", null, null));
        }
        return product;
    }


    /**
     * Write the Changes that the Admin made to the Log Table to the "public" visible Products Collection
     */
    @ApiOperation("This Method produces a flush so that the dirty Changes get written inside the Products Table")
    @RequestMapping(value = "/publishChanges", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void publishChanges(HttpServletRequest request) {
        productLogService.publishChanges();

        socketRemoteInterface.sendToAll("/topic/updatedPublish", gson.toJson(productService.listProducts()));
    }

    @RequestMapping(value = "/resetChanges", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void resetChanges() {
        productLogService.resetChanges();
    }

    @RequestMapping(value = "/getMaxSortOrder", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public int getMaxSortOrder() {
        return productLogService.getMaxSortOrder();
    }
}
