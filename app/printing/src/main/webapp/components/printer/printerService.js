//# sourceURL=printerService.js
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
define([
    'angular'
], function () {
    return ["$resource", function ($resource) {

        var printerResource = $resource('/printer/printers', {}, {
            deletePrinterByMongoDbId: {
                url:'/printer/printers/dbId',
                method:'DELETE',
                isArray:false
            },
            uploadLogo: {
                url: '/printer/printing/uploadLogo',
                method: 'POST',
                transformRequest: formDataObject,
                headers: {'Content-Type':undefined, enctype:'multipart/form-data'}
            },
            testPrinting: {
                url: '/printer/printing/testPrinting',
                method: 'POST'
            },
            printCurrentLogo: {
                method: 'POST',
                url: '/printer/printing/printCurrentLogo',
                isArray: false
            }
        });

        function formDataObject (data) {
            var fd = new FormData();
            angular.forEach(data, function(value, key) {
                fd.append(key, value);
            });
            return fd;
        }

        this.checkIfResourceInstance = function(instance) {
            return (instance instanceof printerResource);
        };

        this.findAll = function () {
            var printers = printerResource.query();
            return printers;
        };

        this.update = function (printer) {
           return printer.$save();
        };

        this.save = function(printer) {
            return printerResource.save(printer);
        };

        this.deleteByProductNumber = function(documentId) {
            return printerResource.deletePrinterByMongoDbId({documentId: documentId});
        };

        this.testPrinting = function(printer) {
            return printerResource.testPrinting(printer)
        };

        this.uploadLogo = function (logo, printer) {
            return printerResource.uploadLogo({file:logo, printerId: printer.printerId});
        };

        this.printCurrentLogo = function(printer) {
            return printerResource.printCurrentLogo(printer);
        }
    }];
});