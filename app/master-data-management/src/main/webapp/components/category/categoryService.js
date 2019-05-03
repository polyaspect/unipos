//# sourceURL=categoryService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", function ($resource) {

        var categoryResource = $resource('/data/categories', {}, {
            deletePrinterByGuid: {
                method: 'DELETE',
                isArray:false,
                url: '/data/categories/guid'
            }
        });

        this.findAll = function () {
            var categories = categoryResource.query();
            return categories;
        };

        this.save = function(category) {
            return categoryResource.save(category);
        };

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof categoryResource);
        };

        this.update = function (category) {
            return category.$save();
        };

        this.save = function(category) {
            return categoryResource.save(category);
        };

        this.deleteByGuid = function(guid) {
            return categoryResource.deletePrinterByGuid({guid: guid});
        };
    }];
});