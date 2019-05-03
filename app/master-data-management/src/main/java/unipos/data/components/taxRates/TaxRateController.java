package unipos.data.components.taxRates;

import com.wordnik.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by dominik on 28.07.15.
 */

@RestController
@Api("/taxRates")
@RequestMapping("/taxRates")
public class TaxRateController {

    @Autowired
    TaxRateService taxRateService;

    //List all TaxRates

    @ApiOperation(value = "Return all stored TaxRates",
        response = TaxRate.class,
        responseContainer = "List")
    @ApiResponse(code = 400, message = "There is not Element Stored inside the Database")
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<TaxRate> findAllTaxRates(HttpServletResponse response) {
        List<TaxRate> taxRates = taxRateService.findAll();
        if(taxRates != null) {
            return taxRates;
        } else {
            response.setStatus(400);
            return null;
        }
    }

    //Save a new TaxRate

    @ApiOperation("Save a new TaxRate")
    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void saveTaxRate(@ApiParam @RequestBody TaxRate taxRate) {
        if(taxRate.getId() != null && !taxRate.getId().isEmpty())
            taxRateService.updateTaxRate(taxRate);
        else
            taxRateService.saveTaxRate(taxRate);
    }

    //Delete a TaxRate

    @ApiOperation("Delete a TaxRate by its Mongo-DB ID")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ApiResponse(code = 404, message = "Unable to Delete TaxRate with the given Mongo-DB ID")
    public void deleteTaxRateByMongoId(@RequestParam("id") String dbId, HttpServletResponse response) {
        try {
            taxRateService.deleteTaxRateByMongoId(dbId);
        } catch (IllegalArgumentException e) {
            response.setStatus(404);
        }
    }

    @RequestMapping(value = "/guid", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteByGuid(@RequestParam("guid") String guid) {
        taxRateService.deleteByGuid(guid);
    }

}
