//# sourceURL=pos.designer.designerSettings.gridDirectiveSettings.storesGridSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.gridDirectiveSettings.storesGridSettings"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope',
        function (initOptions, screenService, styleService, rootScope) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/gridDirectiveSettings/gridTypes/storesGrid/storesGridSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    scope.parentCss = styleService.getGlobalCssData().jkGrid.data.storeChooser.data || {};
                    if (scope.area && scope.area.data) {
                        scope.model.gridId = scope.area.data.id;
                    }
                    scope.save = function () {
                        var storesGridConfig = scope.area;
                        storesGridConfig.data = {};
                        storesGridConfig.data.id = scope.model.gridId;
                        storesGridConfig.data.service = {};
                        storesGridConfig.data.service.name = "pos.jkStoreChooserService";
                        storesGridConfig.data.service.columnDefs = "getColumnDefs";
                        storesGridConfig.data.service.data = "getStores";
                        storesGridConfig.data.service.eventName = "pos.jkStoreChooserService-stores";
                        storesGridConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});