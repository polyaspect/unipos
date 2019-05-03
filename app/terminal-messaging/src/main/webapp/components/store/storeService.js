//# sourceURL=storeService.js
define([
    'angular'
], function () {
    return ["$resource", "$http", function ($resource, http) {

        $this = this;

        var storeResource = $resource('/data/stores', {}, {
            findAvailableStores: {
                url: "/data/stores/findByUser",
                method: "GET",
                isArray: true
            },
            findDefaultStoreOfDevice: {
                url: '/socket/device/getStoreByDeviceId',
                method: "GET",
                isArray: false
            }
        });

        $this.findAvailableStores = function () {
            return storeResource.findAvailableStores();
        };

        $this.findDefaultStoreOfDeviceId = function (deviceId) {
            return storeResource.findDefaultStoreOfDevice({deviceId: deviceId});
        };

        $this.addStoreAndPrinterToDevice = function (deviceName, deviceId, store, printer) {
            return http({
                method: 'POST',
                url: "/socket/device/addStoreAndPrinterToNewDevice",
                data: "storeGuid=" + store.guid + "&printerGuid=" + printer.guid + (deviceId == undefined ? "": "&deviceId=" + deviceId) + "&deviceName=" + deviceName,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            });
        }

    }];
});