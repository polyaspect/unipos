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

        $this = this;

        var printerResource = $resource('/printer/printers', {}, {
            findAvailablePrinter: {
                url: "/printer/printers",
                method: "GET",
                isArray:true
            },
            findDefaultPrinterOfDeviceId: {
                url: '/socket/device/findDefaultPrinterOfDeviceId',
                method: "GET",
                isArray: false
            },
            addDefaultPrinter: {
                url: '/socket/device/addPrinterToDevice',
                method: 'POST',
                isArray:false
            }
        });

        $this.findAvailablePrinter = function () {
            return printerResource.findAvailablePrinter();
        };

        $this.findDefaultPrinterOfDeviceId = function (deviceId) {
            return printerResource.findDefaultPrinterOfDeviceId({deviceId : deviceId});
        };

        $this.addDefaultPrinter = function (printer) {
            var printerDto = {
                printerGuid: printer.guid,
                defaultPrinter: true
            };
            return printerResource.addDefaultPrinter(printerDto);
        };
    }];
});