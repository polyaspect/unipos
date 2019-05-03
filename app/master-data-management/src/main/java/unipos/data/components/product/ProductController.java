package unipos.data.components.product;

import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import unipos.common.container.RequestHandler;
import unipos.common.remote.auth.AuthRemoteInterface;
import unipos.common.remote.auth.model.Token;
import unipos.common.remote.socket.SocketRemoteInterface;
import unipos.data.components.company.CompanyService;
import unipos.data.components.company.StoreService;
import unipos.data.components.company.model.Company;
import unipos.data.components.company.model.Store;
import unipos.data.components.exception.DataNotFoundException;
import unipos.data.components.productLog.ProductLogService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Dominik on 23.07.2015.
 */
@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@Api(value = "/products")
public class ProductController {

    @Autowired
    ProductService productService;
    @Autowired
    ProductLogService productLogService;
    @Autowired
    StoreService storeService;
    @Autowired
    AuthRemoteInterface authRemoteInterface;
    @Autowired
    CompanyService companyService;
    //Save Products

    /**
     * @param product the Product Entity to log
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ApiOperation(value = "Save a new Product")
    @ResponseStatus(HttpStatus.OK)
    public void saveProduct(@ApiParam @RequestBody Product product) {
        //check if there's an valid Category set
        if (product.getCategory() == null || product.getCategory().getId() == null || product.getCategory().getId().isEmpty()) {
            product.setCategory(null);
        }

        //Check if this is an update or  a new paymentMethod
        if (!productLogService.existsProductNumber(product.getProductIdentifier())) {
            //Save the paymentMethod as a new Product inside the Product Collection
            productService.saveProduct(product);
        } else {
            //If there exists an ID, we want to update an already existing entity
            productService.updateProduct(product);
        }
    }

    //Search for Products

    @RequestMapping(value = "/productNumberOrName/{searchString}", method = RequestMethod.GET)
    @ApiOperation(value = "List some Products by their Name, or their ProductNumber",
            response = Product.class,
            responseContainer = "List")
    @ApiResponses({
            @ApiResponse(code = 404, message = "The desired Product was not found")
    })
    public List<Product> findProductsByProductNumberOrName(@PathVariable("searchString") String searchString, HttpServletResponse response) {
        List<Product> products = productService.findProductByNumberOrName(searchString);
        if (products.size() > 0) {
            return products;
        } else {
            response.setStatus(404);
            return null;
        }
    }

    //List Products

    /**
     * This method is just used by the Clients, that access the Product Data. The Administrative List Method is in @Code ProductLogController.listall()
     *
     * @return all already published items.
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    @ApiOperation(value = "List all stored products",
            response = Product.class,
            responseContainer = "List")
    public List<Product> listProducts(HttpServletRequest request, HttpServletResponse response) throws IOException {

        Store store = storeService.getStoreByUserAndDevice(request);
        if (store == null) {
            response.sendError(400, "You are not logged in or are not assigned to a valid device.");
            return null;
        }
        List<Product> products = productService.listProductsByStore(store);
        return products;
    }

    @RequestMapping(value = "/company", method = RequestMethod.GET)
    public List<Product> listProductsByCompany(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authToken = RequestHandler.getCookieValue(request, "AuthToken");

        if (authToken == null || authToken.isEmpty()) {
            response.sendError(400, "You have no valid AuthToken Cookie");
            return null;
        }

        Token token = authRemoteInterface.getAuthTokenByRequest(request);
        Company company = companyService.findByGuid(token.getUser().getCompanyGuid());

        if (company == null) {
            response.sendError(400, "You have no valid AuthToken Cookie");
            return null;
        }

        return productService.listProductsByCompany(company);
    }

    @RequestMapping(value = "/companyGuid/{companyGuid}", method = RequestMethod.GET)
    public List<Product> listProductsByCompanyGuid(@PathVariable("companyGuid") String companyGuid, HttpServletResponse response) throws IOException {
        if (companyGuid == null || companyGuid.isEmpty()) {
            response.sendError(400, "The given companyGuid is empty");
            return null;
        }

        List<Product> products = null;

        try {
            products = productService.listProductsByCompanyGuid(companyGuid);
        } catch (DataNotFoundException e) {
            response.sendError(500, e.getMessage());
            return null;
        }

        return products;
    }

    //List a single Product

    @ApiOperation(value = "List a specific Product by its MongoDB-ID",
            response = Product.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "The desired ID was not found in the DB")
    })
    @RequestMapping(value = "/dbId/{id}", method = RequestMethod.GET)
    public Product findProductByMongoId(@PathVariable("id") String id, HttpServletResponse response) {
        Product product = null;
        try {
            product = productService.findProductById(id);
        } catch (IllegalArgumentException e) {
            response.setStatus(404);
        }
        return product;
    }

    @RequestMapping(value = "/productNumber/{number}", method = RequestMethod.GET)
    @ApiOperation(value = "List a specific Product by its Product-Number",
            response = Product.class)
    @ApiResponses({
            @ApiResponse(code = 404, message = "The desired Product with the given Product-Number was not found in the DB")
    })
    public Product findProductByProductNumber(@PathVariable("number") String number, HttpServletResponse response) {
        Product product = null;
        try {
            product = productService.findProductByProductIdentifier(Integer.parseInt(number));
        } catch (IllegalArgumentException e) {
            response.setStatus(404);
        }
        return product;
    }

    //Delete Products

    @RequestMapping(value = "/dbId", method = RequestMethod.DELETE)
    @ApiOperation("Delete a Product by its MongoDB ID")
    @ApiResponses({
            @ApiResponse(code = 400, message = "The MongoDB ID is invalid"),
            @ApiResponse(code = 404, message = "The Product with the given MongoDB ID was not existent in the Database. So nothing has been deleted")
    })
    public void deleteProductByMongoId(@ApiParam @RequestParam("id") String id, HttpServletResponse response) {
        if (!id.isEmpty()) {
            try {
                productService.deleteProduct(id);
            } catch (IllegalArgumentException e) {
                response.setStatus(404);
            }
        } else {
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/productNumber", method = RequestMethod.DELETE)
    @ApiOperation("Delete a Product by its ProductNumber")
    @ApiResponses({
            @ApiResponse(code = 400, message = "The ProductNumber was smaller than 1"),
            @ApiResponse(code = 404, message = "The Product with the given ProductNumber was not existent in the Database. So nothing has been deleted")
    })
    public void deleteProductByProductNumber(@RequestParam("productNumber") Long productNumber, HttpServletResponse response) {
        if (productNumber > 0L) {
            try {
                productService.deleteProductByProductNumber(productNumber);
            } catch (IllegalArgumentException e) {
                response.setStatus(404);
            }
        } else {
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/updateStores", method = RequestMethod.POST)
    public void updateStores(@RequestBody Product product, HttpServletResponse response) throws IOException {
        productService.updateStores(product);
    }

    @RequestMapping(value = "/getAllAttributes", method = RequestMethod.GET)
    public List<String> getAllAtributes() {
        return productService.getAllAtributes();
    }

    @RequestMapping(value = "/getByAttributes", method = RequestMethod.GET)
    public List<Product> getByAtributes(@RequestParam("attributes") List<String> attributes) {
        return productService.getByAtributes(attributes);
    }

    @RequestMapping(value = "/reduceStockAmountForProductGuid", method = RequestMethod.POST)
    public void reduceStockAmountForProductGuid(@RequestBody Product product, HttpServletResponse response) throws IOException {
        productService.reduceStockAmountForProductGuid(product);
    }

    @RequestMapping(value = "/setSortOrderToNumber", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void setSortOrderToNumber() throws IOException {
        productService.setSortOrderToNumber();
    }
}
