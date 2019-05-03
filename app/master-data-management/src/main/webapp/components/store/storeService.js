//# sourceURL=storeService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", function ($resource) {

        var storeResource = $resource('/data/stores', {}, {
            deleteStoreByGuid: {
                method: 'DELETE',
                isArray:false,
                url: '/data/stores/guid'
            },
            addStoreToCompany: {
                method: "POST",
                url: "/data/stores/addStoreToCompany",
                isArray: false
            },
            findAllStoreGuids: {
                method: "GET",
                url: "/data/stores/guid",
                isArray: true
            },
            findStoreGuidsByCompanyGuid: {
                method: 'GET',
                url: "/data/stores/companyGuid/:companyGuid",
                isArray: true
            }
        });

        this.findAll = function () {
                var stores = storeResource.query();
            return stores;
        };

        this.save = function(store) {
            return storeResource.save(store);
        }

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof storeResource);
        };

        this.update = function (store) {
            return store.$save();
        };

        this.save = function(store) {
            return storeResource.save(store);
        };

        this.deleteByGuid = function(guid) {
            return storeResource.deleteStoreByGuid({guid: guid});
        };

        this.addStoreToCompany = function(storeGuid, companyGuid) {
            return storeResource.addStoreToCompany({storeGuid: storeGuid, companyGuid: companyGuid})
        };

        this.findAllStoreGuids = function() {
            return storeResource.findAllStoreGuids();
        };
        this.findStoreGuidsByCompanyGuid = function (companyGuid) {
            return storeResource.findStoreGuidsByCompanyGuid({companyGuid:companyGuid})
        }
    }];
});