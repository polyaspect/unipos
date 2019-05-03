//# sourceURL=pos.designer.designerSettings.gridDirectiveSettings.orderGridSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.gridDirectiveSettings.orderGridSettings"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope',
        function (initOptions, screenService, styleService, rootScope) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/gridDirectiveSettings/gridTypes/orderGrid/orderGridSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    scope.parentCss = styleService.getGlobalCssData().jkGrid.data.orderDirective.data || {};
                    if (scope.area && scope.area.data) {
                        scope.model.gridId = scope.area.data.id;
                    }
                    scope.save = function () {
                        var orderGridConfig = scope.area;
                        orderGridConfig.data = {};
                        orderGridConfig.data.id = scope.model.gridId;
                        orderGridConfig.data.service = {};
                        orderGridConfig.data.service.name = "pos.orderDirectiveService";
                        orderGridConfig.data.service.columnDefs = "getColumnDefs";
                        orderGridConfig.data.service.data = "getData";
                        orderGridConfig.data.service.eventName = "orderService-orders";
                        orderGridConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});