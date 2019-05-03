//# sourceURL=pos.designer.designerSettings.gridDirectiveSettings.cashbookEntryGridSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.gridDirectiveSettings.cashbookEntryGridSettings"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope',
        function (initOptions, screenService, styleService, rootScope) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/gridDirectiveSettings/gridTypes/cashbookEntryGrid/cashbookEntryGridSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    scope.parentCss = styleService.getGlobalCssData().jkGrid.data.cashBookEntryGrid.data || {};
                    scope.options = {};
                    if (scope.area && scope.area.data) {
                        scope.model.gridId = scope.area.data.id;
                        scope.options = angular.copy(scope.area.data.service.options);
                    }
                    scope.save = function () {
                        let areaConfig = scope.area;
                        areaConfig.data = {};
                        areaConfig.data.id = scope.model.gridId;
                        areaConfig.data.service = {};
                        areaConfig.data.service.name = "pos.cashbookEntryGridService";
                        areaConfig.data.service.columnDefs = "getColumnDefs";
                        areaConfig.data.service.data = "getData";
                        areaConfig.data.service.eventName = "pos.cashbookEntryService-";
                        areaConfig.data.service.options = scope.options;
                        areaConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});