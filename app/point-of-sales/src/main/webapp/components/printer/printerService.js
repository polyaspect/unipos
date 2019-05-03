//# sourceURL=pos.printerService.js
define([
    'angular'
], function (angular) {
    return ['pos.areaService', 'pos.initOptions', '$http', '$rootScope', '$location', '$cookies', function (areaService, initOptions, http, rootScope, location, cookies) {
        var printers = undefined;
        var self = this;
        this.getPrinters = function () {
            return self.printers;
        };
        this.addPrinterToDevice = function (areaData) {
            var selectedRows = rootScope.jkGridApi[areaData.serviceData.printerGrid].selection.getSelectedRows();
            if (selectedRows.length > 0) {
                angular.forEach(selectedRows, function (item, key) {
                    http.post("/socket/device/addPrinterToDevice", {
                        "printerGuid": item.value.guid,
                        "defaultPrinter": true
                    }).then(function () {
                        location.path(initOptions.baseUrl + "/pos");
                    });
                });
            }
        };
        this.getAllPrinters = function () {
            if (cookies.get("AuthToken") && cookies.get("DeviceToken")) {
                return http.get("/printer/printers/findPrinterByUsersStore").then(function (response) {
                    self.printers = response.data;
                });
            }
        };
    }];
});