//# sourceURL=companyService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", function ($resource) {

        var companyResource = $resource('/data/companies', {}, {
            deleteCompanyByGuid: {
                method: 'DELETE',
                isArray:false,
                url: '/data/companies/guid'
            }
        });

        this.findAll = function () {
                var companies = companyResource.query();
            return companies;
        };

        this.save = function(company) {
            return companyResource.save(company);
        }

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof companyResource);
        };

        this.update = function (company) {
            return company.$save();
        };

        this.save = function(company) {
            return companyResource.save(company);
        };

        this.deleteByGuid = function(guid) {
            return companyResource.deleteCompanyByGuid({guid: guid});
        };
    }];
});