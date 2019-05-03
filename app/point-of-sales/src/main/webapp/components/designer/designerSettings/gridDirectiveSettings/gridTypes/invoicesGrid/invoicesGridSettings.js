//# sourceURL=pos.designer.designerSettings.gridDirectiveSettings.invoicesGridSettings.js
define([
    'angular',
    "css!pos.designer.designerSettings.gridDirectiveSettings.invoicesGridSettings"
], function (angular) {
    return ['pos.initOptions', 'pos.designer.screenService', 'pos.designer.styleService', '$rootScope',
        function (initOptions, screenService, styleService, rootScope) {
            return {
                restrict: 'E',
                replace: true,
                scope: false,
                templateUrl: initOptions.baseDir + 'components/designer/designerSettings/gridDirectiveSettings/gridTypes/invoicesGrid/invoicesGridSettings.html',
                link: function (scope, el, attrs) {
                    if (scope.modelOptions.showDesignFirst) {
                        scope.activeTab = 1;
                    }
                    scope.model = {};
                    scope.parentCss = styleService.getGlobalCssData().jkGrid.data.showInvoicesGrid.data || {};
                    if (scope.area && scope.area.data) {
                        scope.model.gridId = scope.area.data.id;
                    }
                    scope.save = function () {
                        var invoicesGridConfig = scope.area;
                        invoicesGridConfig.data = {};
                        invoicesGridConfig.data.id = scope.model.gridId;
                        invoicesGridConfig.data.service = {};
                        invoicesGridConfig.data.service.name = "pos.showInvoicesGridService";
                        invoicesGridConfig.data.service.columnDefs = "getColumnDefs";
                        invoicesGridConfig.data.service.data = "getData";
                        invoicesGridConfig.data.service.eventName = "showInvoicesGridService-invoices";
                        invoicesGridConfig.render = true;

                        scope.$broadcast("saveStyle-" + scope.area.uuid);
                        rootScope.$emit(screenService.serviceName + "-" + scope.area.uuid);

                        scope.hide();
                    }
                }
            }
        }];
});