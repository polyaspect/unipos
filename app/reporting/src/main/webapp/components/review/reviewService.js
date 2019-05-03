//# sourceURL=reviewService.js
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
    return ["$resource", "$http", function ($resource, $http) {

        var invoiceResource = $resource('/pos/invoices', {}, {
            findByCompanyGuid: {
                url: '/pos/invoices/findByCompanyGuid/:companyGuid',
                method: 'GET',
                isArray:true
            },
            getUserCompanyGuid: {
                url: '/auth/auth/getCompanyGuidByAuthToken',
                method: 'GET',
                isArray: false
            }
        });

        this.findAll = function() {
            var promise = invoiceResource.query();
            return promise;
        };
        this.getInvoicePreviewText = function(invoice) {
            return $http.post("/report/invoiceReports/getInvoiceText", invoice);
        };
        this.reprintInvoice = function(invoice) {
            return $http.post("/report/invoiceReports/reprintInvoice", invoice);
        };
        this.revertInvoice = function (invoiceGuid) {
            var req = {
                method: 'POST',
                url: '/pos/invoices/revertInvoice',
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded'
                },
                data: "invoiceGuid=" + invoiceGuid
            };

            return $http(req);
        };
        this.findbyCompanyGuid = function(companyGuid) {
            return invoiceResource.findByCompanyGuid({companyGuid:companyGuid});
        };

        this.getCompanyGuidByAuthToken = function () {
            return $http.get("/auth/auth/getCompanyGuidByAuthToken");
        };
    }];
});