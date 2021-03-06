//# sourceURL=pos.addNewDeviceController.js
define([
    'angular',
    'pos.angularResource'
], function (angular) {
    return ['$rootScope', '$scope', '$http', 'pos.directiveIdsConst', 'pos.areaService', 'pos.areaNames', 'pos.dataService', '$timeout', 'pos.deviceService',
        'pos.ui.elements.text.textDirectiveService', 'pos.abstractStrategyFactory', "pos.jkStoreChooserService", 'pos.printerService', 'pos.loginService',
        function (rootScope, scope, http, directiveIdsConst, areaService, areaNames, dataService, timeout, deviceService, textService, abstractStrategyFactory, jkStoreChooserService, printerService, loginService) {
            rootScope.showNav = false;
            http.get("/design/screenCollection/default").then(function (response) {
                jkStoreChooserService.getAllStores();
                loginService.setInitValues();
                abstractStrategyFactory.changeState("DEVICE_NOT_SETUP");

                let approxSizes = deviceService.getApproxSize();
                let screens;
                for (size of approxSizes.data) {
                    screens = Enumerable.From(response.data.screens).Where(function (x) {
                        return x.settings.orientation == approxSizes.orientation && x.settings.size == size.size && x.settings.function == "SELECTSTORE";
                    }).ToArray();
                    if (screens.length > 0) {
                        break;
                    }
                }
                if (screens.length <= 0) {
                    noScreensFound();
                    return;
                }
                scope.areaConfigCollection = screens;

                areaService.data = scope.areaConfigCollection;
                areaService.areaNames = Enumerable.From(scope.areaConfigCollection).Select(function (x) {
                    return x.name;
                }).ToArray();
                dataService.setByDirectiveKey("areaService", directiveIdsConst.areaConfigAll, scope.areaConfigCollection);
                dataService.setByDirectiveKey(areaService.serviceName, directiveIdsConst.areaConfigAll, response.data);
                areaService.toScreen(areaService.areaNames[0]);
                timeout(function () {
                    textService.setText(textService.textModes.info, "Bitte geben Sie einen Namen für dieses Gerät ein:");
                }, 1);
            }, function (response) {
                if (response.status = 400) {
                    noScreensFound();
                }
            });

            function noScreensFound() {
                swal("Keine Oberfläche zur Geräte-Einrichtung gefunden", "Es wurde keine Oberfläche für dieses Design mit dem Typ 'Geräte-Einrichtung' gefunden. Bitte legen Sie diese im Designer an!", "error");
            }
        }];
});