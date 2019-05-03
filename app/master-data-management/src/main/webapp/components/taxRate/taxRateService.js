//# sourceURL=taxRateService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", function ($resource) {

        var taxRateResource = $resource('/data/taxRates', {}, {
            deleteTaxRateByGuid: {
                method: 'DELETE',
                isArray:false,
                url: '/data/taxRates/guid'
            }
        });

        this.findAll = function () {
                var taxRates = taxRateResource.query();
            return taxRates;
        };

        this.save = function(taxRate) {
            return taxRateResource.save(taxRate);
        }

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof taxRateResource);
        };

        this.update = function (taxRate) {
            return taxRate.$save();
        };

        this.save = function(taxRate) {
            return taxRateResource.save(taxRate);
        };

        this.deleteByGuid = function(guid) {
            return taxRateResource.deleteTaxRateByGuid({guid: guid});
        };
    }];
});