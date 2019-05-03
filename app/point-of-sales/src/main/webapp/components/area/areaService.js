//# sourceURL=pos.areaService.js
define([
    'angular'
], function (angular) {
    return ["pos.dataService", 'pos.directiveIdsConst', 'pos.urlSettings', '$http', 'pos.areaNames', '$rootScope',
        function (dataService, directiveIdsConst, urlSettings, http, areaNames, rootScope) {
            this.serviceName = "areaService";
            this.currentAreaConfigName = "";
            this.areaNames = [];
            this.keys = {
                areaConfigAll: "allAreaConfigs",
            };
            this.config = {
                storeGuid: "",
                designTyp: "",
                device: "",
            };
            var self = this;
            dataService.setByServiceKey(self.serviceName, {});

            this.setDirectiveData = function (directiveId, data) {
                dataService.setByDirectiveKey(self.serviceName, directiveId, data);
            };

            this.getDirectiveData = function (directiveId) {
                return dataService.getByDirectiveKey(self.serviceName, directiveId);
            };
            this.doAction = function (cell) {
                self.toScreen(cell.serviceData.areaName);
            };
            this.toScreen = function (areaName) {
                self.currentAreaConfigName = areaName;
                rootScope.$emit(self.serviceName + "currentConfig", self.currentAreaConfigName);
            };

            this.getByStoreId = function (catalogs, storeGuid) {
                return Enumerable.From(catalogs).FirstOrDefault(undefined, function (x) {
                    var storeFound = Enumerable.From(x.stores).Any(function (x) {
                        return x.guid == storeGuid;
                    });
                    return !x.vorlage && (!x.stores || storeFound);
                });
            };
            this.getByName = function (catalogs, catalogName) {
                return Enumerable.From(catalogs).FirstOrDefault(undefined, function (x) {
                    return x.name == catalogName;
                });
            };
            this.getDesignByTyp = function (catalog, designTyp) {
                return Enumerable.From(catalog.data).FirstOrDefault(undefined, function (x) {
                    return x.Value.active && x.Value.typ == designTyp;
                }).Value;
            };
            this.getDesignForDevice = function (designTyp, device) {
                return Enumerable.From(designTyp.data).FirstOrDefault(undefined, function (x) {
                    return x.Key == device;
                }).Value;
            };
            this.getDefaultScreen = function (designDevice) {
                return Enumerable.From(designDevice.data).FirstOrDefault(undefined, function (x) {
                    return x.Value.default;
                }).Value;
            };
            this.getDesignForDeviceByConfig = function (config) {
                var defaultKatalog = self.getByStoreId(self.getDirectiveData(self.keys.areaConfigAll), config.storeGuid);
                var mainDesign = self.getDesignByTyp(defaultKatalog, config.designTyp);
                var designDevice = self.getDesignForDevice(mainDesign, config.device);
                return designDevice;
            };
            this.getDefaultScreenByConfig = function (config) {
                var defaultKatalog = self.getByStoreId(self.getDirectiveData(self.keys.areaConfigAll), config.storeGuid);
                var mainDesign = self.getDesignByTyp(defaultKatalog, config.designTyp);
                var designDevice = self.getDesignForDevice(mainDesign, config.device);
                return self.getDefaultScreen(designDevice);
            };
            this.toDefaultScreen = function () {
                var defaultScreen = dataService.getByDirectiveKey(self.serviceName, directiveIdsConst.areaConfigAll)[0];
                self.toScreen(defaultScreen.name);
            }
        }];
});