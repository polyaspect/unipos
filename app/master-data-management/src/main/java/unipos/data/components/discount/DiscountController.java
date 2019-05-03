package unipos.data.components.discount;

import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.data.components.discountLog.DiscountLogService;
import unipos.data.components.paymentMethod.PaymentMethod;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by dominik on 31.08.15.
 */

@RestController
@RequestMapping(value = "/discounts", produces = "application/json")
public class DiscountController {

    @Autowired
    DiscountService discountService;
    @Autowired
    DiscountLogService discountLogService;

    /**
     * @param discount the Product Entity to log
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void saveDiscount(@ApiParam @RequestBody Discount discount) {
        //Check if this is an update or  a new paymentMethod
        if(!discountLogService.existsDiscountIdentifier(discount.getDiscountIdentifier())) {
            //Save the paymentMethod as a new Product inside the Product Collection
            discountService.saveDiscount(discount);
        } else {
            //If there exists an ID, we want to update an already existing entity
            discountService.updateDiscount(discount);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Discount> listDiscounts() {
        List<Discount> discounts = discountService.findAll();
        return discounts;
    }

    @RequestMapping("/dbId/{documentId}")
    public Discount getDiscountByMongoDbId(@PathVariable("documentId") String documentId) {
        return discountService.findByMongoDbId(documentId);
    }

    @RequestMapping(value = "/discountIdentifier", method = RequestMethod.DELETE)
    public void deleteDiscountByDiscountIdentifier(@RequestParam("discountIdentifier") Long discountIdentifier, HttpServletResponse response) {
        if (discountIdentifier > 0L) {
            try {
                discountService.deleteDiscountByDiscountIdentifier(discountIdentifier);
            } catch (IllegalArgumentException e) {
                response.setStatus(404);
            }
        } else {
            response.setStatus(400);
        }
    }
}
