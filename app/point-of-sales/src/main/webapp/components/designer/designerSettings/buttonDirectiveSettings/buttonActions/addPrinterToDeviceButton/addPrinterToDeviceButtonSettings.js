//# sourceURL=pos.design.jkButton.addPrinterToDeviceButtonSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.addPrinterToDeviceButtonSettings'
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope', '$timeout',
        function (initOptions, screenService, styleService, rootScope, timeout) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/addPrinterToDeviceButton/addPrinterToDeviceButtonSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {
                        printerGrid: undefined
                    };
                    scope.parentCss = styleService.getGlobalCssData().button.data.addPrinterToDeviceFunction.data || {};

                    scope.printerGridDirectives = screenService.getAreaByDirAndService("pos.ui.elements.grid.grid-directive", "pos.printerGridService");
                    if (scope.area && scope.area.data) {
                        scope.model.buttonText = scope.area.data.name;
                        var storeChooser = (Enumerable.From(scope.printerGridDirectives).FirstOrDefault(undefined, function (x) {
                            return x.data.id == scope.area.data.serviceData.printerGrid;
                        }));
                        if (storeChooser) {
                            scope.model.printerGrid = storeChooser.data.id;
                        }
                    }
                    scope.save = function () {
                        var areaConfig = scope.area;
                        areaConfig.data = {};
                        areaConfig.data.name = scope.model.buttonText;
                        areaConfig.data.serviceName = "pos.printerService";
                        areaConfig.data.serviceAction = "addPrinterToDevice";
                        areaConfig.data.serviceData = {
                            printerGrid: scope.model.printerGrid
                        };
                        areaConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});