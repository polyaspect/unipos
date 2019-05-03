//# sourceURL=pos.addNewDeviceService.js
define([
    'angular'
], function (angular) {
    return ['$http', '$rootScope', 'pos.ui.elements.text.textDirectiveService', 'pos.urlSettings', '$location', 'pos.printerService', function (http, rootScope, textService, urlSettings, location, printerService) {
        var self = this;
        this.doAction = function (cell) {
            var selectedStore = rootScope.jkGridApi[cell.serviceData.jkStoreChooser].selection.getSelectedRows()[0];
            if (selectedStore != undefined) {
                selectedStore = selectedStore.value;
            } else {
                swal("Keine Filiale ausgewählt", "Bitte wählen Sie die Filiale aus der Liste aus, in welcher sich dieses Gerät befindet!", "info");
                return;
            }
            var deviceName = textService.getText(textService.textModes.keyboard);
            if (deviceName == undefined) {
                swal("Kein Geräte-Name eingegeben", "Bitte benennen Sie dieses Gerät mit einem eindeutigem Namen, mit welchem Sie das Gerät identifizieren können!", "info");
                return;
            }

            if (selectedStore == undefined) {
                swal("Keine Filiale ausgewählt", "Bitte wählen Sie die Filiale aus der Liste aus, in welcher sich dieses Gerät befindet!", "info");
                return;
            }

            http({
                method: 'POST',
                url: urlSettings.createNewDevice,
                data: "storeGuid=" + selectedStore.guid + "&deviceName=" + deviceName,
                headers: {'Content-Type': 'application/x-www-form-urlencoded'}
            }).success(function (data) {
                printerService.getAllPrinters();
                self.setCashier().then(function () {
                    location.path("/pos");
                });
            }).error(function (data) {
            });
        };
        this.setCashier = function () {
            return http.post('/socket/device/setCashierForWorkstation', rootScope.pos.user.id)
        }
    }];
});