package unipos.data.components.company;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import unipos.authChecker.annotations.RequiresPermissions;
import unipos.authChecker.interceptors.AuthenticationManager;
import unipos.data.components.company.model.Company;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * Created by dominik on 04.09.15.
 */

@RestController
@RequestMapping(value = "/companies", produces = "application/json")
public class CompanyController {

    @Autowired
    CompanyService companyService;

    @Autowired
    AuthenticationManager authenticationManager;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<Company> findAllCompanies() {
        List<Company> company = companyService.findAll();
        return company;
    }

    @RequiresPermissions("data.createCompanies")
    @RequestMapping(value = "", method = RequestMethod.POST)
    public Company newCompany(@RequestBody Company company, HttpServletResponse response) {
        Company result = null;
        try {
            if (company.getId() != null && !company.getId().isEmpty()) {
                response.setStatus(400);
            } else {
                companyService.saveCompany(company);
                response.setStatus(200);
            }
            result = companyService.findByName(company.getName());
        } catch (CompanySaveMoreThanOneException e) {
            response.setStatus(400);
        }
        return result;
    }

    @RequiresPermissions("data.updateCompanies")
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public Company updateCompany(@RequestBody Company company, HttpServletResponse response) {
        Company result = null;
        try {
            if (company.getId() != null && !company.getId().isEmpty()) {
                companyService.updateCompany(company);
                response.setStatus(200);
            } else {
                response.setStatus(400);
            }
            result = companyService.findByName(company.getName());
        } catch (CompanySaveMoreThanOneException e) {
            response.setStatus(400);
        }
        return result;
    }


    @RequiresPermissions("data.deleteCompanies")
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    public void deleteCompany(@RequestBody Company company) {
        companyService.deleteCompanyByMongoDbId(company.getId());
    }

    @RequestMapping(value = "/guid", method = RequestMethod.DELETE)
    @ResponseStatus(HttpStatus.OK)
    @RequiresPermissions("data.deleteCompanies")
    public void deleteByGuid(@RequestParam("guid") String guid) {
        companyService.deleteByGuid(guid);
    }

    @RequestMapping(value = "/guid/{guid}", method = RequestMethod.GET)
    public Company findByGuid(@PathVariable("guid") String guid) {
        return companyService.findByGuid(guid);
    }
}
