//# sourceURL=productService.js
/*
 define([
 'angular',
 'angularResource'
 ], function () {
 var f = function ($scope, $resource) {
 this.productResource = $resource('/data/products');
 this.findAll = function () {
 return productResource.query();
 }
 };

 f.inject = ['$scope', '$resource'];
 return f;
 }
 );*/
define([], function () {
    return ["$resource", "$http", function ($resource, http) {
        this.allAttributes = undefined;
        var productResource = $resource('/data/products', {}, {
            findAll: {
                url: '/data/adminProducts',
                method: 'GET',
                isArray: true
            },
            findByNameOrNumber: {
                url: '/data/products/productNumberOrName/:searchString',
                method: 'GET',
                isArray: true
            },
            getNextValidProductNumber: {
                url: '/data/adminProducts/highestProductNumberProduct',
                method: 'GET',
                isArray: false
            },
            deleteProductByMongoDbId: {
                url: '/data/products/productNumber',
                method: 'DELETE',
                isArray: false
            },
            publishChanges: {
                url: '/data/adminProducts/publishChanges',
                method: 'POST',
                isArray: false
            },
            resetChanges: {
                url: '/data/adminProducts/resetChanges',
                method: 'POST',
                isArray: false
            },
            findByCompanyGuid: {
                url: '/data/adminProducts/companyGuid/:companyGuid',
                method: 'GET',
                isArray: true
            },
            updateStores: {
                url: '/data/products/updateStores',
                method: 'POST',
                isArray: false
            },
            getAllAttributes: {
                url: '/data/products/getAllAttributes',
                method: 'GET',
                isArray: true
            },
            getMaxSortOrder: {
                url: '/data/adminProducts/getMaxSortOrder',
                method: 'GET',
                isArray: false
            }
        });

        this.checkIfResourceInstance = function (instance) {
            return (instance instanceof productResource);
        };

        this.getNextValidProductNumber = function () {
            var highestProduct = productResource.getNextValidProductNumber();
            return highestProduct;
        };
        this.getmaxSortOrder = function () {
            return http.get("/data/adminProducts/getMaxSortOrder");
        };
        this.findAll = function () {
            var products = productResource.findAll();
            return products;
        };

        this.update = function (product) {
            return product.$save();
        };

        this.save = function (product) {
            if (product.customPriceInputAllowed) {
                product.price = null;
            }
            return productResource.save(product);
        };

        this.deleteByProductNumber = function (number) {
            return productResource.deleteProductByMongoDbId({productNumber: number});
        };

        this.searchProduct = function (searchString) {
            return productResource.findByNameOrNumber({searchString: searchString})
        };

        this.publishChanges = function () {
            return productResource.publishChanges();
        };

        this.resetChanges = function () {
            return productResource.resetChanges();
        };

        this.findbyCompanyGuid = function (companyGuid) {
            return productResource.findByCompanyGuid({companyGuid: companyGuid});
        };

        this.updateStores = function (product) {
            return productResource.updateStores(product);
        };
        this.getAllAttributes = function () {
            return productResource.getAllAttributes();
        }
    }];
});