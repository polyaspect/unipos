//# sourceURL=pos.designer.designerSettings.gridDirectiveSettings.usersGridSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.gridDirectiveSettings.usersGridSettings"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope',
        function (initOptions, screenService, styleService, rootScope) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/gridDirectiveSettings/gridTypes/usersGrid/usersGridSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    scope.parentCss = styleService.getGlobalCssData().jkGrid.data.usersGrid.data || {};

                    scope.save = function () {
                        var gridConfig = scope.area;
                        gridConfig.data = {};
                        gridConfig.data.id = scope.model.gridId;
                        gridConfig.data.service = {};
                        gridConfig.data.service.name = "pos.usersGridService";
                        gridConfig.data.service.columnDefs = "getColumnDefs";
                        gridConfig.data.service.data = "getData";
                        gridConfig.data.service.eventName = "usersGridService-order";
                        gridConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});