//# sourceURL=paymentMethodService.js
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
    return ["$resource", function ($resource) {

        var paymentResource = $resource('/data/paymentMethods', {}, {
            findAll: {
                url:'/data/adminPaymentMethods',
                method: 'GET',
                isArray: true
            },
            deletePaymentMethodByIdentifier: {
                url:'/data/paymentMethods/paymentMethodIdentifier',
                method:'DELETE',
                isArray:false
            },
            publishChanges: {
                url:'/data/adminPaymentMethods/publishChanges',
                method:'POST',
                isArray:false
            },
            resetChanges: {
                url:'/data/adminPaymentMethods/resetChanges',
                method:'POST',
                isArray:false
            }
        });

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof paymentResource);
        };

        this.getNextValidProductNumber = function() {
            var highestProduct = paymentResource.getNextValidProductNumber();
            return highestProduct;
        };

        this.findAll = function () {
            var products = paymentResource.findAll();
            return products;
        };

        this.update = function (paymentMethod) {
           return paymentMethod.$save();
        };

        this.save = function(paymentMethod) {
            return paymentResource.save(paymentMethod);
        };

        this.deleteByPaymentMethodIdentifier = function(paymentMethodIdentifier) {
            return paymentResource.deletePaymentMethodByIdentifier({paymentMethodIdentifier: paymentMethodIdentifier});
        };

        this.searchProduct = function(searchString) {
            return paymentResource.findByNameOrNumber({searchString: searchString})
        };

        this.publishChanges = function() {
            return paymentResource.publishChanges();
        };

        this.resetChanges = function() {
            return paymentResource.resetChanges();
        }
    }];
});