//# sourceURL=pos.design.jkButton.addNewDeviceSettings.js
define([
    'angular',
    'css!pos.designer.designerSettings.buttonDirectiveSettings.addNewDeviceButtonSettings'
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope',
        function (initOptions, screenService, styleService, rootScope) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/buttonDirectiveSettings/buttonActions/addNewDeviceButton/addNewDeviceButtonSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    scope.parentCss = styleService.getGlobalCssData().button.data.addNewDeviceFunction.data || {};
                    scope.storeChoosers = screenService.getAreaByDirAndService("pos.ui.elements.grid.grid-directive", "pos.jkStoreChooserService");
                    if (scope.area && scope.area.data) {
                        scope.model.buttonText = scope.area.data.name;
                        var storeChooser = (Enumerable.From(scope.storeChoosers).FirstOrDefault(undefined, function (x) {
                            return x.data.id == scope.area.data.serviceData.jkStoreChooser;
                        }));
                        if (storeChooser) {
                            scope.model.selectedStoreChooser = storeChooser.data.id;
                        }
                    }
                    scope.save = function () {
                        let addNewDeviceButtonConfig = scope.area;
                        addNewDeviceButtonConfig.data = {};
                        addNewDeviceButtonConfig.data.name = scope.model.buttonText;
                        addNewDeviceButtonConfig.data.serviceName = "pos.addNewDeviceService";
                        addNewDeviceButtonConfig.data.serviceAction = "doAction";
                        addNewDeviceButtonConfig.data.serviceData = {
                            jkStoreChooser: scope.model.selectedStoreChooser
                        };
                        addNewDeviceButtonConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});