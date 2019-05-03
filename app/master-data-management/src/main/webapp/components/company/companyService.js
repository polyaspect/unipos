//# sourceURL=companyService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", function ($resource) {

        var companyResource = $resource('/data/companies', {}, {
            update: {
                method: 'PUT',
                isArray:false,
                url: '/data/companies'
            },
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
            if(company.id == null){
                return companyResource.save(company).$promise;
            }else{
                return companyResource.update(company).$promise;
            }
        };

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof companyResource);
        };

        this.saveResourceInstance = function (company) {
            if(company.id == null){
                return company.$save();
            }else{
                return company.$update();
            }
        };

        this.deleteByGuid = function(guid) {
            return companyResource.deleteCompanyByGuid({guid: guid});
        };
    }];
});