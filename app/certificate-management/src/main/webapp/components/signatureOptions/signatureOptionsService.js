//# sourceURL=signatureOptionsService.js
/**
 * Created by dominik on 04.08.15.
 */

define(["angular"], function (angular) {
    return ["$resource", "$http", function ($resource, $http) {

        var signatureOptionsService = $resource('/signature/signatureOptions', {}, {
            deleteByStoreGuid: {
                method: 'DELETE',
                url: '/signature/signatureOptions',
                isArray: false
            },
            isFirstSignatureOptionForStore: {
                method: 'GET',
                url: '/signature/signatureOptions/isFirstSignatureOptionForStore',
                isArray: false
            },
            encryptionDetails: {
                method: 'GET',
                url: '/signature/signatureOptions/encryptionDetails',
                isArray: false
            }
        });

        var storeResource = $resource('/data/stores/findByUser', {}, {
            findAllStoreGuids: {
                method: "GET",
                url: "/data/stores/findByUser",
                isArray: true
            }
        });

        this.findAll = function () {
            return signatureOptionsService.query();
        };

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof signatureOptionsService);
        };

        this.update = function (signatureOption) {
            return signatureOption.$save();
        };

        this.save = function(signatureOption) {
            return signatureOptionsService.save(signatureOption);
        };

        this.deleteByStoreGuid = function(storeGuid) {
            return signatureOptionsService.deleteByStoreGuid({storeGuid: storeGuid});
        };

        this.findEncryptionDetailsById = function(id){
            return signatureOptionsService.encryptionDetails({id: id});
        };

        this.isFirstSignatureOptionForStore = function() {
            return $http.get("/signature/signatureOptions/isFirstSignatureOptionForStore")
        };

        this.findAllStores = function () {
            return storeResource.query();
        };

        this.randomString = function(size){
            var result = '';
            for (var i = size; i > 0; --i) result +=  '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'[Math.floor(Math.random() *  '0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ'.length)];
            return result;
        };
    }];
});