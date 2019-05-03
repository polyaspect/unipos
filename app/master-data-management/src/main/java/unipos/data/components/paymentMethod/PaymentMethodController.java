package unipos.data.components.paymentMethod;

import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.data.components.paymentMethodLog.PaymentMethodLogService;
import unipos.data.components.product.Product;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by dominik on 31.08.15.
 */

@RestController
@RequestMapping("/paymentMethods")
public class PaymentMethodController {

    @Autowired
    PaymentMethodService paymentMethodService;
    @Autowired
    PaymentMethodLogService paymentMethodLogService;

    /**
     * @param paymentMethod the Product Entity to log
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void savePaymentMethod(@ApiParam @RequestBody PaymentMethod paymentMethod) {
        //Check if this is an update or  a new paymentMethod
        if(!paymentMethodLogService.existsProductNumber(paymentMethod.getPaymentMethodIdentifier())) {
            //Save the paymentMethod as a new Product inside the Product Collection
            paymentMethodService.savePaymentMethod(paymentMethod);
        } else {
            //If there exists an ID, we want to update an already existing entity
            paymentMethodService.updatePaymentMethod(paymentMethod);
        }
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<PaymentMethod> listPaymentMethods() {
        List<PaymentMethod> paymentMethods = paymentMethodService.findAll();
        return paymentMethods;
    }

    @RequestMapping(value = "/paymentMethodIdentifier", method = RequestMethod.DELETE)
    public void deleteProductByProductNumber(@RequestParam("paymentMethodIdentifier") Long paymentMethodIdentifier, HttpServletResponse response) {
        if (paymentMethodIdentifier > 0L) {
            try {
                paymentMethodService.deletePaymentMethodByPaymentMethodIdentifier(paymentMethodIdentifier);
            } catch (IllegalArgumentException e) {
                response.setStatus(404);
            }
        } else {
            response.setStatus(400);
        }
    }

    @RequestMapping(value = "/guid/{guid}", method = RequestMethod.GET)
    public PaymentMethod findByGuid(@PathVariable("guid") String guid) {
        return paymentMethodService.findByGuid(guid);
    }
}
