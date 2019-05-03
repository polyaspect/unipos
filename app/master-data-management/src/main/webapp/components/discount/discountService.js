//# sourceURL=discountService.js
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

        var discountResource = $resource('/data/discounts', {}, {
            findAll: {
                url:'/data/adminDiscounts',
                method: 'GET',
                isArray: true
            },
            deleteDiscountByIdentifier: {
                url:'/data/discounts/discountIdentifier',
                method:'DELETE',
                isArray:false
            },
            publishChanges: {
                url:'/data/adminDiscounts/publishChanges',
                method:'POST',
                isArray:false
            },
            resetChanges: {
                url:'/data/adminDiscounts/resetChanges',
                method:'POST',
                isArray:false
            }
        });

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof discountResource);
        };

        this.findAll = function () {
            var discounts = discountResource.findAll();
            return discounts;
        };

        this.update = function (discount) {
           return discount.$save();
        };

        this.save = function(discount) {
            discount.discountUsage = discount.discountUsage == "" ? null : discount.discountUsage;
            discount.discountType = discount.discountType == "" ? null : discount.discountType;
            return discountResource.save(discount);
        };

        this.deleteByDiscountIdentifier = function(discountIdentifier) {
            return discountResource.deleteDiscountByIdentifier({discountIdentifier: discountIdentifier});
        };

        this.publishChanges = function() {
            return discountResource.publishChanges();
        };

        this.resetChanges = function() {
            return discountResource.resetChanges();
        }
    }];
});